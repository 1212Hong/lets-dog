package com.dog.data.model.chatHelthCheck


data class ChatHealthCheckRequest(
    val content: String,
    val content_type: String,
    val id: String,
    val read_count: Int,
    val room_id: Int,
    val send_time: Int,
    val sender_email: String,
    val sender_id: Int,
    val sender_name: String
)
