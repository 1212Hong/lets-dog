package com.dog.util.common

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object UploadRetrofitClient {
    private const val baseUrl = "http://k9c205.p.ssafy.io:9000/api/"

    fun getInstance(interceptor: RequestInterceptor): Retrofit {
        val interceptorClient = OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()


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
