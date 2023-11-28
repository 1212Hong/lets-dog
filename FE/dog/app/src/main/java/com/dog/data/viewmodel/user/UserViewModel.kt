package com.dog.data.viewmodel.user

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.MainActivity
import com.dog.data.model.common.Response
import com.dog.data.model.common.ResponseBodyResult
import com.dog.data.model.user.SignInRequest
import com.dog.data.model.user.SignUpRequest
import com.dog.data.model.user.UserBody
import com.dog.data.repository.UserRepository
import com.dog.util.common.DataStoreManager
import com.dog.util.common.RetrofitClient
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    @ApplicationContext private val context: Context
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
    private val _isLogin = MutableStateFlow<Boolean>(false)
    val isLogin = _isLogin.asStateFlow()
    private val _message = mutableStateOf<String?>(null)
    val message: State<String?> = _message
    private val _userInfo = MutableStateFlow<UserBody?>(null)
    val userInfo = _userInfo.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(true)
    val isLoading = _isLoading.asStateFlow()

    fun clearMessage() {
        _message.value = null
    }

    init {
        viewModelScope.launch {
            _jwtToken.value = dataStoreManager.getToken()
            _isLogin.value = !_jwtToken.value.isNullOrEmpty()
            if (_isLogin.value)
                _userState.value = UserState(dataStoreManager.getUserNickname(), "")
            _isLoading.value = false
        }
    }

    suspend fun login(id: String, pw: String) {
        viewModelScope.launch {
            try {
                val fcmToken = dataStoreManager.getFCM()
                val response = userApi.login(SignInRequest(id, pw, fcmToken))
                Log.d("api", response.toString())
                if (response.isSuccessful) {
                    // 성공적으로 응답을 받았을 때의 처리
                    _message.value = response.body()!!.result?.message.toString()
                    val loginBody = response.body()?.body
                    if (loginBody != null) {
                        val token = loginBody?.jwt
                        val userNickname = loginBody?.userNickname
                        val userLoginId = loginBody?.userLoginId

                        _jwtToken.value = token
                        dataStoreManager.saveToken(token)
                        dataStoreManager.saveUserDetails(userNickname, userLoginId)
                        _userState.value?.name = loginBody?.userNickname.toString()
                        _userState.value = UserState(loginBody.userNickname, loginBody.userPicture)
                        _isLogin.value = true
                        Log.d("login", loginBody.toString())
                    }
                } else {
                    // 서버에서 올바르지 않은 응답을 반환한 경우
                    val errorBody = response.errorBody()?.string()
                    val gson = Gson()
                    val typeToken = object : TypeToken<Response<ResponseBodyResult>>() {}.type
                    try {
                        val errorResponse: Response<ResponseBodyResult> =
                            gson.fromJson(errorBody, typeToken)
                        Log.e("loginRequest", "${errorResponse.result.message}")
                        _message.value = errorResponse.result.message
                        Log.d("loginRequest", _message.value.toString())
                    } catch (e: JsonSyntaxException) {
                        Log.e("loginRequest", "JSON 파싱 에러", e)
                    }
                }
            } catch (e: Exception) {
                Log.e("loginRequest", "네트워크 요청 에러", e)
                _message.value = "로그인 중 오류가 발생했습니다."
            }
        }
    }

    suspend fun signup(
        id: String,
        pw: String,
        checkPw: String,
        nickName: String,
        agreement: Boolean
    ) {
        viewModelScope.launch {
            try {
                val response =
                    userApi.signup(SignUpRequest(id, pw, checkPw, nickName, agreement))
                Log.d("api", response.toString())
                if (response.isSuccessful) {
                    val signupBody = response.body()?.body
                    val signupResult = response.body()?.result

                    if(_message.value == null) _message.value = signupResult?.message
                    else _message.value = signupBody.toString() + " 계정으로 로그인에 성공햇습니다."
                    Log.d("signup", signupBody.toString())
                } else {
                    // 서버에서 올바르지 않은 응답을 반환한 경우
                    val errorBody = response.errorBody()?.string()
                    val gson = Gson()
                    val typeToken = object : TypeToken<Response<ResponseBodyResult>>() {}.type
                    try {
                        val errorResponse: Response<ResponseBodyResult> =
                            gson.fromJson(errorBody, typeToken)
                        Log.e("signupRequest", "${errorResponse.result.message}")
                        _message.value = errorResponse.result.description
                    } catch (e: JsonSyntaxException) {
                        Log.e("signupRequest", "JSON 파싱 에러", e)
                    }
                }
            } catch (e: Exception) {
                Log.e("signupRequest", "네트워크 요청 에러", e)
                _message.value = "회원가입 중 오류가 발생했습니다."
            }
        }
    }

    suspend fun getUser() {
        viewModelScope.launch {
            try {
                val response =
                    userApi.getUserInfo(_userState.value!!.name)
                Log.i("getuser", "저장된 유저 이름 ${_userState.value!!.name}")
                if (response.isSuccessful && response.body() != null) {
                    val getUserBody = response.body()?.body
                    _userInfo.value = getUserBody
                    Log.d("getuser", "리턴 유저 정보 : $getUserBody")
                } else {
                    // 서버에서 올바르지 않은 응답을 반환한 경우
                    Log.e("getuser", response.errorBody().toString())

                    _message.value = response.errorBody().toString()
                }

            } catch (e: Exception) {
                // 네트워크 오류 처리
                Log.d("api_err", e.message.toString())
            }
        }
    }

    fun logout() {
        _isLogin.value = false
        _isLoading.value = true
        viewModelScope.launch {
            dataStoreManager.onLogout()
        }
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }
}
