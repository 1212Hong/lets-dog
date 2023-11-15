package com.dog.data.model.feed

import com.google.gson.annotations.SerializedName

data class DeleteFeedResponse(
    @SerializedName("result")
    val result: DeleteFeedResult,
    @SerializedName("body")
    val body: String
)

data class DeleteFeedResult(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("description")
    val description: String
)
