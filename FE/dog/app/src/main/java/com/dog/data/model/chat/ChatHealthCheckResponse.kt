package com.dog.data.model.chat

import com.dog.data.model.common.ResponseBodyResult

data class ChatHealthCheckResponse(
    val responseBodyResult: ResponseBodyResult,
    val body: String
)
