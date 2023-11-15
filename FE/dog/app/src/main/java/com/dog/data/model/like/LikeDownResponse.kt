package com.dog.data.model.like

import com.google.gson.annotations.SerializedName

data class LikeDownResponse(
    @SerializedName("result")
    val result: LikeDownResult,
    @SerializedName("body")
    val body: String
)

data class LikeDownResult(
    @SerializedName("code")
    val code: Long,
    @SerializedName("message")
    val message: String,
    @SerializedName("description")
    val description: String
)
