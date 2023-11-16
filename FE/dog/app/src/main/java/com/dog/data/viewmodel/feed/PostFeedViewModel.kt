package com.dog.data.viewmodel.feed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.model.feed.BoardRequest
import com.dog.data.repository.FeedRepository
import com.dog.util.common.DataStoreManager
import com.dog.util.common.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostFeedViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {
    private val interceptor = RetrofitClient.RequestInterceptor(dataStoreManager)
    private val postFeedApi: FeedRepository =
        RetrofitClient.getInstance(interceptor).create(FeedRepository::class.java)

    private var _boardScope = MutableStateFlow("Everyone")
    val boardScope: MutableStateFlow<String>
        get() = _boardScope

    private var _fileUrls = MutableStateFlow<List<String>>(emptyList())
    val fileUrls: MutableStateFlow<List<String>>
        get() = _fileUrls

    fun setBoardScope(scope: String) {
        _boardScope.value = scope
    }

    // Coroutine을 사용하여 비동기로 API 호출
    fun postFeed(
        userNickname: String,
        boardContent: String,
        fileUrls: List<String>,
        boardScope: String
    ) {
        viewModelScope.launch {
            try {
//                 API 호출
                val response = postFeedApi.postFeedApiResponse(
                    BoardRequest(
                        boardContent = boardContent,
                        boardScope = boardScope, // _boardScope.value 대신 매개변수 값을 사용합니다.
                        boardLikes = 0, // 예시로 0으로 설정
                        fileUrlLists = fileUrls,
                        boardComments = 0 // 예시로 0으로 설정
                    )
                )
                Log.d("res", "API 호출 성공: ${response.body().toString()}")
//                 API 응답 결과를 LiveData에 전달
//                 _postFeedResult.value = response.body()?.body.

            } catch (e: Exception) {
                // 에러 처리
                Log.e("res", "API 호출 실패: ${e.message}")
                // _postFeedResult.value = PostFeedResponseResult.("게시글 작성에 실패했습니다.")
            }
        }
    }
}






