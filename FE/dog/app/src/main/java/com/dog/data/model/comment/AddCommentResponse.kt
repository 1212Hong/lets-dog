package com.dog.data.model.comment

import com.google.gson.annotations.SerializedName

data class CommentAddResponse(
    @SerializedName("result") val result: AddCommentResponseResult,
    @SerializedName("body") val body: String
)

data class AddCommentResponseResult(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("description") val description: String
)
