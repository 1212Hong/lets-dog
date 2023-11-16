package com.dog.data.viewmodel.feed

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.model.feed.BoardItem
import com.dog.data.model.feed.BoardResponse
import com.dog.data.repository.FeedRepository
import com.dog.util.common.DataStoreManager
import com.dog.util.common.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val interceptor = RetrofitClient.RequestInterceptor(dataStoreManager)
    private val feedApi: FeedRepository =
        RetrofitClient.getInstance(interceptor).create(FeedRepository::class.java)

    private val _feedList = mutableStateListOf<BoardItem>()
    val feedList: SnapshotStateList<BoardItem> get() = _feedList

    private var _isDataLoaded = mutableStateOf(false)
    val isDataLoaded: State<Boolean> get() = _isDataLoaded

    private val _isLoading = MutableStateFlow<Boolean>(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userLatitude = dataStoreManager.getLocationLatitude()
                val userLongitude = dataStoreManager.getLocationLongitude()
                Log.i("위치", "${userLatitude}, ${userLongitude}")
                loadBoarderNearData(userLatitude, userLongitude)
            } catch (e: Exception) {
                Log.e("ViewModel", "Error loading data: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun loadBoarderNearData(
        userLatitude: Double?,
        userLongitude: Double?,
    ){
        var latitude = userLatitude;
        var longitude = userLongitude;
        if (latitude == null || longitude == null) {
            latitude = dataStoreManager.getLocationLatitude()
            longitude = dataStoreManager.getLocationLongitude()
        }
        Log.i("위치", "${latitude}, ${longitude}")
        try {
            val NearDateApi: FeedRepository =
                RetrofitClient.getInstance(interceptor).create(FeedRepository::class.java)

            val response = NearDateApi.getBoarderNearApiResponse(
                userLatitude = latitude,
                userLongitude = longitude,
            )
            if (response.isSuccessful) {
                response.body()?.body?.let { boardNearApi ->
                    _feedList.clear()
                    _feedList.addAll(boardNearApi)
                    _isDataLoaded.value = true
                }
                Log.d("API_Response", "API Call Successful: ${response.body()}")
            } else {
                _isDataLoaded.value = false
                Log.e("API_Response", "API Call Failed: ${response.code()}, ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("[HomeViewModel]", "loadBoarderNearData : ${e.message}")
        }
    }
    suspend fun deleteFeed(boardId: Long) {
        viewModelScope.launch {
            try {
                val response = feedApi.deleteFeedApiResponse(boardId)
                Log.d("delete", response.body().toString())
                if (response.isSuccessful) {
                    val deleteFeedResponse = response.body()?.body
                    deleteFeedResponse?.let {
                        _feedList.removeIf { it.boardId == boardId }
                    }
                } else {
                    // 게시글 삭제 실패 시의 처리
                    Log.e(
                        "API_Response",
                        "Delete Feed Failed: ${response.code()}, ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("API_Response", "Exception: ${e.message}", e)
            }
        }
    }

    fun refreshFeedList() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userLatitude = dataStoreManager.getLocationLatitude()
                val userLongitude = dataStoreManager.getLocationLongitude()
                loadBoarderNearData(userLatitude, userLongitude)
                Log.i("갱신","갱신중")
            } catch (e: Exception) {
                Log.e("ViewModel", "Error refreshing data: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

}








