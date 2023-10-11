package com.gugu.dragon.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class Message(
    @SerialName("messageTime") val messageTime: String = "",
    @SerialName("nickName") val nickName: String ="",
    @SerialName("messageId") val messageId: String ="",
    @SerialName("newStudent") val newStudent: Boolean = true,
    @SerialName("body") val body: String = "",
    @SerialName("uuid") val uuid: String = "",
    @SerialName("originalMessage") val originalMessage: String = ""
)

@Serializable
class OriginalMessage {
    @SerialName("body") val body: String = ""
}

@Serializable
data class Data(
    @SerialName("messages") var messages: List<Message>
)

@Serializable
data class Result(
    @SerialName("resultKey") val resultKey: String,
    @SerialName("data") val data: Data
)