package com.dog.data.model.matching

import com.dog.data.model.common.ResponseBodyResult
import com.google.gson.annotations.SerializedName

data class MatchingApiResponse(
    @SerializedName("result") val result: ResponseBodyResult,
    @SerializedName("body") val body: List<MatchingUserResponse>
)

data class MatchingUserResponse(
    @SerializedName("user_login_id") val loginId: String,
    @SerializedName("user_nickname") val nickname: String,
    @SerializedName("user_picture") val picture: String,
    @SerializedName("user_about_me") val aboutMe: String,
    @SerializedName("user_gender") val gender: String,
    @SerializedName("user_latitude") val latitude: Double,
    @SerializedName("user_longitude") val longitude: Double,
    @SerializedName("user_address") val address: String?,
    @SerializedName("dogs") val dogs: List<Dog>?
)

data class Dog(
    @SerializedName("dog_id") val dogId: String,
    @SerializedName("dog_name") val dogName: String,
    @SerializedName("dog_disposition_list") val dogDispositionList: List<String>
)
