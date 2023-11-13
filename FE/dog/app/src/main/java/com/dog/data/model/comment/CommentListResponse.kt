package com.dog.data.model.comment

import com.google.gson.annotations.SerializedName

data class CommentItem(
    @SerializedName("boardId") val boardId: Long,
    @SerializedName("commentId") val commentId: Long,
    @SerializedName("commentContent") val commentContent: String,
    @SerializedName("userNickname") val userNickname: String,
    @SerializedName("commentLikes") val commentLikes: Long,
    @SerializedName("userProfileUrl") val userProfileUrl: String,
)

data class CommentResponse(
    @SerializedName("result") val result: ResponseResult,
    @SerializedName("body") val comments: List<CommentItem>
)

data class ResponseResult(
    @SerializedName("code") val code: Long,
    @SerializedName("message") val message: String,
    @SerializedName("description") val description: String
)
