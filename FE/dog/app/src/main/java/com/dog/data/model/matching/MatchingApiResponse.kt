package com.dog.data.model.matching

import com.dog.data.model.common.ResponseBodyResult

data class MatchingApiResponse(
    val result: ResponseBodyResult,
    val body: List<MatchingUserResponse>
)

data class MatchingUserResponse(
    val userLoginId: String,
    val userNickname: String,
    val userPicture: String,
    val userAboutMe: String,
    val userGender: String,
    val userLatitude: Double,
    val userLongitude: Double,
    val userAddress: String?,
    val dogs: List<Dog>?
)

data class Dog(
    val dogId: String,
    val dogName: String,
    val dogBreed: String,
    val dogDispositionList: List<String>,
    val dogPicture: String,
)
