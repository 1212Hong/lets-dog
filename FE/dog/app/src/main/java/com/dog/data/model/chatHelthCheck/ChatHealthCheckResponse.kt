package com.dog.data.model.chatHelthCheck

import com.dog.data.model.common.ResponseBodyResult

data class ChatHealthCheckResponse(
    val responseBodyResult: ResponseBodyResult,
    val body: String
)
