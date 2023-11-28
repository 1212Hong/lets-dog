package com.dog.data.model.feed

import com.google.gson.annotations.SerializedName

data class PostFeedResponseBody(
    @SerializedName("result") val result: PostFeedResponseResult,
    @SerializedName("body") val body: String
)

data class PostFeedResponseResult(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("description") val description: String
)
