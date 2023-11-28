package com.dog.data.model.like

import com.google.gson.annotations.SerializedName

data class LikeUpRequest(
    @SerializedName("boardId")
    val boardId: Long
)
