package com.dog.data.model.like

import com.google.gson.annotations.SerializedName

data class LikeUpResponse(
    @SerializedName("result")
    val result: LikeUpResult,
    @SerializedName("body")
    val body: String
)

data class LikeUpResult(
    @SerializedName("code")
    val code: Long,
    @SerializedName("message")
    val message: String,
    @SerializedName("description")
    val description: String
)
