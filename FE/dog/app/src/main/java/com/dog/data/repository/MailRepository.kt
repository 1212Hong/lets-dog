package com.dog.data.repository

import com.dog.data.model.email.EmailCodeResponse
import com.dog.data.model.email.EmailValidationRequest
import com.dog.data.model.email.EmailValidationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT


interface MailRepository {

    @POST("email/validation-email")
    suspend fun sendEmailCode(@Body email: String): Response<EmailCodeResponse>

    @PUT("email/validation-email")
    suspend fun emailValidation(@Body request: EmailValidationRequest): Response<EmailValidationResponse>


}
