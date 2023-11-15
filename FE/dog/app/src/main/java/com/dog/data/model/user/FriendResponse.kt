package com.dog.data.model.user

import com.dog.data.model.common.ResponseBodyResult
import com.dog.data.model.matching.Dog

data class FriendApiResponse(
    val result: ResponseBodyResult,
    val body: FriendResponse
)

data class FriendResponse(
    val senderNickname: String,
    val receiverNickname: String
)

data class FriendRequestResponse(
    val result: ResponseBodyResult,
    val body: List<UserBody>
)
