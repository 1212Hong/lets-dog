package com.dog.data.model.comment

import com.google.gson.annotations.SerializedName


data class DeleteCommentResponse(
    @SerializedName("result") val result: DeleteCommentResult,
    @SerializedName("body") val body: String
)

data class DeleteCommentResult(
    @SerializedName("code") val code: Long,
    @SerializedName("message") val message: String,
    @SerializedName("description") val description: String
)
