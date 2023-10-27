package com.dog.data.repository

import com.dog.data.model.chatHelthCheck.ChatHealthCheckRequest
import com.dog.data.model.chatHelthCheck.ChatHealthCheckResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface ChatRepository {
    @POST("/chat/test")
    suspend fun sendChatMessage(@Body request: ChatHealthCheckRequest): ChatHealthCheckResponse

    /*

    //        val chatApi = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
//        GlobalScope.launch {
//            val result = chatApi.sendChatMessage(
//                ChatRequest(
//                    id = "string",
//                    room_id = 0,
//                    content_type = "string",
//                    content = "string",
//                    sender_name = "string",
//                    sender_id = 0,
//                    send_time = 0,
//                    read_count = 0,
//                    sender_email = "string"
//                )
//            )
//            Log.d("test", chatApi.toString())
//            if (result != null) {
//                Log.d("여기인가?: ", result.toString())
//            }
//        }
//    private lateinit var client: OkHttpClient

     */
}
