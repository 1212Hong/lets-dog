package com.dog.data.repository

import com.dog.data.model.chat.ChatHistoryResponse
import com.dog.data.model.chat.ChatListResponse
import com.dog.data.model.chat.CreateChatroomRequest
import com.dog.data.model.chat.CreateChatroomResponse
import com.dog.data.model.chat.ExitChatroomResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ChatRepository {

    @POST("chatroom")
    suspend fun createChatroom(@Body request: CreateChatroomRequest): Response<CreateChatroomResponse>

    @GET("chatroom")
    suspend fun getChatroomList(): Response<ChatListResponse>

    @GET("chatroom/{roomId}")
    suspend fun getChatroomHistory(@Path("roomId") roomID: Long): Response<ChatHistoryResponse>

    @DELETE("chatroom/{roomId}")
    suspend fun disconnectChatroom(@Path("roomId") roomID: Long): Response<ExitChatroomResponse>
}
