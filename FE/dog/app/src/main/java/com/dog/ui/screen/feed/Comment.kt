package com.dog.ui.screen.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.dog.data.model.comment.CommentItem
import com.dog.data.model.feed.BoardItem
import com.dog.data.viewmodel.feed.CommentViewModel
import com.dog.data.viewmodel.user.UserViewModel
import com.dog.ui.components.DropdownMenuContent
import kotlinx.coroutines.runBlocking

@Composable
fun Comment(
    feedItem: BoardItem,
    commentViewModel: CommentViewModel,
    userViewModel: UserViewModel
) {
    var isCommentExpanded by remember { mutableStateOf(false) }
    var isSheetOpen by remember { mutableStateOf(false) }
    val commentsCount by commentViewModel.commentsCount.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable {
                isCommentExpanded = !isCommentExpanded
                isSheetOpen = isCommentExpanded
            }
        ) {
            Text(text = "댓글 $commentsCount 개 모두 보기")
            if (isSheetOpen) {
                CustomBottomSheet(
                    feedItem = feedItem, isSheetOpen = isSheetOpen, onDismissSheet = {
                        // CustomBottomSheet가 닫힐 때 호출됩니다.
                        isSheetOpen = false
                        isCommentExpanded = false
                    },
                    commentViewModel = commentViewModel,
                    userViewModel = userViewModel
                )
            }
        }
    }
}

@Composable
fun CommentItem(comment: CommentItem, commentViewModel: CommentViewModel) {
    var expanded by remember { mutableStateOf(false) }

    DisposableEffect(expanded) {
        onDispose {
            expanded = false
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LoadImage(url = comment.userProfileUrl)
            Text(
                text = "${comment.userNickname}: ${comment.commentContent}",
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .background(MaterialTheme.colorScheme.background)
                    .padding(8.dp)
            ) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                DropdownMenuContent(
                    onDeleteClick = {
                        expanded = false
                        runBlocking {
                            commentViewModel.deleteComment(comment.commentId)
                        }
                    },
                    onReportClick = {
                        expanded = false
                    },
                    item = comment
                )
            }
        }
    }
}

@Composable
fun CommentInput(
    commentText: TextFieldValue,
    onCommentTextChange: (TextFieldValue) -> Unit,
    onCommentSubmit: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = commentText,
            onValueChange = {
                onCommentTextChange(it)
            },
            singleLine = true,
            placeholder = { Text(text = "메시지를 입력하세요.") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onCommentSubmit()
                    onCommentTextChange(TextFieldValue())
                }
            ),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        )
        Button(
            onClick = {
                onCommentSubmit()
                onCommentTextChange(TextFieldValue())
            },
            modifier = Modifier
                .height(56.dp)
                .padding(start = 8.dp)
        ) {
            Text(text = "전송")
        }
    }
}
