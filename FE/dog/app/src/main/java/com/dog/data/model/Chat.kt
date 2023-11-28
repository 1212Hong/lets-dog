package com.dog.data.model

data class Chat(
    val id: Int,
    val content: String,
    val senderName: String,
    val sendTime: String,
    val direction: Boolean
)
