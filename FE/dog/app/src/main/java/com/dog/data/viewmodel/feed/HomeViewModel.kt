package com.dog.data.viewmodel.feed

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.model.Comment
import com.dog.data.model.comment.AddCommentRequest
import com.dog.data.model.comment.CommentItem
import com.dog.data.model.comment.CommentResponse
import com.dog.data.model.feed.BoardItem
import com.dog.data.model.feed.BoardResponse
import com.dog.data.repository.CommentRepository
import com.dog.data.repository.FeedRepository
import com.dog.data.repository.GpsRepository
import com.dog.util.common.DataStoreManager
import com.dog.util.common.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val interceptor = RetrofitClient.RequestInterceptor(dataStoreManager)
    private val addCommentApi: CommentRepository =
        RetrofitClient.getInstance(interceptor).create(CommentRepository::class.java)
    private val _feedList = mutableStateListOf<BoardItem>()
    val feedListState: SnapshotStateList<BoardItem> get() = _feedList

    private val _commentList = mutableStateListOf<CommentItem>()
    val commentListState: SnapshotStateList<CommentItem> get() = _commentList


    private var _isDataLoaded = mutableStateOf(false)
    val isDataLoaded: State<Boolean> get() = _isDataLoaded

    init {
        viewModelScope.launch {
            val userLatitude = 127.11
            val userLongitude = 35.11
            val userNickname = "test1"
            loadBoarderNearData(userLatitude, userLongitude, userNickname)
        }
    }

    init {
        viewModelScope.launch {
            val boardId = 1L
            loadCommentListData(boardId)
        }
    }

    fun addComment(addCommentRequest: AddCommentRequest) {
        viewModelScope.launch {
            Log.d("API_Response", addCommentRequest.toString())
            try {
                val response = addCommentApi.addCommentApiResponse(addCommentRequest)
                if (response.isSuccessful) {
                    Log.i("API_Response", "댓글 등록 : ${response.body()}")
                    val commentResponse = response.body()
                    commentResponse?.let {
                        // 성공적으로 댓글이 추가되었을 때의 처리
                        val newCommentItem = CommentItem(
                            boardId = addCommentRequest.boardId,
                            commentId = 0,
                            commentContent = addCommentRequest.commentContent,
                            userNickname = addCommentRequest.userNickname,
                            commentLikes = 0,
                            userProfileUrl = ""  // 여기에 프로필 URL을 설정해야 합니다.
                        )
                        _commentList.add(newCommentItem)
                        _commentsCount.value = _commentList.size
                    }
                } else {
                    // 댓글 추가 실패 시의 처리
                    Log.e(
                        "API_Response",
                        "Add Comment Failed: ${response.code()}, ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                // 예외 발생 시의 처리
                // 만약 빈 배열이 들어올 때 예외를 무시하고 싶다면 여기에 추가적인 로그만 남기거나 아무 작업을 하지 않으면 됩니다.
                Log.e("API_Response", "Exception: ${e.message}", e)
            }
        }
    }


    suspend fun loadCommentListData(
        boardId: Long
    ): CommentResponse? {
        val commentListApi: CommentRepository =
            RetrofitClient.getInstance(interceptor).create(CommentRepository::class.java)
        val commentListResponse = commentListApi.getCommentListApiResponse(
            boardId = boardId
        )
        if (commentListResponse.isSuccessful) {
            return commentListResponse.body()
        } else {
            _isDataLoaded.value = false
            Log.e(
                "API_Response",
                "API Call Failed: ${commentListResponse.code()}, ${commentListResponse.message()}"
            )
        }
        return null
    }


    suspend fun loadBoarderNearData(
        userLatitude: Double,
        userLongitude: Double,
        userNickname: String
    ): Response<BoardResponse> {
        val NearDateApi: FeedRepository =
            RetrofitClient.getInstance(interceptor).create(FeedRepository::class.java)
//     리포지토리를 통해 데이터를 불러옵니다.
        val response = NearDateApi.getBoarderNearApiResponse(
            userLatitude = userLatitude,
            userLongitude = userLongitude,
            userNickname = userNickname
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
        return response
    }

    private val _likes = MutableStateFlow(0)
    val likes: StateFlow<Int> get() = _likes

    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> get() = _isLiked


    fun toggleLikeStatus() {
        _isLiked.value = !_isLiked.value
        if (_isLiked.value) {
            _likes.value += 1
        } else {
            _likes.value -= 1
        }
    }


    private val _commentsList = mutableStateListOf<Comment>()
    val commentsList: List<Comment> get() = _commentsList

    private val _commentsCount = MutableStateFlow(0)
    val commentsCount: StateFlow<Int> get() = _commentsCount

    fun addComment(comment: Comment) {
        _commentsList.add(comment)
        _commentsCount.value = _commentsList.size
    }

}
