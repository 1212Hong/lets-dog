package com.dog.data.model.user

data class SignUpRequest(
    val userLoginId: String,
    val userPw1: String,
    val userPw2: String,
    val userNickname: String,
    val userTermsAgreed: Boolean
)

data class SignInRequest(
    val userLoginId: String,
    val userPw: String,
)
