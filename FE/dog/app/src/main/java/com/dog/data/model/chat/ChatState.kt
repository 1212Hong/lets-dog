package com.dog.data.model.chat

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatState(
    val roomId: Long = 0, // 방 pk
    val senderId: Long = 0, // 보낸사람 pk
    val senderName: String = "",
    val contentType: String = "", // 글 or 사진 구분용
    val content: String = "",
    val sendTime: String,
    val readList: IntArray
) : Parcelable
