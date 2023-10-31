package com.dog.data.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Person(
    val id: Int = 0, // userId(PK)
    val name: String = "", // userNickname 표시할 닉네임
    @DrawableRes val icon: Int = 0, // 이걸 userPicture 이미지로 바꿀 수 있나
    val userPicture: String = "",
    val address: String = "",
) : Parcelable

