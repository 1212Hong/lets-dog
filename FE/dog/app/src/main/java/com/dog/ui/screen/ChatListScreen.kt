package com.dog.ui.screen

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dog.R
import com.dog.ui.components.IconComponentDrawable
import com.dog.ui.theme.Pink400

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(navController: NavController) {
    // Chat 목록 데이터를 가져오는 함수 또는 ViewModel을 사용하여 데이터를 로드합니다.
    val chatList = remember { generateDummyChatList() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TopAppBar(
                title = { Text(text = "Chats") },
                Modifier.background(Pink400)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(chatList) { chat ->
                    ChatItem(chat, navController)
                    Divider()
                }
            }
        }
    }
}

@Composable
fun ChatItem(chat: Chat, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // 해당 채팅방으로 이동하는 코드
                navController.navigate("chatroom/${chat.roomId}")
            }
            .padding(16.dp)
    ) {
        IconComponentDrawable(icon = chat.icon, size = 56.dp)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = chat.name,
                style = TextStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
            Text(
                text = chat.lastMessage,
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            )
        }
    }
}

data class Chat(
    val roomId: Int,
    val name: String,
    val icon: Int, // Drawable resource ID
    val lastMessage: String
)

// Chat 목록의 더미 데이터 생성 함수
fun generateDummyChatList(): List<Chat> {
    return listOf(
        Chat(1, "User 1", R.drawable.ic_launcher, "Hello, how are you?"),
        Chat(2, "User 2", R.drawable.ic_launcher, "Good! How about you?"),
        Chat(3, "User 3", R.drawable.ic_launcher, "I'm doing great."),
        Chat(4, "User 4", R.drawable.ic_launcher, "Let's catch up soon!")
    )
}
