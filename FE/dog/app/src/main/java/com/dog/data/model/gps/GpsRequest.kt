package com.dog.data.model.gps

import com.dog.data.model.common.ResponseBodyResult
import com.dog.data.model.matching.MatchingUserResponse

data class GpsRequest(
    val gpsPoints: Map<String, List<List<Double>>>
)

data class GpsPoint(
    val latitude: Double,
    val longitude: Double
)

