package com.dog.data.model.common


data class Response<T>(
    val result: ResponseBodyResult,
    val body: T
)
