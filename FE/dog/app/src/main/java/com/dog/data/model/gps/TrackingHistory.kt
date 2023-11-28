package com.dog.data.model.gps

import com.dog.data.model.common.ResponseBodyResult

data class TrackingHistoryResponse(
    val result: ResponseBodyResult,
    val body: Body,
)

data class Body(
    val contents: List<TrackingHistory>,
    val totalPages: Int,
    val totalElements: Long
)
data class TrackingHistory(
    val id: String,
    val trackingDate: String,
    val createdDate: String,
    val modifiedDate: String?,
    val gpsPoints: GpsPoints,
    val runningTime: String
)


data class GpsPoints(
    val gps_points: List<List<Double>>
)
