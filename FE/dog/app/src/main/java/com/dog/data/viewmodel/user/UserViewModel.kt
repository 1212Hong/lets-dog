package com.dog.data.viewmodel.user

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.model.user.SignInRequest
import com.dog.data.model.user.SignUpRequest
import com.dog.data.model.user.UserBody
import com.dog.data.repository.UserRepository
import com.dog.util.common.DataStoreManager
import com.dog.util.common.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val interceptor = RetrofitClient.RequestInterceptor(dataStoreManager)
    private val userApi: UserRepository = RetrofitClient.getInstance(interceptor).create(
        UserRepository::class.java
    )

    // 유저 정보 저장
    private
    val _userState = MutableStateFlow<UserState?>(null)
    val userState = _userState.asStateFlow()
    private val _jwtToken = mutableStateOf<String?>(null)
    val jwtToken: State<String?> get() = _jwtToken
    private val _isLogin = mutableStateOf(false)
    val isLogin: State<Boolean> get() = _isLogin
    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()
    private val _userInfo = MutableStateFlow<UserBody?>(null)
    val userInfo = _userInfo.asStateFlow()

    suspend fun login(id: String, pw: String) {
        viewModelScope.launch {
            try {
                val response = userApi.login(SignInRequest(id, pw))
                Log.d("api", response.toString())
                if (response.isSuccessful && response.body() != null) {
                    // 성공적으로 응답을 받았을 때의 처리
                    val loginBody = response.body()?.body
                    val token = loginBody?.jwt
                    // 로그인이 성공한 경우
                    // 여기에서 처리
                    // 토큰 저장
                    _jwtToken.value = token
                    dataStoreManager.saveToken(token)
                    _userState.value?.name = loginBody?.userNickname.toString()
                    _isLogin.value = true
                    Log.d("login", loginBody.toString())
                    _message.value = response.body()!!.result?.message.toString()
                } else {
                    // 서버에서 올바르지 않은 응답을 반환한 경우
                    Log.e("login!", response.errorBody().toString())
                    _isLogin.value = false
                    _message.value = response.errorBody().toString()

                }
            } catch (e: Exception) {
                // 네트워크 오류 처리
            }
        }
    }

    suspend fun signup(
        id: String,
        phoneNum: String,
        pw: String,
        checkPw: String,
        nickName: String,
        agreement: Boolean
    ) {
        viewModelScope.launch {
            try {
                val response =
                    userApi.signup(SignUpRequest(id, phoneNum, pw, checkPw, nickName, agreement))
                Log.d("api", response.toString())
                if (response.isSuccessful && response.body() != null) {
                    val signupBody = response.body()?.body
                    Log.d("signup", signupBody.toString())
                    _message.value = response.body()!!.result.message
                } else {
                    // 서버에서 올바르지 않은 응답을 반환한 경우
                    Log.e("!signup", response.errorBody().toString())

                    _message.value = response.errorBody().toString()
                }

            } catch (e: Exception) {
                // 네트워크 오류 처리
            }
        }
    }

    suspend fun getUser() {
//        viewModelScope.launch {
        try {
            val response =
                userApi.getUserInfo(_userState.value!!.name)
            if (response.isSuccessful && response.body() != null) {
                val getUserBody = response.body()?.body
                _userInfo.value = getUserBody
                Log.d("userInfo_success", getUserBody.toString())
            } else {
                // 서버에서 올바르지 않은 응답을 반환한 경우
                Log.e("userInfo_fail", response.errorBody().toString())

                _message.value = response.errorBody().toString()
            }

        } catch (e: Exception) {
            // 네트워크 오류 처리
            Log.d("api_err", e.message.toString())
        }
    }
//    }

}
