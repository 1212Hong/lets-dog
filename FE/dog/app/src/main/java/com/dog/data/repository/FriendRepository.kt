package com.dog.data.repository

import com.dog.data.model.user.FriendListResponse
import com.dog.data.model.user.FriendResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FriendRepository {
    @POST("friend/request/{receiverNickname}")
    suspend fun sendFriendRequest(@Query("receiverNickname") receiverNickname: String): Response<FriendResponse>

    @GET("friend/friendlist")
    suspend fun getFriendListRequest(): Response<FriendListResponse>
}
