package com.dog.data.repository

import com.dog.data.model.user.SignInRequest
import com.dog.data.model.user.SignInResponse
import com.dog.data.model.user.SignUpRequest
import com.dog.data.model.user.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface UserRepository {

    @POST("user/signup")
    suspend fun signup(@Body request: SignUpRequest): Response<SignUpResponse>

    @POST("user/login")
    suspend fun login(@Body request: SignInRequest): Response<SignInResponse>


}
