package com.dog.data.repository

import com.dog.data.model.chatHelthCheck.ChatListResponse
import com.dog.data.model.chatHelthCheck.ChatRoomRequest
import com.dog.data.model.common.ResponseBodyResult
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ChatRepository {
    @POST("/api/chatroom")
    suspend fun makeChatRoom(@Body request: ChatRoomRequest): ResponseBodyResult

    @GET("/api/chatroom")
    suspend fun getChatList(): ChatListResponse

    @DELETE("/api/chatroom/{roomId}")
    suspend fun exitChatRoom(@Path("roomId") roomId: String)

}
