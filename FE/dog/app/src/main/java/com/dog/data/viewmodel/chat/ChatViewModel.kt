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
import com.dog.data.model.user.FriendInfo
import com.dog.data.repository.ChatRepository
import com.dog.data.repository.FriendRepository
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
    private val FriendApi: FriendRepository = RetrofitClient.getInstance(interceptor).create(
        FriendRepository::class.java
    )

    // 채팅 정보 저장
    private val _chatState = MutableStateFlow(chatList)
    val chatState: StateFlow<List<ChatState>> = _chatState.asStateFlow()

    // 채팅방 목록
    private val _chatListState = mutableStateListOf<ChatroomInfo>()
    val chatListState: List<ChatroomInfo> get() = _chatListState

    // 로딩 여부 변수
    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> get() = _loading

    // 앱 내부 알림메시지를 위한 변수
    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage.asStateFlow()

    // 대화방을 만들때 선택하기 위한 친구 목록
    private val _friendList = mutableStateListOf<FriendInfo>()
    val friendList: List<FriendInfo> get() = _friendList

    // 읽음 처리를 위한 현재 채팅 접속자 리스트
    private val _readList = mutableStateListOf<String>()
    val readList: List<String> get() = _readList


    var curMessage by mutableStateOf("")

    suspend fun newChatroom(roomName: String, selectedFriends: List<String>) {
        viewModelScope.launch {
            val friendNicknames = selectedFriends.map { it }

            try {
                val res = chatApi.createChatroom(friendNicknames)

                if (res.isSuccessful) {
                    // Handle success, you might want to update UI or navigate to the created chatroom
                    Log.d("newChatroom", res.body().toString())
                } else {
                    // 서버에서 올바르지 않은 응답을 반환한 경우
                    _loading.value = false
                    val errorBody = res.errorBody()?.string()
                    val gson = Gson()
                    val typeToken = object : TypeToken<Response<ResponseBodyResult>>() {}.type
                    try {
                        val errorResponse: Response<ResponseBodyResult> =
                            gson.fromJson(errorBody, typeToken)
                        Log.e("newChatroom", "${errorResponse.result.message}")
                        _toastMessage.value = errorResponse.result.description
                    } catch (e: JsonSyntaxException) {
                        Log.e("newChatroom", "JSON 파싱 에러", e)
                    }
                }
            } catch (e: Exception) {
                Log.e("APIError in ChatViewModel", "API 호출 중 오류 발생: ${e.message}")
                _toastMessage.value = "친구목록을 불러오는 중 오류가 발생했습니다."
            }
        }
    }

    fun sendMessage(
        newChatLog: ChatState
    ) {
        // 새 메시지 전송시 채팅방화면에 메시지 로그 추가
        updateChatState(newChatLog)
        curMessage = ""
    }

    fun updateChatState(chat: ChatState) {
        val currentChatState = chatState.value.toMutableList() // 현재 상태를 가져옵니다.
        currentChatState.add(chat)
        _chatState.value = currentChatState // 수정된 목록을 다시 StateFlow에 할당합니다.
        Log.d("chatState", chatState.toString())
    }

    fun updateReadList(userId: String) {
        val currentReadList = _readList.toMutableList() // 현재 상태를 가져옵니다.
        currentReadList.add(userId)
        _readList.clear()
        _readList.addAll(currentReadList)
        Log.d("readList", _readList.toString())
    }

    fun leaveChatroom(roomId: Long) {
        viewModelScope.launch {
            chatApi.disconnectChatroom(roomId)
        }
    }

    fun getFriendList() {
        viewModelScope.launch {
            try {
                val res = FriendApi.getFriendListRequest()

                if (res.isSuccessful) {
                    Log.d("friendlist", res.body().toString())
                    res.body()?.body?.let { list ->
                        _friendList.clear()
                        _friendList.addAll(list)
                        _loading.value = true
                        Log.d("friendListRequest", list.toString())
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
                        Log.e("friendListRequest", "${errorResponse.result.message}")
                        _toastMessage.value = errorResponse.result.description
                    } catch (e: JsonSyntaxException) {
                        Log.e("friendListRequest", "JSON 파싱 에러", e)
                    }
                }
            } catch (e: Exception) {
                Log.e("APIError in ChatViewModel", "API 호출 중 오류 발생: ${e.message}")
                _toastMessage.value = "친구목록을 불러오는 중 오류가 발생했습니다."
            }

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

    suspend fun getChatHistory(roomId: Long) {
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
