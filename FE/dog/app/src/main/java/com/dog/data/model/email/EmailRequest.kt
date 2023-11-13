package com.dog.data.model.email

data class EmailValidationRequest(
    val email: String,
    val token: String
)

