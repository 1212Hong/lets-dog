package com.dog.ui.screen.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dog.data.Screens
import com.dog.data.model.user.FriendState
import com.dog.data.viewmodel.chat.ChatViewModel
import com.dog.ui.components.MainButton
import com.dog.ui.theme.DogTheme
import com.dog.ui.theme.Pink400
import com.dog.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateChatting(navController: NavController, chatViewModel: ChatViewModel) {
    val friendList = chatViewModel.friendList
    var selectedFriends by remember { mutableStateOf(emptyList<String>()) }
    var chatroomName by remember { mutableStateOf("채팅방 이름") }

    LaunchedEffect(Unit) {
        chatViewModel.getFriendList()
    }

    val clickNewChatroom = suspend {
        chatViewModel.newChatroom(chatroomName, selectedFriends)
    }

    val clickGoBack = {
        navController.navigate(Screens.ChatList.route)
    }

    DogTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = White
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                TopAppBar(
                    modifier = Modifier.background(Pink400),
                    title = { Text(text = "채팅 생성") },
                    navigationIcon = {
                        // 뒤로가기 버튼
                        IconButton(onClick = clickGoBack) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                        }
                    }
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 채팅방 이름을 입력받는 텍스트 필드
                    TextField(
                        value = chatroomName,
                        onValueChange = { chatroomName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                    // 여기에 친구 목록을 표시하고 선택된 친구를 추가하는 부분을 구현합니다.
                    FriendList(
                        friendList = friendList,
                        onFriendSelected = { selectedFriend ->
                            // 선택된 친구를 처리하는 로직 추가
                            // 예: 선택된 친구를 저장하는 등의 동작
                            selectedFriends = selectedFriends + selectedFriend.userNickname
                        }
                    )

                    // 채팅방 생성 버튼
                    MainButton(
                        onClick = clickNewChatroom,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        text = "채팅방 생성"
                    )
                }
            }
        }
    }
}

@Composable
fun FriendList(friendList: List<FriendState>, onFriendSelected: (FriendState) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = "친구 목록", modifier = Modifier.padding(bottom = 8.dp))

        // 친구 목록을 보여주는 부분
        LazyColumn {
            items(friendList) { friend ->
                FriendListItem(
                    friend = friend,
                    onFriendSelected = { selectedFriend ->
                        onFriendSelected(selectedFriend)
                    }
                )
            }
        }
    }
}

@Composable
fun FriendListItem(friend: FriendState, onFriendSelected: (FriendState) -> Unit) {
    // 친구 목록에서 각 항목을 나타내는 UI 구성
    val isSelected by remember { mutableStateOf(friend.isSelected) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onFriendSelected(friend.copy(isSelected = !friend.isSelected)) } // 친구를 터치하면 선택 동작 수행
    ) {
        // 선택된 경우 체크 아이콘 표시
        if (isSelected) {
            // 여기에 체크 아이콘 등을 추가할 수 있습니다.
            // 예: Icon(imageVector = Icons.Default.Check, contentDescription = "선택됨")
            Icon(imageVector = Icons.Default.Check, contentDescription = "선택됨")
        }
        Text(text = friend.userNickname)
        // 여기에 필요한 경우 친구 아이콘 등을 추가할 수 있습니다.
    }
}
