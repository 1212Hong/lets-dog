package com.dog.data.viewmodel.user

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.model.user.SignInRequest
import com.dog.data.repository.UserRepository
import com.dog.di.UserDataStore
import com.dog.util.common.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class UserViewModel : ViewModel() {
    private val token by lazy { stringPreferencesKey("token") }
//    private val passwordKey by lazy { stringPreferencesKey("password") }

    @Inject
    @UserDataStore
    lateinit var dataStore: DataStore<Preferences>

    // 유저 정보 저장
    private
    val _userState = mutableStateOf(UserState())
    val userState: State<UserState> get() = _userState
    private val _jwtToken = mutableStateOf<String?>(null)
    val jwtToken: State<String?> get() = _jwtToken
    private val _isLogin = mutableStateOf(false)
    val isLogin: State<Boolean> get() = _isLogin


    // Retrofit 인터페이스를 사용하려면 여기서 인스턴스를 생성합니다.
    private val userApi: UserRepository =
        RetrofitClient.getInstance().create(UserRepository::class.java)

    var test: String
        get() {
            return runBlocking(Dispatchers.IO) {
                dataStore.data.map { preferences ->
                    preferences[token] ?: ""
                }.first()
            }
        }
        set(value) {
            viewModelScope.launch {
                dataStore.edit { preferences ->
                    preferences[token] = value
                }
            }
        }

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

                    // 토큰 저장
                    _jwtToken.value = token
                    _isLogin.value = true

                    if (signInResponse != null) {
                        // 로그인이 성공한 경우
                        // 여기에서 처리
                        Log.d("login", signInResponse.toString())
                    } else {
                        // 서버에서 올바르지 않은 응답을 반환한 경우
                        Log.e("login!", response.errorBody().toString())
                        _isLogin.value = false
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
