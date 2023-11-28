package com.dog.data.viewmodel.user

data class UserState(
//    val id: Int = 0, // userId(PK)
    var name: String = "", // userNickname 표시할 닉네임
//    @DrawableRes val icon: Int = 0, // 이걸 userPicture 이미지로 바꿀 수 있나
    val userPicture: String = "",
//    val address: String = "",
)
