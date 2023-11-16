package com.dog.util.common

import java.text.SimpleDateFormat
import java.util.Locale

fun formatTrackingDate(trackingDateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy년 MM월 dd일 a h시 mm분", Locale.getDefault())

    return try {
        val date = inputFormat.parse(trackingDateString)
        date?.let { outputFormat.format(it) } ?: "날짜 형식 오류"
    } catch (e: Exception) {
        "날짜 파싱 실패"
    }
}

fun formatDate(inputDate: String, inputPattern: String): String {
    val inputFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())

    try {
        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return inputDate // 변환이 실패-> 입력된 문자열을 그대로 반환
}