package com.dog.data.model.chat

data class CreateChatroomRequest(
    val roomTitle: String,
    val userNicks: List<String?>
)
