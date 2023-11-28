package com.dog.data.repository

import com.dog.data.model.like.LikeDownResponse
import com.dog.data.model.like.LikeUpRequest
import com.dog.data.model.like.LikeUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Query

interface LikeRepository {
    @POST("like")
    suspend fun likeUp(@Body request: LikeUpRequest): Response<LikeUpResponse>

    @DELETE("like")
    suspend fun likeDown(
        @Query("boardId") boardId: Long
    ): Response<LikeDownResponse>
}
