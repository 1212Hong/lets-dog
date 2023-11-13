package com.dog.data.repository

import com.dog.data.model.gps.GpsRequest
import com.dog.data.model.gps.TrackingHistory
import com.dog.data.model.gps.TrackingHistoryResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GpsRepository {

    @POST("gps")
    suspend fun sendGpsTrackingData(@Body gpsRequest: GpsRequest): Response<Unit>

    @GET("gps")
    suspend fun getTrackingHistory(@Query("page") page: Int, @Query("size") size: Int = 6): Response<TrackingHistoryResponse>

}