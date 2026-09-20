package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.data.SketchEntity
import com.example.ui.theme.Graphite
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.PaperDark
import com.example.ui.theme.PaperSheet

@Composable
fun SketchArtView(
    sketch: SketchEntity,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(PaperSheet)
    ) {
        if (sketch.isCustomUpload && !sketch.customImageUri.isNullOrBlank()) {
            AsyncImage(
                model = sketch.customImageUri,
                contentDescription = sketch.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            SketchVectorCanvas(drawingKey = sketch.drawingKey)
        }
    }
}

@Composable
fun SketchVectorCanvas(
    drawingKey: String,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val inkColor = InkPrimary
        val gridColor = Color(0x186B6659)

        // Subtle horizontal sketchbook ruling lines
        val lineSpacing = h / 7f
        for (i in 1..6) {
            drawLine(
                color = gridColor,
                start = Offset(0f, i * lineSpacing),
                end = Offset(w, i * lineSpacing),
                strokeWidth = 1f
            )
        }

        val stroke = Stroke(
            width = 3.5f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        val fineStroke = Stroke(
            width = 2f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        val dashStroke = Stroke(
            width = 2f,
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
        )

        when (drawingKey) {
            "hands_study" -> {
                // Study of Hands
                val p = Path().apply {
                    moveTo(w * 0.25f, h * 0.85f)
                    cubicTo(w * 0.28f, h * 0.65f, w * 0.30f, h * 0.45f, w * 0.38f, h * 0.35f)
                    cubicTo(w * 0.42f, h * 0.32f, w * 0.46f, h * 0.34f, w * 0.46f, h * 0.40f)
                    cubicTo(w * 0.46f, h * 0.50f, w * 0.43f, h * 0.60f, w * 0.43f, h * 0.67f)
                }
                drawPath(p, inkColor, style = stroke)

                val p2 = Path().apply {
                    moveTo(w * 0.46f, h * 0.40f)
                    cubicTo(w * 0.48f, h * 0.33f, w * 0.53f, h * 0.32f, w * 0.54f, h * 0.39f)
                    cubicTo(w * 0.55f, h * 0.47f, w * 0.52f, h * 0.57f, w * 0.51f, h * 0.67f)
                }
                drawPath(p2, inkColor, style = stroke)

                val p3 = Path().apply {
                    moveTo(w * 0.54f, h * 0.39f)
                    cubicTo(w * 0.58f, h * 0.33f, w * 0.63f, h * 0.35f, w * 0.62f, h * 0.42f)
                    cubicTo(w * 0.61f, h * 0.50f, w * 0.57f, h * 0.59f, w * 0.56f, h * 0.67f)
                }
                drawPath(p3, inkColor, style = stroke)

                val p4 = Path().apply {
                    moveTo(w * 0.62f, h * 0.42f)
                    cubicTo(w * 0.65f, h * 0.38f, w * 0.69f, h * 0.41f, w * 0.68f, h * 0.48f)
                    cubicTo(w * 0.66f, h * 0.56f, w * 0.62f, h * 0.62f, w * 0.60f, h * 0.69f)
                }
                drawPath(p4, inkColor, style = stroke)

                val palm = Path().apply {
                    moveTo(w * 0.25f, h * 0.85f)
                    cubicTo(w * 0.38f, h * 0.92f, w * 0.54f, h * 0.93f, w * 0.66f, h * 0.86f)
                }
                drawPath(palm, inkColor, style = stroke)
            }

            "cafe_scene" -> {
                // Corner Café, 7am
                val bldg = Path().apply {
                    moveTo(w * 0.12f, h * 0.85f)
                    lineTo(w * 0.12f, h * 0.45f)
                    lineTo(w * 0.38f, h * 0.32f)
                    lineTo(w * 0.64f, h * 0.45f)
                    lineTo(w * 0.64f, h * 0.85f)
                }
                drawPath(bldg, inkColor, style = stroke)

                // Awning & side
                val side = Path().apply {
                    moveTo(w * 0.64f, h * 0.58f)
                    lineTo(w * 0.92f, h * 0.58f)
                    lineTo(w * 0.92f, h * 0.85f)
                    lineTo(w * 0.64f, h * 0.85f)
                }
                drawPath(side, inkColor, style = stroke)

                // Door & window
                drawRect(
                    color = inkColor,
                    topLeft = Offset(w * 0.22f, h * 0.64f),
                    size = androidx.compose.ui.geometry.Size(w * 0.14f, h * 0.21f),
                    style = fineStroke
                )
                drawRect(
                    color = inkColor,
                    topLeft = Offset(w * 0.72f, h * 0.67f),
                    size = androidx.compose.ui.geometry.Size(w * 0.14f, h * 0.18f),
                    style = fineStroke
                )
                // Ground baseline
                drawLine(
                    color = inkColor,
                    start = Offset(w * 0.08f, h * 0.85f),
                    end = Offset(w * 0.95f, h * 0.85f),
                    strokeWidth = 3f
                )
                // Street lamp
                drawCircle(inkColor, radius = 4f, center = Offset(w * 0.38f, h * 0.42f))
            }

            "sleeping_cat" -> {
                // Sleeping Cat
                val catBody = Path().apply {
                    moveTo(w * 0.22f, h * 0.76f)
                    cubicTo(w * 0.22f, h * 0.56f, w * 0.35f, h * 0.44f, w * 0.60f, h * 0.45f)
                    cubicTo(w * 0.85f, h * 0.46f, w * 0.96f, h * 0.59f, w * 0.93f, h * 0.75f)
                    cubicTo(w * 0.90f, h * 0.85f, w * 0.28f, h * 0.85f, w * 0.22f, h * 0.76f)
                    close()
                }
                drawPath(catBody, inkColor, style = stroke)

                // Ears
                val ear1 = Path().apply {
                    moveTo(w * 0.35f, h * 0.47f)
                    lineTo(w * 0.32f, h * 0.34f)
                    lineTo(w * 0.43f, h * 0.43f)
                }
                drawPath(ear1, inkColor, style = stroke)

                val ear2 = Path().apply {
                    moveTo(w * 0.72f, h * 0.46f)
                    lineTo(w * 0.78f, h * 0.33f)
                    lineTo(w * 0.83f, h * 0.44f)
                }
                drawPath(ear2, inkColor, style = stroke)

                // Spine curve
                val spine = Path().apply {
                    moveTo(w * 0.60f, h * 0.45f)
                    cubicTo(w * 0.63f, h * 0.56f, w * 0.63f, h * 0.66f, w * 0.60f, h * 0.74f)
                }
                drawPath(spine, inkColor, style = fineStroke)
            }

            "rooftops" -> {
                // Rooftops, West Side
                val roofs = Path().apply {
                    moveTo(w * 0.06f, h * 0.85f)
                    lineTo(w * 0.06f, h * 0.60f)
                    lineTo(w * 0.25f, h * 0.47f)
                    lineTo(w * 0.25f, h * 0.85f)

                    moveTo(w * 0.25f, h * 0.85f)
                    lineTo(w * 0.25f, h * 0.66f)
                    lineTo(w * 0.47f, h * 0.53f)
                    lineTo(w * 0.47f, h * 0.85f)

                    moveTo(w * 0.47f, h * 0.85f)
                    lineTo(w * 0.47f, h * 0.40f)
                    lineTo(w * 0.63f, h * 0.30f)
                    lineTo(w * 0.78f, h * 0.40f)
                    lineTo(w * 0.78f, h * 0.85f)

                    moveTo(w * 0.78f, h * 0.85f)
                    lineTo(w * 0.78f, h * 0.63f)
                    lineTo(w * 0.95f, h * 0.50f)
                    lineTo(w * 0.95f, h * 0.85f)
                }
                drawPath(roofs, inkColor, style = stroke)

                // Baseline
                drawLine(
                    color = inkColor,
                    start = Offset(w * 0.05f, h * 0.85f),
                    end = Offset(w * 0.95f, h * 0.85f),
                    strokeWidth = 3f
                )
            }

            "hands_thread" -> {
                // Hands with Thread
                val leftThumb = Path().apply {
                    moveTo(w * 0.38f, h * 0.75f)
                    cubicTo(w * 0.44f, h * 0.63f, w * 0.50f, h * 0.56f, w * 0.63f, h * 0.55f)
                    cubicTo(w * 0.75f, h * 0.54f, w * 0.84f, h * 0.59f, w * 0.88f, h * 0.69f)
                }
                drawPath(leftThumb, inkColor, style = stroke)

                // Dashed thread
                val thread = Path().apply {
                    moveTo(w * 0.63f, h * 0.55f)
                    cubicTo(w * 0.61f, h * 0.47f, w * 0.60f, h * 0.40f, w * 0.58f, h * 0.34f)
                }
                drawPath(thread, inkColor, style = dashStroke)

                // Base palm
                val palm = Path().apply {
                    moveTo(w * 0.47f, h * 0.69f)
                    cubicTo(w * 0.56f, h * 0.74f, w * 0.72f, h * 0.74f, w * 0.80f, h * 0.68f)
                }
                drawPath(palm, inkColor, style = fineStroke)
            }

            "potted_fern" -> {
                // Potted Fern
                val pot = Path().apply {
                    moveTo(w * 0.41f, h * 0.88f)
                    lineTo(w * 0.59f, h * 0.88f)
                    lineTo(w * 0.55f, h * 0.66f)
                    lineTo(w * 0.45f, h * 0.66f)
                    close()
                }
                drawPath(pot, inkColor, style = stroke)

                // Central stalk
                drawLine(
                    color = inkColor,
                    start = Offset(w * 0.50f, h * 0.66f),
                    end = Offset(w * 0.50f, h * 0.38f),
                    strokeWidth = 3f,
                    cap = StrokeCap.Round
                )

                // Curved fronds
                val frond1 = Path().apply {
                    moveTo(w * 0.50f, h * 0.60f)
                    cubicTo(w * 0.44f, h * 0.54f, w * 0.35f, h * 0.52f, w * 0.28f, h * 0.44f)
                }
                drawPath(frond1, inkColor, style = stroke)

                val frond2 = Path().apply {
                    moveTo(w * 0.50f, h * 0.54f)
                    cubicTo(w * 0.56f, h * 0.48f, w * 0.65f, h * 0.46f, w * 0.72f, h * 0.38f)
                }
                drawPath(frond2, inkColor, style = stroke)

                val frond3 = Path().apply {
                    moveTo(w * 0.50f, h * 0.48f)
                    cubicTo(w * 0.45f, h * 0.42f, w * 0.39f, h * 0.39f, w * 0.33f, h * 0.31f)
                }
                drawPath(frond3, inkColor, style = stroke)

                val frond4 = Path().apply {
                    moveTo(w * 0.50f, h * 0.42f)
                    cubicTo(w * 0.55f, h * 0.35f, w * 0.61f, h * 0.32f, w * 0.68f, h * 0.26f)
                }
                drawPath(frond4, inkColor, style = stroke)
            }

            "alleyway" -> {
                // Old Town Alleyway
                val arch = Path().apply {
                    moveTo(w * 0.20f, h * 0.88f)
                    lineTo(w * 0.20f, h * 0.45f)
                    cubicTo(w * 0.25f, h * 0.30f, w * 0.75f, h * 0.30f, w * 0.80f, h * 0.45f)
                    lineTo(w * 0.80f, h * 0.88f)
                }
                drawPath(arch, inkColor, style = stroke)

                // Cobblestone strokes
                val stones = Path().apply {
                    moveTo(w * 0.32f, h * 0.70f)
                    lineTo(w * 0.42f, h * 0.70f)
                    moveTo(w * 0.55f, h * 0.72f)
                    lineTo(w * 0.68f, h * 0.72f)
                    moveTo(w * 0.36f, h * 0.80f)
                    lineTo(w * 0.48f, h * 0.80f)
                    moveTo(w * 0.58f, h * 0.82f)
                    lineTo(w * 0.72f, h * 0.82f)
                }
                drawPath(stones, inkColor, style = fineStroke)

                // Hanging lantern
                drawLine(
                    color = inkColor,
                    start = Offset(w * 0.50f, h * 0.33f),
                    end = Offset(w * 0.50f, h * 0.45f),
                    strokeWidth = 2f
                )
                drawCircle(inkColor, radius = 8f, center = Offset(w * 0.50f, h * 0.48f))
            }

            "mandala" -> {
                // Mandala Meditation
                val center = Offset(w * 0.50f, h * 0.50f)
                val baseRadius = w * 0.12f
                drawCircle(inkColor, radius = baseRadius, center = center, style = stroke)
                drawCircle(inkColor, radius = baseRadius * 1.8f, center = center, style = fineStroke)
                drawCircle(inkColor, radius = baseRadius * 2.6f, center = center, style = dashStroke)
                drawCircle(inkColor, radius = 5f, center = center)

                // 8 radial petals
                for (i in 0 until 8) {
                    val angle = (i * Math.PI / 4).toFloat()
                    val pEnd = Offset(
                        center.x + (baseRadius * 2.6f) * kotlin.math.cos(angle),
                        center.y + (baseRadius * 2.6f) * kotlin.math.sin(angle)
                    )
                    drawLine(
                        color = inkColor,
                        start = center,
                        end = pEnd,
                        strokeWidth = 1.5f
                    )
                }
            }

            else -> {
                // Generic artistic sketch outline
                val generic = Path().apply {
                    moveTo(w * 0.25f, h * 0.75f)
                    cubicTo(w * 0.20f, h * 0.45f, w * 0.45f, h * 0.25f, w * 0.65f, h * 0.35f)
                    cubicTo(w * 0.85f, h * 0.45f, w * 0.80f, h * 0.70f, w * 0.50f, h * 0.75f)
                }
                drawPath(generic, inkColor, style = stroke)
            }
        }
    }
}
