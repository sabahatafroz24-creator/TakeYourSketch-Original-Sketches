package com.example.ai

import com.example.BuildConfig
import com.example.data.SketchEntity
import com.example.model.ChatMessage
import com.example.model.MessageSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class SketchPickerAssistant {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .build()

    suspend fun consultAssistant(
        userPrompt: String,
        history: List<ChatMessage>,
        catalog: List<SketchEntity>
    ): ChatMessage = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Exception) {
            ""
        }

        val availableSketches = catalog.filter { !it.isSold }
        val catalogSummary = catalog.joinToString("\n") { sketch ->
            val status = if (sketch.isSold) "SOLD" else "AVAILABLE"
            "- \"${sketch.title}\" (${sketch.meta}), ₹${sketch.price.toInt()} [$status] - ${sketch.description}"
        }

        // Check if API key is real and valid (not empty or default placeholder)
        val hasRealApiKey = apiKey.isNotBlank() &&
                !apiKey.contains("MY_GEMINI_API_KEY", ignoreCase = true) &&
                apiKey.length > 10

        if (hasRealApiKey) {
            try {
                val apiResponse = callGeminiApi(apiKey, userPrompt, history, catalogSummary)
                if (apiResponse.isNotBlank()) {
                    val recommendedId = matchCatalogId(apiResponse, availableSketches)
                    return@withContext ChatMessage(
                        sender = MessageSender.ASSISTANT,
                        text = apiResponse,
                        recommendedSketchId = recommendedId
                    )
                }
            } catch (e: Exception) {
                // Fall back gracefully to studio curator heuristics
            }
        }

        // Studio Curator fallback logic
        val curatedResponse = generateCuratorResponse(userPrompt, availableSketches)
        ChatMessage(
            sender = MessageSender.ASSISTANT,
            text = curatedResponse.text,
            recommendedSketchId = curatedResponse.recommendedId
        )
    }

    private fun callGeminiApi(
        apiKey: String,
        userPrompt: String,
        history: List<ChatMessage>,
        catalogSummary: String
    ): String {
        val systemInstruction = """
            You are the sketch-picking assistant for TYS (Take Your Sketch), a gallery and shop selling one-of-a-kind original pencil, graphite, and ink sketches.
            Help the visitor pick a piece from the current catalog based on what they tell you about their taste, space, mood, or budget.
            Only recommend pieces marked "AVAILABLE" — never recommend a "SOLD" piece to buy.
            Keep replies short (2-4 sentences), warm, artistic, and specific. Name the actual sketch title and price (in ₹) when recommending.
            If nothing fits, reply honestly with a friendly artist note.
            
            Current catalog:
            $catalogSummary
        """.trimIndent()

        val contentsArray = JSONArray()

        // Include recent history (last 4 turns)
        history.takeLast(4).forEach { msg ->
            val role = if (msg.sender == MessageSender.USER) "user" else "model"
            contentsArray.put(
                JSONObject().apply {
                    put("role", role)
                    put("parts", JSONArray().put(JSONObject().put("text", msg.text)))
                }
            )
        }

        // Append current user prompt
        contentsArray.put(
            JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().put(JSONObject().put("text", userPrompt)))
            }
        )

        val rootJson = JSONObject().apply {
            put(
                "system_instruction",
                JSONObject().apply {
                    put("parts", JSONArray().put(JSONObject().put("text", systemInstruction)))
                }
            )
            put("contents", contentsArray)
            put(
                "generationConfig",
                JSONObject().apply {
                    put("temperature", 0.7)
                    put("maxOutputTokens", 300)
                }
            )
        }

        val requestBody = rootJson.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
            .post(requestBody)
            .build()

        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return ""
            val body = response.body?.string() ?: return ""
            val json = JSONObject(body)
            val candidates = json.optJSONArray("candidates") ?: return ""
            if (candidates.length() == 0) return ""
            val firstCandidate = candidates.getJSONObject(0)
            val content = firstCandidate.optJSONObject("content") ?: return ""
            val parts = content.optJSONArray("parts") ?: return ""
            if (parts.length() == 0) return ""
            return parts.getJSONObject(0).optString("text", "")
        }
    }

    private data class CuratorResult(val text: String, val recommendedId: String?)

    private fun generateCuratorResponse(
        prompt: String,
        available: List<SketchEntity>
    ): CuratorResult {
        val lower = prompt.lowercase()

        // 1. Cat / Animal / Pet / Charcoal
        if (lower.contains("cat") || lower.contains("pet") || lower.contains("animal") || lower.contains("sleep")) {
            val cat = available.find { it.id == "p3" }
            if (cat != null) {
                return CuratorResult(
                    "You'd love \"${cat.title}\" (${cat.meta}) for ₹${cat.price.toInt()}. It's an intimate 4-minute soft charcoal study that brings a quiet, serene presence to any bedside table.",
                    cat.id
                )
            } else {
                val alternative = available.firstOrNull { it.id == "p5" || it.id == "p6" }
                val altTitle = alternative?.title ?: "Study of Hands"
                val altPrice = alternative?.price?.toInt() ?: 320
                return CuratorResult(
                    "Our original \"Sleeping Cat\" sketch recently found a home, but for that same tranquil intimacy I'd suggest \"$altTitle\" at ₹$altPrice. It shares that quiet, personal sketchbook warmth.",
                    alternative?.id
                )
            }
        }

        // 2. Hands / Anatomical / Gesture
        if (lower.contains("hand") || lower.contains("gesture") || lower.contains("anatomy") || lower.contains("thread")) {
            val handThread = available.find { it.id == "p5" }
            val handStudy = available.find { it.id == "p1" }
            val chosen = handStudy ?: handThread ?: available.firstOrNull()
            if (chosen != null) {
                return CuratorResult(
                    "Take a look at \"${chosen.title}\" (${chosen.meta}) at ₹${chosen.price.toInt()}. It has exquisite graphite weight and anatomical nuance — wonderful when framed in a study or near a desk.",
                    chosen.id
                )
            }
        }

        // 3. Plants / Botanical / Nature / Green / Fern
        if (lower.contains("plant") || lower.contains("fern") || lower.contains("botanical") || lower.contains("nature") || lower.contains("leaf")) {
            val fern = available.find { it.id == "p6" }
            if (fern != null) {
                return CuratorResult(
                    "\"${fern.title}\" (${fern.meta}) at ₹${fern.price.toInt()} is wonderful. It was drawn with loose flex-nib ink strokes and brings an organic, airy calm to a shelf or entryway.",
                    fern.id
                )
            }
        }

        // 4. City / Architectural / Rooftop / Café / Street
        if (lower.contains("cafe") || lower.contains("coffee") || lower.contains("city") || lower.contains("urban") || lower.contains("street") || lower.contains("rooftop") || lower.contains("building") || lower.contains("alley")) {
            val cafe = available.find { it.id == "p2" }
            val rooftop = available.find { it.id == "p4" }
            val alley = available.find { it.id == "p7" }
            val chosen = cafe ?: rooftop ?: alley ?: available.firstOrNull()
            if (chosen != null) {
                return CuratorResult(
                    "I recommend \"${chosen.title}\" (${chosen.meta}) at ₹${chosen.price.toInt()}. It captures atmospheric architectural depth with pure ink lines that feel genuinely pulled straight from an urban sketchbook.",
                    chosen.id
                )
            }
        }

        // 5. Meditation / Intricate / Mandala
        if (lower.contains("mandala") || lower.contains("meditation") || lower.contains("detailed") || lower.contains("pattern") || lower.contains("mindful")) {
            val mandala = available.find { it.id == "p8" }
            if (mandala != null) {
                return CuratorResult(
                    "\"${mandala.title}\" (${mandala.meta}) at ₹${mandala.price.toInt()} by Saesha Chillarega is deeply mesmerizing. Drawn with micro-precision archival liners, it's designed to bring stillness to any room.",
                    mandala.id
                )
            }
        }

        // 6. Budget-conscious (e.g., under 300, 350, cheap)
        if (lower.contains("budget") || lower.contains("cheap") || lower.contains("under") || lower.contains("affordable") || lower.contains("small")) {
            val affordable = available.minByOrNull { it.price }
            if (affordable != null) {
                return CuratorResult(
                    "\"${affordable.title}\" (${affordable.meta}) at ₹${affordable.price.toInt()} is currently one of our most accessible originals. It is a genuine 1-of-1 sheet, packed flat in a rigid archival mailer.",
                    affordable.id
                )
            }
        }

        // Default friendly curator suggestion
        val picked = available.firstOrNull()
        if (picked != null) {
            return CuratorResult(
                "For a versatile, timeless piece, take a look at \"${picked.title}\" (${picked.meta}) at ₹${picked.price.toInt()}. Its subtle paper grain and expressive linework make it captivating in natural room lighting.",
                picked.id
            )
        }

        return CuratorResult(
            "Every original sketch on the wall right now has been claimed! Check back soon or speak with our artists via the Members panel as new sketchbook sheets are added weekly.",
            null
        )
    }

    private fun matchCatalogId(text: String, available: List<SketchEntity>): String? {
        val lower = text.lowercase()
        return available.firstOrNull { sketch ->
            lower.contains(sketch.title.lowercase())
        }?.id
    }
}
