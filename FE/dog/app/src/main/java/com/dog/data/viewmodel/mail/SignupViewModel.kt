package com.dog.data.viewmodel.mail

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.model.email.EmailRequest
import com.dog.data.model.email.EmailValidationRequest
import com.dog.data.repository.SignupRepository
import com.dog.util.common.DataStoreManager
import com.dog.util.common.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val interceptor = RetrofitClient.RequestInterceptor(dataStoreManager)
    private val signupApi: SignupRepository = RetrofitClient.getInstance(interceptor).create(
        SignupRepository::class.java
    )
    private val _message = mutableStateOf<String?>(null)
    val message: State<String?> = _message

    suspend fun sendMailCode(email: String) {
        viewModelScope.launch {
            try {
                val response = signupApi.sendEmailCode(EmailRequest(email))

                if (response.isSuccessful) {
                    response.body()?.body?.let { body ->
                        _message.value = "인증 메일 전송이 완료되었습니다."
                        Log.d("mail", body.email)
                        Log.d("mail_message", _message.value.toString())
                    }
                } else {
                    Log.e("SignupViewModel", "Error: ${response.errorBody()?.string()}")
                    _message.value = "인증 메일 전송에 실패했습니다."
                }
            } catch (e: Exception) {
                Log.e("SignupViewModel", "Exception", e)

            }
        }
    }

    suspend fun checkCode(email: String, token: String) {
        viewModelScope.launch {
            try {
                val response = signupApi.emailValidation(EmailValidationRequest(email, token))

                if (response.isSuccessful) {
                    response.body()?.body?.let { res ->
                        if (res.succeed) _message.value = "인증에 성공했습니다.."
                        else _message.value = "인증에 실패했습니다.."
                    }
                } else {
                    Log.e("SignupViewModel", "Error: ${response.errorBody()?.string()}")
                    _message.value = "에러 뜸요"
                }
            } catch (e: Exception) {
                Log.e("SignupViewModel", "Exception", e)

            }
        }
    }

    suspend fun checkNickname(nickname: String) {
        viewModelScope.launch {
            try {
                val response = signupApi.checkDupNickname(nickname)

                if (response.isSuccessful) {
                    response.body()?.body?.let { res ->
                        if (res.isDuplicated) _message.value = "중복 닉네임이 존재합니다."
                        else _message.value = "사용 가능한 닉네임입니다."
                    }
                } else {
                    Log.e("SignupViewModel", "Error: ${response.errorBody()?.string()}")
                    _message.value = "에러 뜸요"
                }
            } catch (e: Exception) {
                Log.e("SignupViewModel", "Exception", e)

            }
        }
    }

    fun initMessage() {
        _message.value = null
    }
}
