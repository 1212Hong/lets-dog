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

    // 각 게시글에 대한 좋아요 수를 Map으로 관리
    private val _likesMap = mutableMapOf<Long, MutableStateFlow<Long>>()

    // 좋아요 수를 관리하는 StateFlow를 반환하는 함수
    private fun getLikesFlow(boardId: Long): MutableStateFlow<Long> {
        return _likesMap.getOrPut(boardId) { MutableStateFlow(0L) }
    }

    // 좋아요 수를 반환하는 StateFlow를 public으로 제공
    fun getLikes(boardId: Long): StateFlow<Long> {
        return getLikesFlow(boardId)
    }

    // 게시글에 대한 좋아요 수를 업데이트하는 함수
    private fun updateLikes(boardId: Long, newLikes: Long) {
        getLikesFlow(boardId).value = newLikes
    }


    //like
    fun toggleLikeStatus(feedItem: BoardItem) {
        viewModelScope.launch {
            val likeUpRequest = LikeUpRequest(feedItem.boardId)
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
                            updateLikes(feedItem.boardId, feedItem.boardLikes)
                        }
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
                            updateLikes(feedItem.boardId, feedItem.boardLikes)
                        }
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
