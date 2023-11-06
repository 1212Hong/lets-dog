package com.dog.data.model.user

import androidx.compose.runtime.MutableState

data class SignUpRequest(
    val user_login_id: String,
    val user_phone: String,
    val user_pw1: String,
    val user_pw2: String,
    val user_nickname: String,
    val user_terms_agreed: Boolean
)

data class SignInRequest(
    val user_login_id: MutableState<String>,
    val user_pw: MutableState<String>,
)
