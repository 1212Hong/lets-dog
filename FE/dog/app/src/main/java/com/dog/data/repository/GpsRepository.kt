package com.dog.data.repository

import com.dog.data.model.gps.GpsRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GpsRepository {

    @POST("gps")
    suspend fun sendGpsTrackingData(@Body gpsRequest: GpsRequest): Response<Unit>

}