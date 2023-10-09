package com.gugu.dragon.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class Message(
    @SerialName("messageTime") val messageTime: String,
    @SerialName("primaryTeacher") val primaryTeacher: Boolean,
    @SerialName("fromRoomId") val fromRoomId: Int,
    @SerialName("isTeachersStudent") val isTeachersStudent: Boolean,
    @SerialName("nickName") val nickName: String,
    @SerialName("messageId") val messageId: String,
    @SerialName("newStudent") val newStudent: Boolean,
    @SerialName("verifyTime") val verifyTime: Long,
    @SerialName("type") val type: String,
    @SerialName("body") val body: String,
    @SerialName("userId") val userId: Long,
    @SerialName("uuid") val uuid: String,
    @SerialName("userImage") val userImage: String,
    @SerialName("multFlag") val multFlag: String,
    @SerialName("isMedal") val isMedal: Boolean,
    @SerialName("topic") val topic: String,
    @SerialName("from") val from: String,
    @SerialName("attributes") val attributes: String,
    @SerialName("isCrown") val isCrown: Boolean,
    @SerialName("contentType") val contentType: String,
    @SerialName("isComment") val isComment: Int,
    @SerialName("originalMessage") val originalMessage:String
)

@Serializable
data class Data(
    @SerialName("messages") val messages: List<Message>
)

@Serializable
data class Result(
    @SerialName("resultKey") val resultKey: String,
    @SerialName("data") val data: Data
)