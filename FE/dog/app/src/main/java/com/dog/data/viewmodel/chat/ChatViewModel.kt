package com.dog.data.viewmodel.chat

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.local.chatList
import com.dog.data.model.chat.ChatState
import com.dog.data.model.chat.ChatroomInfo
import com.dog.data.model.common.Response
import com.dog.data.model.common.ResponseBodyResult
import com.dog.data.repository.ChatRepository
import com.dog.util.common.DataStoreManager
import com.dog.util.common.RetrofitClient
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val interceptor = RetrofitClient.RequestInterceptor(dataStoreManager)
    private val chatApi: ChatRepository = RetrofitClient.getInstance(interceptor).create(
        ChatRepository::class.java
    )

    // 채팅 정보 저장
    private val _chatState = MutableStateFlow(chatList)
    val chatState: StateFlow<List<ChatState>> = _chatState.asStateFlow()
    private val _chatListState = mutableStateListOf<ChatroomInfo>()
    val chatListState: List<ChatroomInfo> get() = _chatListState
    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> get() = _loading
    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage.asStateFlow()

    // Retrofit 인터페이스를 사용하려면 여기서 인스턴스를 생성합니다.

    var curMessage by mutableStateOf("")


    fun sendMessage(
        roomId: Int,
        senderId: Long,
        senderName: String,
    ) {
        // 새 메시지 전송시 채팅방화면에 메시지 로그 추가
//        val chatState = ChatState(roomId, senderId, senderName, contentType, curMessage)
//        val newId = chatState.value.size + 1;
//        val newChat = ChatState(newId, curMessage, senderName, "현재시간", false)
//        updateChatState(newChat)
        curMessage = ""
    }

    fun updateChatState(chat: ChatState) {
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
                    Log.d("chatlist", res.body()?.body.toString())
                    res.body()?.body?.let { chatroom ->
                        _chatListState.clear()
                        _chatListState.addAll(chatroom)
                        _loading.value = true
                        Log.d("chatroom", chatroom.toString())
                    }
                } else {
                    // 서버에서 올바르지 않은 응답을 반환한 경우
                    _loading.value = false
                    val errorBody = res.errorBody()?.string()
                    val gson = Gson()
                    val typeToken = object : TypeToken<Response<ResponseBodyResult>>() {}.type
                    try {
                        val errorResponse: Response<ResponseBodyResult> =
                            gson.fromJson(errorBody, typeToken)
                        Log.e("chatListRequest", "${errorResponse.result.message}")
                        _toastMessage.value = errorResponse.result.description
                    } catch (e: JsonSyntaxException) {
                        Log.e("chatListRequest", "JSON 파싱 에러", e)
                    }
                }
            } catch (e: Exception) {
                Log.e("APIError in ChatViewModel", "API 호출 중 오류 발생: ${e.message}")
                _toastMessage.value = "채팅목록을 불러오는 중 오류가 발생했습니다."
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
                val chatHistory = res.body()?.body
                Log.d("test", responseBody?.body.toString())
                if (chatHistory != null) {
                    _chatState.value = chatHistory
                }
            } else {
                // 서버에서 올바르지 않은 응답을 반환한 경우
                val errorBody = res.errorBody()?.string()
                val gson = Gson()
                val typeToken = object : TypeToken<Response<ResponseBodyResult>>() {}.type
                try {
                    val errorResponse: Response<ResponseBodyResult> =
                        gson.fromJson(errorBody, typeToken)
                    Log.e("chatHistoryRequest", "${errorResponse.result.message}")
                    _toastMessage.value = errorResponse.result.description
                } catch (e: JsonSyntaxException) {
                    Log.e("chatHistoryRequest", "JSON 파싱 에러", e)
                }
            }
        } catch (e: Exception) {
            // API 호출 중에 예외가 발생한 경우의 처리
            Log.e("APIError in ChatViewModel", "API 호출 중 오류 발생: ${e.message}")
            _toastMessage.value = "채팅기록을 불러오는 중 오류가 발생했습니다."
        }
    }

}
