package com.dog.data.viewmodel.map

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.model.gps.GpsPoint
import com.dog.data.model.gps.GpsRequest
import com.dog.data.repository.GpsRepository
import com.dog.data.repository.UserRepository
import com.dog.util.common.DataStoreManager
import com.dog.util.common.RetrofitClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000

@HiltViewModel
class LocationTrackingViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val interceptor = RetrofitClient.RequestInterceptor(dataStoreManager)
    private val apiService: GpsRepository =
        RetrofitClient.getInstance(interceptor).create(GpsRepository::class.java)
    private val userApi: UserRepository = RetrofitClient.getInstance(interceptor).create(
        UserRepository::class.java
    )


    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation = _userLocation.asStateFlow()
    private val _pathPoints = MutableStateFlow<MutableList<LatLng>>(mutableListOf())
    val pathPoints = _pathPoints.asStateFlow()
    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()
    private val _runningTime = MutableStateFlow(0L)
    val runningTime: StateFlow<Long> = _runningTime.asStateFlow()

    private val _formattedTime = MutableStateFlow("00:00")
    val formattedTime: StateFlow<String> = _formattedTime.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(true)
    val isLoading = _isLoading.asStateFlow()

    private var locationUpdateStarted = false

    private var startTime = 0L
    private var timerJob: Job? = null

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context.applicationContext)
    val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL_IN_MILLISECONDS
    ).build()

    init {
        getCurrentLocationAndUpdateUserInfo()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(callback: (LatLng?) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                _userLocation.value = currentLatLng
                addPathPoint(currentLatLng)
                Log.i("LocationTracking", "현재 위치: ${_userLocation.value}")
                callback(currentLatLng)
            } ?: run {
                startLocationUpdates()
                callback(null)
            }
        }
    }

    private fun getCurrentLocationAndUpdateUserInfo() {
        getCurrentLocation { location ->
            location?.let { latLng ->
                viewModelScope.launch {
                    _userLocation.value = latLng
                    dataStoreManager.saveLocation(latLng.latitude, latLng.longitude)
                    updateUserLatLangProfile(latLng.latitude, latLng.longitude)
                    _isLoading.value = false
                }
            }
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.locations.forEach { location ->
                val newLatLng = LatLng(location.latitude, location.longitude)
                _userLocation.value = newLatLng
                addPathPoint(newLatLng) // 상태 업데이트

                Log.i("LocationTracking", "locationCallback 현재 위치: ${_userLocation.value}")

            }
        }
    }

    @SuppressLint("MissingPermission")
    fun updateLocationOnly() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallbackOnlyLocation,
            Looper.getMainLooper()
        ).addOnFailureListener {
            Log.e("LocationTracking", "Failed to start location updates", it)
        }
    }

    private val locationCallbackOnlyLocation = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.locations.forEach { location ->
                val newLatLng = LatLng(location.latitude, location.longitude)
                _userLocation.value = newLatLng
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
        if (!_isRunning.value) {
            resetPathPoints()
            getCurrentLocation { location ->
                location?.let { latLng ->
                    _userLocation.value = latLng
                    addPathPoint(latLng)
                    startLocationUpdates()
                } ?: run {
                    Log.e("LocationTracking", "No initial location available.")
                }
            }
            _isRunning.value = true
            startTimer()
        }
    }


    fun stopTracking() {
        if (_isRunning.value) {
            viewModelScope.launch {
                fusedLocationClient.removeLocationUpdates(locationCallback)
                Log.i("LocationTracking", "현재 위치: ${_userLocation.value} 종료")
                Log.i("LocationTracking", "지금까지 경로: ${_pathPoints.value}")
                sendGpsDataToServer()
                timerJob?.cancel()
                _isRunning.value = false
            }
        }
    }

    fun resetPathPoints() {
        _pathPoints.value.clear()
        Log.i("LocationTracking", "Path points have been reset.")
    }

    fun sendGpsDataToServer() {
        viewModelScope.launch {
            val gpsPoints = _pathPoints.value.map { GpsPoint(it.latitude, it.longitude) }
            val formattedTimeToSend = _formattedTime.value
            val gpsPointsWrapper = GpsRequest(
                formattedTimeToSend,
                mapOf("gps_points" to gpsPoints.map { listOf(it.latitude, it.longitude) })
            )
            try {
//                val apiService = RetrofitLocalClient.instance.create(GpsRepository::class.java)
                Log.i("LocationTracking", "전송 데이터 : ${gpsPointsWrapper}")
                val retrofitResponse = apiService.sendGpsTrackingData(gpsPointsWrapper)
                if (retrofitResponse.isSuccessful) {
                    Log.i("LocationTracking", "Data sent to server successfully")
                } else {
                    Log.e(
                        "LocationTracking",
                        "Failed to send data: ${retrofitResponse.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("LocationTracking", "Error sending data to server", e)
            }
        }
    }

    private fun startTimer() {
        startTime = System.currentTimeMillis()
        timerJob = viewModelScope.launch {
            while (_isRunning.value) {
                val currentTime = System.currentTimeMillis() - startTime
                _runningTime.value = currentTime
                _formattedTime.value = formatTime(currentTime)
                delay(1000)
            }
        }
    }

    private fun formatTime(millis: Long): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }


    suspend fun updateUserLatLangProfile(lat: Double, long: Double) {

        try {
            val response =
                userApi.updateUserLatLong(lat, long)
            if (response.isSuccessful && response.body() != null) {
                val getUserBody = response.body()?.body
                Log.d("초기화", "위도경도 갱신 유저 정보 : $getUserBody")
            } else {
                // 서버에서 올바르지 않은 응답을 반환한 경우
                Log.e("getuser", response.errorBody().toString())
            }
        } catch (e: Exception) {
            // 네트워크 오류 처리
            Log.d("api_err", e.message.toString())
        }

    }


}
