package com.dog.data.model.user

data class UserUpdateRequest(

    val userNickname: String,
    val userPhone: String,
    val userPicture: String,
    val userAboutMe: String,
    val userGender: String?,
    val userLatitude: Double?,
    val userLongitude: Double?,
    val userAddress: String?,
)

