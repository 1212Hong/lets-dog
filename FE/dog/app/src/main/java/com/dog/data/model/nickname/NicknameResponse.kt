package com.dog.data.model.nickname

import com.dog.data.model.common.ResponseBodyResult

data class CheckDupNicknameResponse(
    val result: ResponseBodyResult,
    val body: DupInfo
)

data class DupInfo(
    val isDuplicated: Boolean
)