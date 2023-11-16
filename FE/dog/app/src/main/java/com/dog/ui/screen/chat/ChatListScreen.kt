package com.dog.ui.screen.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dog.data.model.chat.ChatroomInfo
import com.dog.data.viewmodel.chat.ChatViewModel
import com.dog.ui.theme.Pink400
import com.dog.util.common.ImageLoader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(navController: NavController, chatViewModel: ChatViewModel) {
    val listState = rememberLazyListState()
    val chatList = chatViewModel.chatListState

    LaunchedEffect(chatList) {
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
                modifier = Modifier.background(Pink400),
                title = { Text(text = "채팅") },
                actions = {
                    // 여기에 새로운 채팅방을 만드는 버튼 추가
                    NewChatButton(navController)
                }
            )
            if (chatViewModel.loading.value && chatViewModel.chatListState.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState
                ) {
                    items(chatList.size) { idx ->
                        ChatItem(chatList[idx], navController, chatViewModel)
                    }
                }
            } else {
                Text(text = "loading중...")
                LinearProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun ImageGridItem(
    imageUrl: String,
    gridSize: Dp,
    index: String,
    totalItemCount: Int
) {
    Box(
        modifier = Modifier
            .size(gridSize)
            .clip(CircleShape)
    ) {
        ImageLoader(imageUrl, type = "chat", modifier = Modifier.fillMaxSize())

        // 3의 배수면 한줄 추가하기
        val isLastItem = index.toInt() == totalItemCount - 1
        val isMultipleOfThree = (index.toInt() + 1) % 3 == 0 && !isLastItem

        if (isMultipleOfThree) {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ChatItem(chatroom: ChatroomInfo, navController: NavController, chatViewModel: ChatViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // 해당 채팅방으로 이동하는 코드
                navController.navigate("chatroom/${chatroom.roomId}")
                chatViewModel.curChatroomTotalCnt = chatroom.roomMembers.size
            }
            .padding(16.dp)
    ) {
//        IconComponentDrawable(icon = R.drawable.person_icon, size = 56.dp)
        Row {
            // Convert the map entries to a list
            val roomMembersList = chatroom.roomMembers.entries.toList()

            // Create a list of rows, each containing three members
            val rows = roomMembersList.chunked(3)

            for (row in rows) {
                Row {
                    for (member in row) {
                        ImageGridItem(
                            imageUrl = member.value,
                            gridSize = 34.dp,
                            index = roomMembersList.indexOf(member).toString(),
                            totalItemCount = roomMembersList.size
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "${chatroom.roomTitle}",
                    modifier = Modifier.widthIn(88.dp, 140.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
                // 마지막 메시지와 시간 (미사용 주석 처리)
                /*
                Text(
                    text = chatroom.lastMessage,
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "2시간 전",  // TODO: 실제로는 시간 데이터를 사용해야 합니다.
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                )
                */
            }
        }

    }
}

@Composable
fun NewChatButton(navController: NavController) {
    FloatingActionButton(
        onClick = { navController.navigate("newChatting") },
        modifier = Modifier.padding(16.dp),
        contentColor = Color.White,
        content = {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    )
}
