package com.dog.data.model.like

import com.dog.data.model.feed.BoardItem
import com.dog.data.model.feed.ResponseResult
import com.google.gson.annotations.SerializedName

data class ResponseResult(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("description") val description: String
)

data class LikeResponse(
    @SerializedName("result") val result: ResponseResult,
    @SerializedName("body") val body: List<BoardItem>
)
