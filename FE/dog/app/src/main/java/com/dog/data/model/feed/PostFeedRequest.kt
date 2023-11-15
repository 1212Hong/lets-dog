package com.dog.data.model.feed

import com.google.gson.annotations.SerializedName

data class BoardRequest(
    @SerializedName("boardContent") val boardContent: String,
    @SerializedName("boardScope") val boardScope: String,
    @SerializedName("boardLikes") val boardLikes: Int,
    @SerializedName("fileUrlLists") val fileUrlLists: List<String>,
    @SerializedName("boardComments") val boardComments: Int
)
