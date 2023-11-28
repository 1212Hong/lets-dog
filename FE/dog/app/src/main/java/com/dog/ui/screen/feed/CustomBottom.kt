package com.dog.ui.screen.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.dog.data.model.comment.AddCommentRequest
import com.dog.data.model.feed.BoardItem
import com.dog.data.viewmodel.feed.CommentViewModel
import com.dog.data.viewmodel.user.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(
    feedItem: BoardItem,
    isSheetOpen: Boolean,
    onDismissSheet: () -> Unit,
    commentViewModel: CommentViewModel,
    userViewModel: UserViewModel
) {
    var newCommentText by remember { mutableStateOf(TextFieldValue()) }
    // 비동기적으로 데이터를 로드하고 상태를 갱신하는 부분
    LaunchedEffect(feedItem.boardId) {
        var commentList = commentViewModel.loadCommentListData(feedItem.boardId)
        commentList?.let { commentListApi ->
            commentViewModel.commentListState.clear()
            commentViewModel.commentListState.addAll(commentListApi.comments)
        }
    }

    ModalBottomSheet(
        modifier = Modifier,
        sheetState = rememberModalBottomSheetState(
            confirmValueChange = { isSheetOpen }
        ),
        onDismissRequest = {
            onDismissSheet()
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 비동기적으로 로드한 데이터를 보여주는 부분
            LazyColumn {
                items(commentViewModel.commentListState) { commentItem ->
                    CommentItem(comment = commentItem, commentViewModel = commentViewModel)
                }
                item {
                    CommentInput(
                        commentText = newCommentText,
                        onCommentTextChange = { newCommentText = it },
                        onCommentSubmit = {
                            // Submit 버튼이 클릭되었을 때의 동작 처리
                            if (newCommentText.text.isNotEmpty()) {
                                // Perform the asynchronous operation directly without coroutineScope
                                val commentAddRequest = AddCommentRequest(
                                    boardId = feedItem.boardId,
                                    commentContent = newCommentText.text
                                )
                                commentViewModel.addComment(
                                    commentAddRequest,
                                    userViewModel = userViewModel
                                )
                                newCommentText = TextFieldValue() // 입력 필드 초기화
                            }
                        }
                    )
                }
            }
        }
    }
}
