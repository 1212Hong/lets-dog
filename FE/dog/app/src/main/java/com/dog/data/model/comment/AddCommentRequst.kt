package com.dog.data.model.comment

import com.google.gson.annotations.SerializedName

data class AddCommentRequest(
    @SerializedName("boardId") val boardId: Long,
    @SerializedName("commentContent") val commentContent: String,
    @SerializedName("userNickname") val userNickname: String
)
