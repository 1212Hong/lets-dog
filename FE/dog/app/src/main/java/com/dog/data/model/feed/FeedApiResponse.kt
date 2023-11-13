package com.dog.data.model.feed

import com.google.gson.annotations.SerializedName

data class ResponseResult(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("description") val description: String
)

data class BoardItem(
    @SerializedName("userNickname") val userNickname: String,
    @SerializedName("boardId") val boardId: Long,
    @SerializedName("boardContent") val boardContent: String,
    @SerializedName("boardScope") val boardScope: String,
    @SerializedName("boardLikes") val boardLikes: Long,
    @SerializedName("fileUrlLists") val fileUrlLists: List<String>,
    @SerializedName("boardComments") val boardComments: Long,
    @SerializedName("likecheck") val likecheck: Boolean,
    @SerializedName("profileUrl") val profileUrl: String,
)

data class BoardResponse(
    @SerializedName("result") val result: ResponseResult,
    @SerializedName("body") val body: List<BoardItem>
)
