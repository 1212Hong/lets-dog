package com.dog.data.model.email

import com.dog.data.model.common.ResponseBodyResult

data class EmailValidationResponse(
    val result: ResponseBodyResult,
    val body: EmailCheck
)

data class EmailCodeResponse(
    val result: ResponseBodyResult,
    val body: Email
)

data class Email(
    val email: String
)
