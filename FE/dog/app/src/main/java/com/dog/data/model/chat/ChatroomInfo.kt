package com.dog.data.model.chat

data class ChatroomInfo(
    val roomId: Int,
    val roomTitle: String,
    val roomMembers: Map<String, String>
)
