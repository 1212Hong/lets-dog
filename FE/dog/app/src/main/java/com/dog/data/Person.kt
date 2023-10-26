package com.dog.data

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

val personList = listOf(
    Person(
        1,
        "Pranav",
        R.drawable.ic_launcher
    ),
    Person(
        2,
        "Ayesha",
        R.drawable.ic_launcher
    ),
    Person(
        3,
        "Roshini",
        R.drawable.ic_launcher
    ),
    Person(
        4,
        "Kaushik",
        R.drawable.ic_launcher
    ),
    Person(
        5,
        "Dad",
        R.drawable.ic_launcher
    ),
    Person(
        6,
        "Pranav",
        R.drawable.ic_launcher
    ),
    Person(
        7,
        "Ayesha",
        R.drawable.ic_launcher
    ),
    Person(
        8,
        "Roshini",
        R.drawable.ic_launcher
    ),
    Person(
        9,
        "Kaushik",
        R.drawable.ic_launcher
    ),
    Person(
        10,
        "Dad",
        R.drawable.ic_launcher
    ),
)
