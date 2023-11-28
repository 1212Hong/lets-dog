package com.dog.data.model.feed

import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("body") val body: Body,
    @SerializedName("result") val result: DeleteFeedResult
)

data class Body(
    @SerializedName("file_pk") val filePk: Long,
    @SerializedName("url") val url: String
)

data class Result(
    @SerializedName("code") val code: Long,
    @SerializedName("description") val description: String,
    @SerializedName("message") val message: String
)
