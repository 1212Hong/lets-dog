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

@Composable
fun DropdownMenuContent(
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.clickable {
            expanded = !expanded
        }
    ) {
        // 클릭할 때마다 expanded 값이 토글됩니다.

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false // 닫기를 요청할 때 expanded 값을 false로 설정
            }
        ) {
            DropdownMenuItem(
                onClick = {
                    onDeleteClick()
                    expanded = false // 항목을 클릭하면 expanded 값을 false로 설정하여 닫힘
                },
                modifier = Modifier.background(color = Color.Gray),
            ) {
                Text(text = "삭제", color = Color.Black)
            }
            DropdownMenuItem(
                onClick = {
                    onReportClick()
                    expanded = false // 항목을 클릭하면 expanded 값을 false로 설정하여 닫힘
                },
                modifier = Modifier.background(color = Color.Gray),
            ) {
                Text(text = "신고", color = Color.Black)
            }
        }
    }
}
