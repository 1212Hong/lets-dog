package com.dog.data.model.Image

import com.dog.data.model.common.ResponseBodyResult
import com.dog.data.model.matching.Dog
import com.dog.data.model.matching.MatchingUserResponse
import com.google.gson.annotations.SerializedName

data class ImageUploadResponse(
    val result: ResponseBodyResult,
    val body: UploadResponse
)

data class UploadResponse(
    @SerializedName("file_pk")
    val filePk: Int,
    val url: String,
)

