package com.dog.ui.screen.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dog.R
import com.dog.data.model.chat.ChatroomInfo
import com.dog.data.viewmodel.chat.ChatViewModel
import com.dog.ui.components.IconComponentDrawable
import com.dog.ui.theme.Pink400

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(navController: NavController) {
    // Chat 목록 데이터를 가져오는 함수 또는 ViewModel을 사용하여 데이터를 로드합니다.
    var listState = rememberLazyListState()
    val chatViewModel: ChatViewModel = hiltViewModel()
    val chatList = chatViewModel.chatListState

    LaunchedEffect(Unit) {
        chatViewModel.getChatList()
        Log.d("chatList", chatList.toString())
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TopAppBar(
                title = { Text(text = "채팅 목록") },
                Modifier.background(Pink400)
            )
            if (chatViewModel.loading.value && chatViewModel.chatListState.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(chatList.size) { idx ->
                        ChatItem(chatList[idx], navController)
                        Divider()
                    }
                }
            } else {
                Text(text = "loading중...")
            }
        }
    }
}

@Composable
fun ChatItem(chatroom: ChatroomInfo, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // 해당 채팅방으로 이동하는 코드
                navController.navigate("chatroom/${chatroom.roomId}")
            }
            .padding(16.dp)
    ) {
        IconComponentDrawable(icon = R.drawable.person_icon, size = 56.dp)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            val memberList = chatroom.roomMembers
            var members = StringBuilder()
            for (i in 0 until memberList.size - 1) {
                members.append(memberList[i])
                if (i < memberList.size - 1) {
                    members.append(" ")
                }
            }
            Text(
                text = "$members 와의 채팅방",
                style = TextStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
//            Text(
//                text = chatroom.lastMessage,
//                style = TextStyle(
//                    color = Color.Gray,
//                    fontSize = 14.sp
//                )
//            )
        }
    }
}
