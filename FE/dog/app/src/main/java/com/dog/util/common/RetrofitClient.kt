package com.dog.util.common

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object RetrofitClient {
    const val baseUrl = "http://k9c205.p.ssafy.io:8000/api/"

    private val interceptorClient = OkHttpClient().newBuilder().addInterceptor(RequestInterceptor())
        .addInterceptor(ResponseInterceptor()).build()

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).client(interceptorClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }

    class RequestInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()
            var auth = "1" // get from localStorage

            builder.addHeader("Authorization", auth)

            return chain.proceed(builder.build())
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
