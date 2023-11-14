package com.dog.data.repository

import com.dog.data.model.feed.BoardResponse
import com.dog.data.model.feed.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface FeedRepository {
    @GET("boardnear")
    suspend fun getBoarderNearApiResponse(
        @Query("userLatitude") userLatitude: Double,
        @Query("userLongitude") userLongitude: Double,
        @Query("userNickname") userNickname: String
    ): Response<BoardResponse>

    @Multipart
    @POST("upload")
    suspend fun uploadImage(@Part image: List<MultipartBody.Part>): Response<UploadResponse>

    suspend fun getUserBoards(

    )
}
