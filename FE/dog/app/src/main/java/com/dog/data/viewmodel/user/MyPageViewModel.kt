package com.dog.data.viewmodel.user

import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.dog.data.model.common.Response
import com.dog.data.model.common.ResponseBodyResult
import com.dog.data.model.dog.DogInfo
import com.dog.data.model.matching.Dog
import com.dog.data.model.matching.MatchingUserResponse
import com.dog.data.model.user.SignInRequest
import com.dog.data.model.user.SignUpRequest
import com.dog.data.model.user.UserBody
import com.dog.data.model.user.UserUpdateRequest
import com.dog.data.repository.DogRepository
import com.dog.data.repository.FriendRepository
import com.dog.data.repository.UserRepository
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
class MyPageViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val interceptor = RetrofitClient.RequestInterceptor(dataStoreManager)
    private val userApi: UserRepository = RetrofitClient.getInstance(interceptor).create(
        UserRepository::class.java
    )
    private val friendApi: FriendRepository = RetrofitClient.getInstance(interceptor).create(
        FriendRepository::class.java
    )
    private val dogApi: DogRepository = RetrofitClient.getInstance(interceptor).create(
        DogRepository::class.java
    )

    var loginUserNickname = mutableStateOf<String?>(null)
    var currentUserNickname = mutableStateOf<String?>(null)
        private set

    private val _userInfo = MutableStateFlow<UserBody?>(null)
    val userInfo = _userInfo.asStateFlow()

    private val _friendRequest = MutableStateFlow<List<UserBody>>(listOf())
    val friendRequest: StateFlow<List<UserBody>> = _friendRequest.asStateFlow()

    private val _dogs = MutableStateFlow<List<DogInfo>>(listOf())
    val dogs: StateFlow<List<DogInfo>> = _dogs.asStateFlow()

    // 유저 정보 저장
    init {
        getUser()
        getDog()
    }


    fun getUser(userNickname: String? = null) {
        viewModelScope.launch {
            val loginUser = dataStoreManager.getUserNickname()
            val nickname = userNickname ?: loginUser
            loginUserNickname.value = loginUser
            currentUserNickname.value = userNickname?:loginUser
            try {
                val response =
                    userApi.getUserInfo(nickname)
                Log.i("getuser", "저장된 유저 이름 ${nickname}")
                if (response.isSuccessful && response.body() != null) {
                    val getUserBody = response.body()?.body

                    _userInfo.value = getUserBody
                    Log.d("getuser", "리턴 유저 정보 : $getUserBody")
                } else {
                    // 서버에서 올바르지 않은 응답을 반환한 경우
                    Log.e("getuser", response.errorBody().toString())

                }

            } catch (e: Exception) {
                // 네트워크 오류 처리
                Log.d("api_err", e.message.toString())
            }
        }
    }

    fun getDog(userNickname: String? = null) {
        viewModelScope.launch {
            val loginUser = dataStoreManager.getUserNickname()
            val nickname = userNickname ?: loginUser
            try {
                val response =
                    dogApi.getDogs(nickname)
                Log.i("getDog", "저장된 유저 이름 ${nickname}")
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.body?.let { dogs ->
                        _dogs.value = dogs
                    }
                } else {
                    // 서버에서 올바르지 않은 응답을 반환한 경우
                    Log.e("getDog", response.errorBody().toString())
                }
            } catch (e: Exception) {
                // 네트워크 오류 처리
                Log.d("getDog", e.message.toString())
            }
        }
    }

    fun getUserArticle(userNickname: String? = null) {
        viewModelScope.launch {
            val loginUser = dataStoreManager.getUserNickname()
            val nickname = userNickname ?: loginUser
            try {
                val response =
                    dogApi.getDogs(nickname)
                Log.i("getDog", "저장된 유저 이름 ${nickname}")
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.body?.let { dogs ->
                        _dogs.value = dogs
                    }
                } else {
                    // 서버에서 올바르지 않은 응답을 반환한 경우
                    Log.e("getDog", response.errorBody().toString())
                }
            } catch (e: Exception) {
                // 네트워크 오류 처리
                Log.d("getDog", e.message.toString())
            }
        }
    }

    fun updateUserNickname(nickName: String) {
        currentUserNickname.value = nickName
        getUser(nickName)
        getDog(nickName)
    }

    fun getFriendRequests() {
        viewModelScope.launch {
            try {
                val response =
                    friendApi.getFriendRequest()
                if (response.isSuccessful && response.body() != null) {
                    val getUserBody = response.body()?.body
                    response.body()?.body?.let { usersFromApi ->
                        _friendRequest.value = usersFromApi
                    }
                    Log.d("getFriendRequests", "리턴 친구요청 목록 정보 : $getUserBody")
                } else {
                    // 서버에서 올바르지 않은 응답을 반환한 경우
                    Log.e("getFriendRequests", response.errorBody().toString())

                }

            } catch (e: Exception) {
                // 네트워크 오류 처리
                Log.d("api_err", e.message.toString())
            }
        }
    }

    fun acceptFriendRequest(requesterNickname: String) {
        viewModelScope.launch {
            try {
                val response =
                    friendApi.acceptFriendRequest(requesterNickname)
                if (response.isSuccessful && response.body() != null) {
                    val getUserBody = response.body()?.body
                    getFriendRequests()
                    Log.d("FriendRequest", "친구요청 승인 : $getUserBody")
                } else {
                    // 서버에서 올바르지 않은 응답을 반환한 경우
                    Log.e("FriendRequest", response.errorBody().toString())
                }
            } catch (e: Exception) {
                // 네트워크 오류 처리
                Log.d("FriendRequest", e.message.toString())
            }
        }
    }

    fun declineFriendRequest(requesterNickname: String) {
        viewModelScope.launch {
            try {
                val response =
                    friendApi.declineFriendRequest(requesterNickname)
                if (response.isSuccessful && response.body() != null) {
                    val getUserBody = response.body()?.body
                    getFriendRequests()
                    Log.d("FriendRequest", "친구요청 목록 : $getUserBody")
                } else {
                    // 서버에서 올바르지 않은 응답을 반환한 경우
                    Log.e("FriendRequest", response.errorBody().toString())
                }
            } catch (e: Exception) {
                // 네트워크 오류 처리
                Log.d("FriendRequest", e.message.toString())
            }
        }
    }

    fun updateUserProfile(updateRequest: UserUpdateRequest) {
        viewModelScope.launch {
            try {
                val response =
                    userApi.updateUserProfile(updateRequest)
                if (response.isSuccessful && response.body() != null) {
                    val getUserBody = response.body()?.body
                    _userInfo.value = getUserBody
                    Log.d("getuser", "리턴 유저 정보 : $getUserBody")
                } else {
                    // 서버에서 올바르지 않은 응답을 반환한 경우
                    Log.e("getuser", response.errorBody().toString())
                }
            } catch (e: Exception) {
                // 네트워크 오류 처리
                Log.d("api_err", e.message.toString())
            }
        }
    }
}
