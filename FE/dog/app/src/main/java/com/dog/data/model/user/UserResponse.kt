package com.dog.data.model.user

import com.dog.data.model.common.ResponseBodyResult

data class SignUpResponse(
    val responseBodyResult: ResponseBodyResult,
    val body: String? = null
)

data class SignInResponse(
    val result: ResponseBodyResult?,
    val body: LoginBody?
)
