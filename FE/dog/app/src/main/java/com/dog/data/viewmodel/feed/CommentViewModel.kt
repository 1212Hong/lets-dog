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
import com.dog.data.repository.CommentRepository
import com.dog.data.viewmodel.user.UserViewModel
import com.dog.util.common.DataStoreManager
import com.dog.util.common.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {
    private val interceptor = RetrofitClient.RequestInterceptor(dataStoreManager)
    private val commentApi: CommentRepository =
        RetrofitClient.getInstance(interceptor).create(CommentRepository::class.java)

    private val _commentList = mutableStateListOf<CommentItem>()
    val commentListState: SnapshotStateList<CommentItem> = _commentList


    private val _commentsList = mutableStateListOf<Comment>()
    val commentsList: List<Comment> get() = _commentsList

    private var _commentsCount = MutableStateFlow(0L)
    val commentsCount: MutableStateFlow<Long> get() = _commentsCount

    private var _isDataLoaded = mutableStateOf(false)
    val isDataLoaded: State<Boolean> get() = _isDataLoaded

    init {
        viewModelScope.launch {
            val boardId = 1L
            loadCommentListData(boardId)
        }
    }


    fun addComment(
        addCommentRequest: AddCommentRequest,
        userViewModel: UserViewModel
    ) {
        viewModelScope.launch {
            Log.d("API_Response", addCommentRequest.toString())
            try {

                val response = commentApi.addCommentApiResponse(addCommentRequest)

                if (response.isSuccessful) {
                    Log.i("API_Response", "댓글 등록 : ${response.body()}")
                    val commentResponse = response.body()
                    commentResponse?.let {
                        val userInfo = userViewModel.userInfo.value //TODO::물어보기 왜 안됨?
                        if (userInfo != null) {
                            val userNickname = userInfo.userNickname
                            val userProfileUrl = userInfo.userPicture
                            val newCommentItem = CommentItem(
                                boardId = addCommentRequest.boardId,
                                commentId = 0,
                                commentContent = addCommentRequest.commentContent,
                                userNickname = userNickname,
                                commentLikes = 0,
                                userProfileUrl = userProfileUrl,  // 여기에 프로필 URL을 설정해야 합니다.
                            )
                            commentListState.add(newCommentItem)
                            commentsCount.value = _commentList.size.toLong()
                        } else {
                            // 사용자 정보가 없을 경우에 대한 처리
                            Log.e("API_Response", "User information is not available")
                        }
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
            _commentList.clear()
            _commentList.addAll(commentListResponse.body()?.comments ?: emptyList())
            _commentsCount.value = _commentList.size.toLong()
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


    fun deleteComment(comment: Long) {
        viewModelScope.launch {
            try {
                val response = commentApi.deleteCommentApiResponse(commentId = comment)
                Log.d("deleteComment", response.body().toString())
                if (response.isSuccessful) {
                    Log.i("API_Response", "댓글 삭제 성공 : ${response.body()}")
                    val deleteCommentResponse = response.body()?.body
                    deleteCommentResponse?.let {
                        _commentList.removeIf { it.commentId == comment }
                    }
                } else {
                    // 댓글 삭제 실패 시의 처리
                    Log.e(
                        "API_Response",
                        "Delete Comment Failed: ${response.code()}, ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                // 예외 발생 시의 처리
                Log.e("API_Response", "Exception: ${e.message}", e)
            }
        }
    }
}
