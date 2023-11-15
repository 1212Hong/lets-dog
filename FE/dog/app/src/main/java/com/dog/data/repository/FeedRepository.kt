package com.dog.data.repository

import com.dog.data.model.feed.BoardRequest
import com.dog.data.model.feed.BoardResponse
import com.dog.data.model.feed.DeleteFeedResponse
import com.dog.data.model.feed.ResponseBody
import com.dog.data.model.feed.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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
    ): Response<BoardResponse>

    @POST("board")
    suspend fun PostFeedApiResponse(@Body request: BoardRequest): Response<ResponseBody>

    @DELETE("board")
    suspend fun deleteFeedApiResponse(@Query("boardId") boardId: Long): Response<DeleteFeedResponse>


    @Multipart
    @POST("upload")
    suspend fun uploadImage(@Part image: List<MultipartBody.Part>): Response<UploadResponse>

    suspend fun getUserBoards(

    )
}
