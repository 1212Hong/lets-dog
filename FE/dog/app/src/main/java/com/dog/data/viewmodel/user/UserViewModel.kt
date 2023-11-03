package com.dog.data.viewmodel.user

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.model.user.SignInRequest
import com.dog.data.repository.UserRepository
import com.dog.util.common.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    // 유저 정보 저장
    private val _userState = MutableStateFlow({})
//    val userState: StateFlow<UserState> = _userState.asStateFlow()

    // Retrofit 인터페이스를 사용하려면 여기서 인스턴스를 생성합니다.
    private val userApi: UserRepository =
        RetrofitClient.getInstance().create(UserRepository::class.java)

    fun login(id: MutableState<String>, pw: MutableState<String>) {
        viewModelScope.launch {
            try {
                val response = userApi.login(SignInRequest(id, pw)).execute()
                if (response.isSuccessful) {
                    // 성공적으로 응답을 받았을 때의 처리
                    val signInResponse = response.body()
                    if (signInResponse != null) {
                        // 로그인이 성공한 경우
                        // 여기에서 처리
                        println(response)
                    } else {
                        // 서버에서 올바르지 않은 응답을 반환한 경우
                        println(response)
                    }
                } else {
                    // 서버 오류 처리
                }
            } catch (e: Exception) {
                // 네트워크 오류 처리
            }
        }
//
//        userApi.login(SignInRequest(id, pw)).enqueue(object : Callback<SignInResponse> {
//            override fun onResponse(
//                call: Call<SignInResponse>,
//                response: Response<SignInResponse>
//            ) {
//                if (response.isSuccessful) {
//                    // 성공적으로 응답을 받았을 때의 처리
//                    println(response)
//                } else {
//                    // 서버 오류 처리
//                    println(response)
//                }
//            }
//
//            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
//                // 네트워크 오류 처리
//            }
//        })
    }

}
