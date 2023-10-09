package com.gugu.dragon.model


enum class MessageType {
    Text,
    Image
}

data class ChatMessageModel(
    val sender: String,
    val text: String,
    val originalText : String,
    val time: String,
    val messageType: MessageType = MessageType.Text,
    val imageResource: String = "",
    val imageResourceId: Int = 0
)