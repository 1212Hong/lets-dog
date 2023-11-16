package com.dog.data.repository

import com.dog.data.model.email.EmailCodeResponse
import com.dog.data.model.email.EmailRequest
import com.dog.data.model.email.EmailValidationRequest
import com.dog.data.model.email.EmailValidationResponse
import com.dog.data.model.nickname.CheckDupNicknameResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface SignupRepository {

    @POST("email/verification-email")
    suspend fun sendEmailCode(@Body emailRequest: EmailRequest): Response<EmailCodeResponse>

    @PUT("email/verification-code")
    suspend fun emailValidation(@Body request: EmailValidationRequest): Response<EmailValidationResponse>

    @GET("user/duplicate-nickname/{nickname}")
    suspend fun checkDupNickname(@Path("nickname") nickname: String): Response<CheckDupNicknameResponse>

}
