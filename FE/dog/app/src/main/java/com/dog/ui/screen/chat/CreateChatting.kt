package com.dog.ui.screen.chat

import android.util.Log
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.dog.data.viewmodel.user.UserViewModel
import com.dog.ui.components.MainButton
import com.dog.ui.theme.DogTheme
import com.dog.ui.theme.Pink400
import com.dog.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateChatting(navController: NavController, chatViewModel: ChatViewModel, userViewModel: UserViewModel) {
    val friendList = chatViewModel.friendList
    val myName: String? = userViewModel.userState.collectAsState().value?.name
    var selectedFriends by remember { mutableStateOf(listOf(myName)) }
    var chatroomName by remember { mutableStateOf("") }
    val flag = chatViewModel.loading


    LaunchedEffect(Unit) {
        chatViewModel.getFriendList()
    }

    val clickNewChatroom = suspend {
        chatViewModel.newChatroom(chatroomName, selectedFriends)
        if(flag.value) navController.navigate(Screens.ChatList.route)
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
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        value = chatroomName,
                        onValueChange = { chatroomName = it },
                        label = { Text("채팅방 이름") },
                        maxLines = 3
                    )


                    // 채팅방 생성 버튼
                    MainButton(
                        onClick = clickNewChatroom,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        text = "채팅방 생성"
                    )

                    FriendList(
                        friendList = friendList,
                        selectedFriends = selectedFriends,
                        onFriendSelected = { selectedFriend ->
                            // 선택된 친구를 처리하는 로직 추가
                            // 선택된 친구를 저장 동작
                            if (selectedFriends.contains(selectedFriend.userNickname)) {
                                selectedFriends = selectedFriends - selectedFriend.userNickname
                            } else {
                                selectedFriends = selectedFriends + selectedFriend.userNickname
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FriendList(
    friendList: List<FriendState>,
    selectedFriends: List<String?>,
    onFriendSelected: (FriendState) -> Unit
) {
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
                    isSelected = selectedFriends.contains(friend.userNickname),
                    onFriendSelected = { selectedFriend ->
                        onFriendSelected(selectedFriend)
                    }
                )
            }
        }
    }
}

@Composable
fun FriendListItem(
    friend: FriendState,
    isSelected: Boolean,
    onFriendSelected: (FriendState) -> Unit
) {
    // 친구 목록에서 각 항목을 나타내는 UI 구성
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onFriendSelected(friend) } // 친구를 터치하면 선택 동작 수행
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

