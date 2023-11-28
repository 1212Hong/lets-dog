package com.dog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.dog.data.model.comment.CommentItem
import com.dog.data.model.feed.BoardItem
import com.dog.data.viewmodel.user.MyPageViewModel

@Composable
fun <T>DropdownMenuContent(
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit,
    item: T
) {
    val myPageViewModel: MyPageViewModel = hiltViewModel()
    var expanded by remember { mutableStateOf(false) }
    val nickname = when (item) {
        is BoardItem -> item.userNickname
        is CommentItem -> item.userNickname
        else -> ""
    }
    Box(
        modifier = Modifier.clickable {
            expanded = !expanded
        }
    ) {
        // 클릭할 때마다 expanded 값이 토글됩니다.

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            if (myPageViewModel.loginUserNickname.value == nickname){
                DropdownMenuItem(
                    onClick = {
                        onDeleteClick()
                        expanded = false
                    },
                    modifier = Modifier.background(color = Color.White),
                ) {
                    Text(text = "삭제", color = Color.Black)
                }
            }
            DropdownMenuItem(
                onClick = {
                    onReportClick()
                    expanded = false
                },
                modifier = Modifier.background(color = Color.White),
            ) {
                Text(text = "신고", color = Color.Black)
            }
        }
    }
}
