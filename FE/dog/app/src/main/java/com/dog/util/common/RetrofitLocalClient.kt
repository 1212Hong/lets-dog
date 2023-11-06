package com.dog.util.common

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitLocalClient {
    private const val BASE_URL = "http://172.28.80.1:8000/api/"

    val instance: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}