package com.dog.data.model.user

import com.dog.data.model.common.ResponseBodyResult

data class signUpResponse(
    val responseBodyResult: ResponseBodyResult,
    val body: String? = null
)

data class signInResponse(
    val responseBodyResult: ResponseBodyResult,
    val body: String? = null
)
