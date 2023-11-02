package com.dog.data.repository

import com.dog.data.model.user.signInRequest
import com.dog.data.model.user.signInResponse
import com.dog.data.model.user.signUpRequest
import com.dog.data.model.user.signUpResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface UserRepository {

    @POST("/user/signup")
    suspend fun signup(@Body request: signUpRequest): signUpResponse

    @POST("/user/signin")
    suspend fun signin(@Body request: signInRequest): signInResponse


}
