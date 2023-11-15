package com.dog.data.repository

import com.dog.data.model.dog.DogInfo
import com.dog.data.model.dog.DogResponse
import com.dog.data.model.user.SignUpRequest
import com.dog.data.model.user.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface DogRepository {

    @POST("dog")
    suspend fun signup(@Body request: SignUpRequest): Response<SignUpResponse>

    @GET("dog/bynick/{nickname}")
    suspend fun getDogs(@Path("nickname") nickname: String): Response<DogResponse>

    @PUT("dog")
    suspend fun updateDog(@Body dogInfo: DogInfo): Response<DogResponse>
}
