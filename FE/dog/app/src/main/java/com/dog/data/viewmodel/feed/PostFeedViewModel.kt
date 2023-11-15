package com.dog.data.viewmodel.feed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.model.feed.BoardRequest
import com.dog.data.model.feed.PostFeedResponseResult
import com.dog.data.repository.FeedRepository
import com.dog.util.common.DataStoreManager
import com.dog.util.common.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostFeedViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val interceptor = RetrofitClient.RequestInterceptor(dataStoreManager)
    private val postFeedApi: FeedRepository =
        RetrofitClient.getInstance(interceptor).create(FeedRepository::class.java)

    // LiveData를 사용하여 API 응답 결과를 저장
    private val _postFeedResult = MutableLiveData<PostFeedResponseResult>()
    val postFeedResult: LiveData<PostFeedResponseResult>
        get() = _postFeedResult

    // Coroutine을 사용하여 비동기로 API 호출
    fun postFeed(userNickname: String, boardContent: String, fileUrlList: List<String>) {
        viewModelScope.launch {
            try {
                // API 호출
                val response = postFeedApi.PostFeedApiResponse(
                    BoardRequest(
                        boardContent = boardContent,
                        boardScope = "Everyone", // 예시로 "Everyone"으로 설정
                        boardLikes = 0, // 예시로 0으로 설정
                        fileUrlLists = fileUrlList,
                        boardComments = 0 // 예시로 0으로 설정
                    )
                )
                Log.d("res", response.body().toString())
                // API 응답 결과를 LiveData에 전달
//                _postFeedResult.value = response.body()?.body.

            } catch (e: Exception) {
                // 에러 처리
//                _postFeedResult.value = PostFeedResponseResult.("게시글 작성에 실패했습니다.")
            }
        }
    }
}

