package com.dog.data.repository

import com.dog.data.model.Image.ImageUploadResponse
import com.dog.data.model.user.FriendApiResponse
import com.dog.data.model.user.FriendRequestResponse
import com.dog.data.model.user.FriendResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface UploadRepository {
    @Multipart
    @POST("upload")
    suspend fun uploadImage(@Part image: MultipartBody.Part): Response<List<ImageUploadResponse>>
}