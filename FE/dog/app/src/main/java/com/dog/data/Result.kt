package com.dog.data

data class ErrorResponse(val msg: String? = null)
sealed class Result<out T> {

    data class Success<T>(val data: T) : com.dog.data.Result<T>()

    data class Loading(val loadingMessage: String? = null) :
        com.dog.data.Result<Nothing>()

    data class GenericError(val code: Int? = null, val error: ErrorResponse? = null) :
        com.dog.data.Result<Nothing>()

    object NetworkError : com.dog.data.Result<Nothing>()
}
