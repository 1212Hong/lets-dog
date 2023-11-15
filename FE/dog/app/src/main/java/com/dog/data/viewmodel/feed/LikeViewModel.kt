package com.dog.data.viewmodel.feed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.model.feed.BoardItem
import com.dog.data.model.like.LikeUpRequest
import com.dog.data.repository.LikeRepository
import com.dog.util.common.DataStoreManager
import com.dog.util.common.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikeViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val interceptor = RetrofitClient.RequestInterceptor(dataStoreManager)
    private val likeApi: LikeRepository =
        RetrofitClient.getInstance(interceptor).create(LikeRepository::class.java)

    private val _likes = MutableStateFlow(0L)
    val likes: MutableStateFlow<Long> get() = _likes
    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> get() = _isLiked

    //like
    fun toggleLikeStatus(feedItem: BoardItem) {
        viewModelScope.launch {
            val likeUpRequest = LikeUpRequest(feedItem.boardId)
//             likecheck 상태에 따라 게시물 좋아요 또는 취소 호출
            if (!feedItem.likecheck) {
                likePost(likeUpRequest, feedItem)
            } else {
                likeDown(feedItem)
            }
        }
    }

    suspend fun likePost(likeUpRequest: LikeUpRequest, feedItem: BoardItem) {
        viewModelScope.launch {
            Log.d("API_Response", likeUpRequest.toString())
            try {
                val response = likeApi.likeUp(likeUpRequest)
                Log.d("likeUp", response.body()?.body.toString())
                if (response.isSuccessful) {
                    Log.i("API_Response", "게시글 좋아요 등록 : ${response.body()}")
                    val likeResponse = response.body()?.body
                    likeResponse?.let {
                        // 성공적으로 좋아요가 추가되었을 때의 처리
                        if (!feedItem.likecheck) {
                            // 좋아요 수 증가 (눌렀을 때만)
                            feedItem.boardLikes += 1
                            feedItem.likecheck = true
                            _likes.value = feedItem.boardLikes
                        }
                        // 좋아요 여부 업데이트
                        _isLiked.value = feedItem.likecheck
                    }
                } else {
                    // 좋아요 추가 실패 시의 처리
                    Log.e(
                        "API_Response",
                        "Like Post Failed: ${response.code()}, ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                // 예외 발생 시의 처리
                Log.e("API_Response", "Exception: ${e.message}", e)
            }
        }
    }

    suspend fun likeDown(feedItem: BoardItem) {
        viewModelScope.launch {
            try {
                val response = likeApi.likeDown(boardId = feedItem.boardId)
                Log.d("likeDown", response.body().toString())
                if (response.isSuccessful) {
                    Log.i("API_Response", "게시글 좋아요 감소 : ${response.body()}")
                    val likeResponse = response.body()?.body
                    likeResponse?.let {
                        // 성공적으로 좋아요가 감소되었을 때의 처리
                        if (feedItem.likecheck) {
                            // 좋아요 수 감소 (눌렀을 때만)
                            feedItem.boardLikes -= 1
                            feedItem.likecheck = false
                            _likes.value = feedItem.boardLikes
                        }
                        // 좋아요 여부 업데이트
                        _isLiked.value = feedItem.likecheck
                    }
                } else {
                    // 좋아요 감소 실패 시의 처리
                    Log.e(
                        "API_Response",
                        "Like Down Failed: ${response.code()}, ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                // 예외 발생 시의 처리
                Log.e("API_Response", "Exception: ${e.message}", e)
            }
        }
    }
}
