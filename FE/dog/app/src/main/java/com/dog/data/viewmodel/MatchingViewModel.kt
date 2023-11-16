package com.dog.data.viewmodel

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.model.common.Response
import com.dog.data.model.common.ResponseBodyResult
import com.dog.data.model.matching.MatchingUserResponse
import com.dog.data.repository.FriendRepository
import com.dog.data.repository.MatchingRepository
import com.dog.util.common.DataStoreManager
import com.dog.util.common.RetrofitClient
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchingViewModel@Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel(), LifecycleEventObserver {

    private val interceptor = RetrofitClient.RequestInterceptor(dataStoreManager)
    private val MatchingApi: MatchingRepository = RetrofitClient.getInstance(interceptor).create(
        MatchingRepository::class.java)
    private val FriendApi: FriendRepository = RetrofitClient.getInstance(interceptor).create(
        FriendRepository::class.java)

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
                val response = MatchingApi.getMatchingApiResponse()

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

    fun sendFriendRequest(receiverNickname: String) {
        viewModelScope.launch {
            try {
                val retrofitResponse = FriendApi.sendFriendRequest(receiverNickname)
                if (retrofitResponse.isSuccessful) {
                    val responseBody = retrofitResponse.body()
                    _toastMessage.value = "친구 신청이 성공적으로 전송되었습니다."
                } else {
                    // 처리 실패 시
                    val errorBody = retrofitResponse.errorBody()?.string()
                    val gson = Gson()
                    val typeToken = object : TypeToken<Response<ResponseBodyResult>>() {}.type
                    try {
                        val errorResponse: Response<ResponseBodyResult> = gson.fromJson(errorBody, typeToken)
                        Log.e("sendFriendRequest", "${errorResponse.result.message}")
                        _toastMessage.value = errorResponse.result.description
                    } catch (e: JsonSyntaxException) {
                        Log.e("sendFriendRequest", "JSON 파싱 에러", e)
                    }
                }
            } catch (e: Exception) {
                Log.e("sendFriendRequest", "네트워크 요청 에러", e)
                _toastMessage.value = "친구 신청 중 오류가 발생했습니다."
            }
        }
    }


    fun clearToastMessage() {
        _toastMessage.value = null
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_RESUME) {
            loadUsersFromApi()
            viewModelScope.launch {
                Log.i("datastore 저장 좌표 : ", "${dataStoreManager.getLocationLatitude()}|${dataStoreManager.getLocationLongitude()}")
            }
        }
    }

}