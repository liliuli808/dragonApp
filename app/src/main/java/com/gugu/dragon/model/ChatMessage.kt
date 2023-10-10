package com.gugu.dragon.model

import kotlinx.serialization.Serializable


data class ChatMessageModel(
    val sender: String,
    val text: String,
    val originalText : String,
    val time: String,
    val imageResource: List<String>,
    val originalResource: List<String>,
    val imageResourceId: Int = 0
)