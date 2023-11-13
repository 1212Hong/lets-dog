package com.dog.data.viewmodel.mail

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.model.email.EmailRequest
import com.dog.data.model.email.EmailValidationRequest
import com.dog.data.repository.MailRepository
import com.dog.util.common.DataStoreManager
import com.dog.util.common.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MailViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val interceptor = RetrofitClient.RequestInterceptor(dataStoreManager)
    private val mailApi: MailRepository = RetrofitClient.getInstance(interceptor).create(
        MailRepository::class.java
    )
    private val _message = mutableStateOf<String?>(null)
    val message: State<String?> = _message

    suspend fun sendMailCode(email: String) {
        viewModelScope.launch {
            try {
                val response = mailApi.sendEmailCode(EmailRequest(email))

                if (response.isSuccessful) {
                    response.body()?.body?.let { body ->
                        if (body.email === email) _message.value = "메일 전송이 완료되었습니다."
                    }
                } else {
                    Log.e("MailViewModel", "Error: ${response.errorBody()?.string()}")
                    _message.value = "에러 뜸요"
                }
            } catch (e: Exception) {
                Log.e("MailViewModel", "Exception", e)

            }
        }
    }

    suspend fun checkCode(email: String, token: String) {
        viewModelScope.launch {
            try {
                val response = mailApi.emailValidation(EmailValidationRequest(email, token))

                if (response.isSuccessful) {
                    response.body()?.body?.let { res ->
                        if (res.succeed) _message.value = "인증에 성공했습니다.."
                        else _message.value = "인증에 실패했습니다.."
                    }
                } else {
                    Log.e("MailViewModel", "Error: ${response.errorBody()?.string()}")
                    _message.value = "에러 뜸요"
                }
            } catch (e: Exception) {
                Log.e("MailViewModel", "Exception", e)

            }
        }
    }

    fun initMessage() {
        _message.value = null
    }
}
