package com.dog.data.viewmodel.user

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.model.user.SignInRequest
import com.dog.data.model.user.SignUpRequest
import com.dog.data.repository.MatchingRepository
import com.dog.data.repository.UserRepository
import com.dog.util.common.DataStoreManager
import com.dog.util.common.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val interceptor = RetrofitClient.RequestInterceptor(dataStoreManager)
    private val userApi: UserRepository = RetrofitClient.getInstance(interceptor).create(
        UserRepository::class.java)

    // 유저 정보 저장
    private
    val _userState = mutableStateOf(UserState())
    val userState: State<UserState> get() = _userState
    private val _jwtToken = mutableStateOf<String?>(null)
    val jwtToken: State<String?> get() = _jwtToken
    private val _isLogin = mutableStateOf(false)
    val isLogin: State<Boolean> get() = _isLogin
    private val _errMsg = mutableStateOf("")
    val errMsg: State<String> get() = _errMsg



    suspend fun login(id: String, pw: String) {
        viewModelScope.launch {
            try {
                val response = userApi.login(SignInRequest(id, pw))
                Log.d("api", response.toString())
                if (response.isSuccessful) {
                    // 성공적으로 응답을 받았을 때의 처리
                    val signInResponse = response.body()
                    val loginBody = signInResponse?.body
                    val token = loginBody?.jwt

                    if (signInResponse != null) {
                        // 로그인이 성공한 경우
                        // 여기에서 처리
                        // 토큰 저장
                        _jwtToken.value = token
                        dataStoreManager.saveToken(token)
                        _isLogin.value = true
                        Log.d("login", signInResponse.toString())
                        if (_isLogin.value) {

                        }
                    } else {
                        // 서버에서 올바르지 않은 응답을 반환한 경우
                        Log.e("login!", response.errorBody().toString())
                        _isLogin.value = false
                        _errMsg.value = response.errorBody().toString()
                    }
                } else {
                    // 서버 오류 처리
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
                if (response.isSuccessful) {
                    // 성공적으로 응답을 받았을 때의 처리
                    val SignUpRequest = response.body()
                    val signupBody = SignUpRequest?.body

                    if (SignUpRequest != null) {
                        // 로그인이 성공한 경우
                        // 여기에서 처리
                        // 토큰 저장

                        Log.d("signup", SignUpRequest.toString())
                    } else {
                        // 서버에서 올바르지 않은 응답을 반환한 경우
                        Log.e("!signup", response.errorBody().toString())

                        _errMsg.value = response.errorBody().toString()
                    }
                } else {
                    // 서버 오류 처리
                }
            } catch (e: Exception) {
                // 네트워크 오류 처리
            }
        }

    }
}
