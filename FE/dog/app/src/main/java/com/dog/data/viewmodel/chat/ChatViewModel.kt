package com.dog.data.viewmodel.chat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dog.data.local.chatList
import com.dog.data.model.Chat
import com.dog.data.repository.ChatRepository
import com.dog.util.common.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatViewModel : ViewModel() {
    // 채팅 정보 저장
    private val _chatState = MutableStateFlow(chatList)
    val chatState: StateFlow<List<Chat>> = _chatState.asStateFlow()

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

    suspend fun leaveChatroom(roomId: Int) {
        chatApi.disconnectChatroom(roomId)
    }

    suspend fun getChatList() {
        val chatList = chatApi.getChatroomList()
        Log.d("list", chatList.toString())
    }

    suspend fun getChatHistory(roomId: Int) {
        val res = chatApi.getChatroomHistory(roomId)
        Log.d("res", res.toString())
    }

}
