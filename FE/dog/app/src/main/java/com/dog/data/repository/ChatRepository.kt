package com.dog.data.repository

import com.dog.data.model.chat.ChatListResponse
import com.dog.data.model.common.Response
import com.dog.data.viewmodel.chat.ChatState
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ChatRepository {

    @POST("/chatroom")
    suspend fun createChatroom(@Body roomID: Int): Call<Response<ChatListResponse>>

    @GET("/chatroom")
    suspend fun getChatroomList(): Call<Response<ChatListResponse>>

    @GET("/chatroom/{roomId}")
    suspend fun getChatroomHistory(@Path("roomID") roomID: Int): Call<Response<ChatState>>

    @DELETE("/chatroom/{roomId}")
    suspend fun disconnectChatroom(@Path("roomID") roomID: Int): Call<Response<String>>
}
