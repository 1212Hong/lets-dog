package com.dog.data.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.dog.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Person(
    val id: Int = 0,
    val name: String = "",
    @DrawableRes val icon: Int = R.drawable.ic_launcher
) : Parcelable

