package com.dog.data.repository

import com.dog.data.model.comment.AddCommentRequest
import com.dog.data.model.comment.CommentAddResponse
import com.dog.data.model.comment.CommentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CommentRepository {

    @GET("comment")
    suspend fun getCommentListApiResponse(
        @Query("boardId") boardId: Long,
    ): Response<CommentResponse>

    @POST("comment")
    suspend fun addCommentApiResponse(@Body request: AddCommentRequest): Response<CommentAddResponse>
}
