package com.dog.data.viewmodel.map

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.locations.let { locations ->
                for (location in locations) {
                    val newLatLng = LatLng(location.latitude, location.longitude)
                    _userLocation.value = newLatLng
                    _pathPoints.value.add(newLatLng)
                    Log.d("LocationTracking", "New location added: $newLatLng")
                }
                Log.d("LocationTracking", "Current path points: ${_pathPoints.value}")
            }
        }
    }

    init {
        getCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            // 위치 정보가 성공적으로 가져와졌을 때
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                _userLocation.value = currentLatLng
                _pathPoints.value.add(currentLatLng)
                Log.i("LocationTracking", "현재 위치: ${_userLocation.value}") // 로그를 여기로 옮깁니다.
            } ?: run {
                startLocationUpdates()
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

    fun startTracking() {
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

        }
    }

}