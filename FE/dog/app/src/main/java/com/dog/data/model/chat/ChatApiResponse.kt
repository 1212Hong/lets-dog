package com.dog.data.model.chat

import com.dog.data.model.common.ResponseBodyResult
import com.google.gson.annotations.SerializedName


data class ChatListResponse(
    @SerializedName("result") val result: ResponseBodyResult,
    @SerializedName("body") val body: List<ChatroomInfo>
)

data class ChatHistoryResponse(
    @SerializedName("result") val result: ResponseBodyResult,
    @SerializedName("body") val body: List<ChatState>
)

data class ExitChatroomResponse(
    @SerializedName("result") val result: ResponseBodyResult,
    @SerializedName("body") val body: String
)
