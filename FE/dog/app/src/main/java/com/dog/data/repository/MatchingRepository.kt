package com.dog.data.repository

import com.dog.data.model.user.MatchingApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface MatchingRepository {
    @GET("matching/hyeonsug37@example.net") //TODO: 토큰을 자동으로 header에 담아 전송하는 로직으로 변경
    suspend fun getMatchingApiResponse(): Response<MatchingApiResponse>
}