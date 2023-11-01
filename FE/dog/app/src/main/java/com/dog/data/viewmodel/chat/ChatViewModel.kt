package com.dog.data.viewmodel.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatViewModel : ViewModel() {
    // 채팅 정보 저장
    private val _chatStates = MutableStateFlow<List<ChatState>>(emptyList())
    val chatStates: StateFlow<List<ChatState>> = _chatStates.asStateFlow()


    var curMessage by mutableStateOf("")

    fun updateMessage(
        roomId: Long,
        senderId: Long,
        senderName: String,
        contentType: String
    ) {
        val chatState = ChatState(roomId, senderId, senderName, contentType, curMessage)
        updateChatState(chatState)
        curMessage = ""
    }

    fun updateChatState(chatState: ChatState) {
        val currentChatState = chatStates.value.toMutableList() // 현재 상태를 가져옵니다.
        currentChatState.add(chatState)
        _chatStates.value = currentChatState // 수정된 목록을 다시 StateFlow에 할당합니다.
    }
}
