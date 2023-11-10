package com.dog.data.model.user

import androidx.annotation.DrawableRes

data class UserState(
    val id: Int = 0, // userId(PK)
    val name: String = "", // userNickname 표시할 닉네임
    @DrawableRes val icon: Int? = null,
//    @DrawableRes val icon: Int = R.drawable.person_icon, // 이걸 userPicture 이미지로 바꿀 수 있나
    val userPicture: String = "",
    val address: String = "",
)

data class LoginBody(
    val userLoginId: String,
    val userNickname: String,
    val userPicture: String,
    val jwt: String
)
