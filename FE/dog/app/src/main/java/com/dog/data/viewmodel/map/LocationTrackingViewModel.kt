package com.dog.data.viewmodel.map

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.model.gps.GpsPoint
import com.dog.data.model.gps.GpsRequest
import com.dog.data.repository.FriendRepository
import com.dog.data.repository.GpsRepository
import com.dog.util.common.RetrofitClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000

class LocationTrackingViewModel(
    private val context: Context
) : ViewModel() {
    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation = _userLocation.asStateFlow()
    private val _pathPoints = MutableStateFlow<MutableList<LatLng>>(mutableListOf())
    val pathPoints = _pathPoints.asStateFlow()

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL_IN_MILLISECONDS
    ).build()


    init {
        getCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                _userLocation.value = currentLatLng
                addPathPoint(currentLatLng) // 상태 업데이트
                Log.i("LocationTracking", "현재 위치: ${_userLocation.value}")
            } ?: run {
                startLocationUpdates()
            }
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.locations.forEach { location ->
                val newLatLng = LatLng(location.latitude, location.longitude)
                _userLocation.value = newLatLng
                addPathPoint(newLatLng) // 상태 업데이트
            }
        }
    }


    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        ).addOnFailureListener {
            Log.e("LocationTracking", "Failed to start location updates", it)
        }
    }

    fun addPathPoint(newPoint: LatLng) {
        val currentList = _pathPoints.value.toMutableList()
        currentList.add(newPoint)
        _pathPoints.value = currentList
    }

    fun startTracking() {
        resetPathPoints()
        _userLocation.value?.let {
            startLocationUpdates()
        } ?: run {
            getCurrentLocation()
        }
    }


    fun stopTracking() {
        viewModelScope.launch {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            Log.i("LocationTracking", "현재 위치: ${_userLocation.value} 종료")
            Log.i("LocationTracking", "지금까지 경로: ${_pathPoints.value}")
            sendGpsDataToServer()
        }
    }

    fun resetPathPoints() {
        _pathPoints.value.clear()
        Log.i("LocationTracking", "Path points have been reset.")
    }

    fun sendGpsDataToServer() {
        viewModelScope.launch {
            val gpsPoints  = _pathPoints.value.map { GpsPoint(it.latitude, it.longitude) }
            Log.i("LocationTracking", "전송 gpsPoints : ${gpsPoints}")

            val gpsPointsWrapper = GpsRequest(mapOf("gps_points" to gpsPoints.map { listOf(it.latitude, it.longitude) }))
            try {
                val apiService = RetrofitClient.getInstance().create(GpsRepository::class.java)
                Log.i("LocationTracking", "전송 데이터 : ${gpsPointsWrapper}")
                val retrofitResponse = apiService.sendGpsTrackingData(gpsPointsWrapper)
                if (retrofitResponse.isSuccessful) {
                    Log.i("LocationTracking", "Data sent to server successfully")
                } else {
                    Log.e("LocationTracking", "Failed to send data: ${retrofitResponse.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("LocationTracking", "Error sending data to server", e)
            }
        }
    }

}