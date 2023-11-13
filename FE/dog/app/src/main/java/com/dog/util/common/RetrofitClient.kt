package com.dog.util.common

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object RetrofitClient {
    const val baseUrl = "http://k9c205.p.ssafy.io:8000/api/"
    const val JWT = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTczMTI0NzA1M30.KWmcTW4FJRjdEawfSD-_FfEnOshyyTAsXxXfdBNG6S4"


    fun getInstance(interceptor: RequestInterceptor): Retrofit {
        val interceptorClient = OkHttpClient().newBuilder().addInterceptor(interceptor)
            .addInterceptor(ResponseInterceptor()).build()

        return Retrofit.Builder().baseUrl(baseUrl).client(interceptorClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    class RequestInterceptor(private val dataStoreManager: DataStoreManager) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val authToken = runBlocking { dataStoreManager.getToken() }
            val requestBuilder = chain.request().newBuilder()

            if (authToken.isNotEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $authToken")
            }

            return chain.proceed(requestBuilder.build())
        }
    }

    class ResponseInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response = chain.proceed(request)

            when (response.code) {
                400 -> {
                    // todo Control Error
                }

                401 -> {
                    // todo Control Error
                }

                402 -> {
                    // todo Control Error
                }
            }
            return response
        }
    }

}