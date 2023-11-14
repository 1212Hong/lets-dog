package com.dog.data.repository

import com.dog.data.model.dog.DogResponse
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


interface DogRepository {

    @POST("dog")
    suspend fun signup(@Body request: SignUpRequest): Response<SignUpResponse>

    @GET("dog/bynick/{nickname}")
    suspend fun getDogs(@Path("nickname") nickname: String): Response<DogResponse>

}
