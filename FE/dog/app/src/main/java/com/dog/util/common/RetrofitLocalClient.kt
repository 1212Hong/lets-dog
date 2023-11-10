package com.dog.util.common

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitLocalClient {
    private const val BASE_URL = "http://172.28.80.1:8000/api/"

    // TEST용
    private const val JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMDEiLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNzMwODU1NjU3fQ.A-LpWLT_ZjyAjB1_kFwk25SoiR1vCIIA_ikg6_600RA"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $JWT_TOKEN")
                .build()
            chain.proceed(newRequest)
        }
        .build()

    val instance: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}