package com.dog.data.model.user

data class signUpRequest(
    val user_login_id: String,
    val user_phone: String,
    val user_pw1: String,
    val user_pw2: String,
    val user_nickname: String,
    val user_terms_agreed: Boolean
)

data class signInRequest(
    val user_login_id: String,
    val user_pw: String,
)
