package com.dog.data.viewmodel.map

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.model.gps.Body
import com.dog.data.model.gps.TrackingHistory
import com.dog.data.model.gps.TrackingHistoryResponse
import com.dog.data.repository.GpsRepository
import com.dog.util.common.DataStoreManager
import com.dog.util.common.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationTrackingHistoryViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val dataStoreManager: DataStoreManager
) : ViewModel(), LifecycleEventObserver {

    private val interceptor = RetrofitClient.RequestInterceptor(dataStoreManager)
    private val apiService: GpsRepository =
        RetrofitClient.getInstance(interceptor).create(GpsRepository::class.java)


    private val _trackingHistory = MutableStateFlow<Body?>(null)
    val trackingHistory = _trackingHistory.asStateFlow()
    var currentPage = 1
    var totalPages = 1

    fun getTrackingHistory(page: Int = currentPage) {
        viewModelScope.launch {
            try {
                val backendPage = page - 1
                val response = apiService.getTrackingHistory(backendPage)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _trackingHistory.value = it.body
                        totalPages = it.body.totalPages
                        // currentPage는 UI에서 표시될 페이지 번호를 유지
                        currentPage = page
                        Log.i("TrackingHistory", "받아온 데이터: ${it.body}")
                    }
                } else {
                    Log.e(
                        "TrackingHistory",
                        "Failed to load data: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("TrackingHistory", "Error fetching tracking history", e)
            }
        }
    }

    init {
        getTrackingHistory()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_RESUME) {
            getTrackingHistory()
        }
    }
}
