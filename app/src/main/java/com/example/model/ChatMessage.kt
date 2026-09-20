package com.example.model

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val recommendedSketchId: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

enum class MessageSender {
    USER,
    ASSISTANT
}
