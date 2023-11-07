package com.dog.data.viewmodel.chat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.local.chatList
import com.dog.data.model.Chat
import com.dog.data.model.chat.ChatroomInfo
import com.dog.data.repository.ChatRepository
import com.dog.util.common.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    // 채팅 정보 저장
    private val _chatState = MutableStateFlow(chatList)
    val chatState: StateFlow<List<Chat>> = _chatState.asStateFlow()
    val _chatListState = MutableStateFlow<List<ChatroomInfo>>(emptyList())
    val chatListState: StateFlow<List<ChatroomInfo>> = _chatListState.asStateFlow()

    // Retrofit 인터페이스를 사용하려면 여기서 인스턴스를 생성합니다.
    private val chatApi: ChatRepository =
        RetrofitClient.getInstance().create(ChatRepository::class.java)
    var curMessage by mutableStateOf("")


    fun sendMessage(
        roomId: Int,
        senderId: Long,
        senderName: String,
    ) {
//        val chatState = ChatState(roomId, senderId, senderName, contentType, curMessage)
        val newId = chatState.value.size + 1;
        val newChat = Chat(newId, curMessage, senderName, "현재시간", false)
        updateChatState(newChat)
        curMessage = ""
    }

    private fun updateChatState(chat: Chat) {
//        _chatState.update { currentChatState ->
//            currentChatState.toMutableList().apply {
//                add(chat)
//            }
//        }
        val currentChatState = chatState.value.toMutableList() // 현재 상태를 가져옵니다.
        currentChatState.add(chat)
        _chatState.value = currentChatState // 수정된 목록을 다시 StateFlow에 할당합니다.
        Log.d("chatState", chatState.toString())
    }

    fun leaveChatroom(roomId: Int) {
        viewModelScope.launch {
            chatApi.disconnectChatroom(roomId)
        }

    }

    fun getChatList() {
        viewModelScope.launch {
            try {
                val res = chatApi.getChatroomList()

                if (res.isSuccessful) {
                    val responseBody = res.body()
                    if (responseBody != null) {
                        print(responseBody)
//                        _chatListState.value = responseBody.body // chatListState에 응답의 body를 할당
                    }
                } else {
                    val errorBody = res.errorBody()
                    if (errorBody != null) {
                        // 에러 응답 데이터를 처리
                    }
                }
            } catch (e: Exception) {
                Log.e("APIError", "API 호출 중 오류 발생: ${e.message}")
            }
        }

    }

    suspend fun getChatHistory(roomId: Int) {
        try {
            val res = chatApi.getChatroomHistory(roomId)

            // 여기에서 성공적인 응답 처리
            if (res.isSuccessful) {
                // API 호출이 성공했을 때의 처리
                val responseBody = res.body()
                print(responseBody)
                if (responseBody != null) {
                    // 응답 데이터를 사용하는 로직
                }
            } else {
                // API 호출은 성공적으로 완료되었지만, 서버에서 오류 응답을 반환했을 때의 처리
                val errorBody = res.errorBody()
                print(errorBody)
                if (errorBody != null) {
                    // 에러 응답 데이터를 처리
                }
            }
        } catch (e: Exception) {
            // API 호출 중에 예외가 발생한 경우의 처리
            Log.e("APIError", "API 호출 중 오류 발생: ${e.message}")
        }
    }

}
