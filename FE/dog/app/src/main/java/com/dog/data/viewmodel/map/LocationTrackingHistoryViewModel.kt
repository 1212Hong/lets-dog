package com.dog.data.viewmodel.map

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.model.gps.TrackingHistory
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
) : ViewModel() {

    private val interceptor = RetrofitClient.RequestInterceptor(dataStoreManager)
    private val apiService: GpsRepository =
        RetrofitClient.getInstance(interceptor).create(GpsRepository::class.java)


    private val _trackingHistory = MutableStateFlow<List<TrackingHistory>>(emptyList())
    val trackingHistory = _trackingHistory.asStateFlow()
    fun getTrackingHistory() {
        viewModelScope.launch {
            try {
//                val apiService = RetrofitLocalClient.instance.create(GpsRepository::class.java)
                val retrofitResponse = apiService.getTrackingHistory()
                if (retrofitResponse.isSuccessful) {
                    Log.i("TrackingHistory", "히스토리 가져오기 성공 ${retrofitResponse.body()?.body}")
                    _trackingHistory.value = retrofitResponse.body()?.body ?: emptyList()
                } else {
                    Log.e(
                        "TrackingHistory",
                        "데이터 가져오기 실패: ${retrofitResponse.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("TrackingHistory", "에러 발생", e)
            }
        }
    }

    init {
        getTrackingHistory()
    }
}
