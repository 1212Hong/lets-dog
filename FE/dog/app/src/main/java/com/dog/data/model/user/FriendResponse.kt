package com.dog.data.model.user

import com.dog.data.model.common.ResponseBodyResult

data class FriendApiResponse(
    val result: ResponseBodyResult,
    val body: List<FriendResponse>
)

data class FriendResponse(
    val senderNickname: String,
    val receiverNickname: String,
    val status: String,
)

data class FriendInfo(
    val userId: Int,
    val userLoginId: String,
    val userNickname: String,
    val userPhone: String,
    val userPicture: String,
    val userAboutMe: String,
    val userGender: String,
    val userLatitude: Double,
    val userLongitude: Double,
    val userAddress: String,
    val userIsRemoved: Boolean
)

data class FriendState(
    val userNickname: String,
    val userPicture: String,
    val isSelected: Boolean
)

data class FriendListResponse(
    val result: ResponseBodyResult,
    val body: List<FriendInfo>
)
