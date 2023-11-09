package com.dog.data.viewmodel

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.model.common.Response
import com.dog.data.model.common.ResponseBodyResult
import com.dog.data.model.matching.MatchingUserResponse
import com.dog.data.repository.FriendRepository
import com.dog.data.repository.MatchingRepository
import com.dog.util.common.RetrofitClient
import com.dog.util.common.RetrofitLocalClient
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class MatchingViewModel : ViewModel() {

    private val _users = mutableStateListOf<MatchingUserResponse>()
    val users: List<MatchingUserResponse> get() = _users
    private val _selectedUserId = mutableStateOf<String?>(null)
    val selectedUserId: State<String?> get() = _selectedUserId
    private val _isDataLoaded = mutableStateOf(false)
    val isDataLoaded: State<Boolean> get() = _isDataLoaded

    private val _toastMessage = mutableStateOf<String?>(null)
    val toastMessage: State<String?> = _toastMessage


    init {
        loadUsersFromApi()
    }

    private fun loadUsersFromApi() {
        viewModelScope.launch {
            try {
                val apiService = RetrofitClient.getInstance().create(MatchingRepository::class.java)
                val response = apiService.getMatchingApiResponse()

                if (response.isSuccessful) {
                    response.body()?.body?.let { usersFromApi ->
                        _users.clear()
                        _users.addAll(usersFromApi)
                        _isDataLoaded.value = true
                    }
                } else {
                    Log.e("MatchingViewModel", "Error: ${response.errorBody()?.string()}")
                    _isDataLoaded.value = false
                }
            } catch (e: Exception) {
                Log.e("MatchingViewModel", "Exception", e)

            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    var pagerState: PagerState? = null

    @OptIn(ExperimentalFoundationApi::class)
    fun selectUser(userId: String) {
        _selectedUserId.value = userId
        val userIndex = _users.indexOfFirst { it.userLoginId == userId }
        if (userIndex != -1) {
            pagerState?.let { pager ->
                viewModelScope.launch {
                    pager.scrollToPage(userIndex)
                }
            }
        }
    }

    fun senFriendRequest(receiverNickname: String) {
        viewModelScope.launch {
            try {
                val apiService = RetrofitLocalClient.instance.create(FriendRepository::class.java)
                val retrofitResponse = apiService.sendFriendRequest(receiverNickname)
                if (retrofitResponse.isSuccessful) {
                    // 처리 성공 시
                    val responseBody = retrofitResponse.body()
                    _toastMessage.value = "친구 신청이 성공적으로 전송되었습니다."
                } else {
                    // 처리 실패 시
                    val errorBody = retrofitResponse.errorBody()?.string()
                    val gson = Gson()
                    val typeToken = object : TypeToken<Response<ResponseBodyResult>>() {}.type
                    try {
                        val errorResponse: Response<ResponseBodyResult> = gson.fromJson(errorBody, typeToken)
                        Log.e("senFriendRequest", "${errorResponse.result.message}")
                        _toastMessage.value = errorResponse.result.description
                    } catch (e: JsonSyntaxException) {
                        Log.e("senFriendRequest", "JSON 파싱 에러", e)
                    }
                }
            } catch (e: Exception) {
                Log.e("senFriendRequest", "네트워크 요청 에러", e)
                _toastMessage.value = "친구 신청 중 오류가 발생했습니다."
            }
        }
    }


    fun clearToastMessage() {
        _toastMessage.value = null
    }

}