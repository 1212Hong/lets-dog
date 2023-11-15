package com.dog.data.repository

import com.dog.data.model.user.FriendApiResponse
import com.dog.data.model.user.FriendRequestResponse
import com.dog.data.model.user.FriendResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface FriendRepository {
    @POST("friend/request/{receiverNickname}")
    suspend fun sendFriendRequest(@Path("receiverNickname") receiverNickname: String): Response<FriendResponse>

    @GET("friend/request/received")
    suspend fun getFriendRequest(): Response<FriendRequestResponse>

    @PUT("friend/accept/{requesterNickname}")
    suspend fun acceptFriendRequest(@Path("requesterNickname") requesterNickname: String): Response<FriendApiResponse>

    @POST("friend/decline/{requesterNickname}")
    suspend fun declineFriendRequest(@Path("requesterNickname") requesterNickname: String): Response<FriendApiResponse>
}