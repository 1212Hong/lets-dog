package com.dog.data.repository

import com.dog.data.model.user.SignInRequest
import com.dog.data.model.user.SignInResponse
import com.dog.data.model.user.SignUpRequest
import com.dog.data.model.user.SignUpResponse
import com.dog.data.model.user.UserInfoResponse
import com.dog.data.model.user.UserUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path


interface UserRepository {

    @POST("user/signup")
    suspend fun signup(@Body request: SignUpRequest): Response<SignUpResponse>

    @POST("user/login")
    suspend fun login(@Body request: SignInRequest): Response<SignInResponse>

    @GET("user/get/{nickname}")
    suspend fun getUserInfo(@Path("nickname") nickname: String): Response<UserInfoResponse>

    @PATCH("user/update")
    suspend fun updateUserProfile(@Body userUpdateRequest: UserUpdateRequest): Response<UserInfoResponse>

}
