package com.dog.data.repository

import com.dog.data.model.chat.ChatHistoryResponse
import com.dog.data.model.chat.ChatListResponse
import com.dog.data.model.chat.ExitChatroomResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ChatRepository {

    @POST("chatroom")
    suspend fun createChatroom(@Body roomID: Int): Call<Response<ChatListResponse>>

    @GET("chatroom")
    suspend fun getChatroomList(): Response<ChatListResponse>

    @GET("chatroom/{roomId}")
    suspend fun getChatroomHistory(@Path("roomId") roomID: Int): Response<ChatHistoryResponse>

    @DELETE("chatroom/{roomId}")
    suspend fun disconnectChatroom(@Path("roomId") roomID: Int): Response<ExitChatroomResponse>
}
