package com.dog.ui.screen.chat

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dog.R
import com.dog.data.Screens
import com.dog.data.model.Person
import com.dog.data.model.chat.ChatState
import com.dog.data.viewmodel.chat.ChatViewModel
import com.dog.data.viewmodel.user.UserState
import com.dog.data.viewmodel.user.UserViewModel
import com.dog.ui.components.IconComponentDrawable
import com.dog.ui.components.IconComponentImageVector
import com.dog.ui.theme.DogTheme
import com.dog.ui.theme.Orange300
import com.dog.ui.theme.Purple100
import com.dog.ui.theme.Purple300
import com.dog.ui.theme.PurpleGray400
import com.dog.ui.theme.White
import com.dog.ui.theme.Yellow300
import com.dog.util.common.StompManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun ChattingScreen(
    navController: NavHostController,
    roomId: Long,
    userViewModel: UserViewModel,
    chatViewModel: ChatViewModel
) {
    val chatState by chatViewModel.chatState.collectAsState()
    val userState by userViewModel.userState.collectAsState()
    val readList by chatViewModel.readList.collectAsState()
    Log.d("chat", userState.toString())
    val coroutineScope = rememberCoroutineScope()
    val stompManager = remember { StompManager(chatViewModel, userViewModel) }

    LaunchedEffect(roomId) {
        chatViewModel.getChatHistory(roomId)
    }

    // Use LaunchedEffect to initialize and connect StompManager
    DisposableEffect(Unit) {
        stompManager.connectStomp(roomId)

        onDispose {
            // Composable 함수가 사라질 때, 여기에서 Stomp 연결을 해제합니다.
            stompManager.onDestroy()
            chatViewModel.leaveChatroom(roomId)
        }
    }

    DogTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            userState?.let {
                ChatScreen(
                    chatViewModel,
                    chatState,
                    coroutineScope,
                    navController,
                    stompManager,
                    roomId,
                    it,
                    readList = readList
                )
            }
        }
    }

}

@Composable
fun UserNameRow(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    var person =
        rememberNavController().previousBackStackEntry?.savedStateHandle?.get<Person>("data")
            ?: Person()
    val clickGoBack = {
        navController.navigate(Screens.ChatList.route)
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = clickGoBack,
                modifier = Modifier.height(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurpleGray400,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                ),
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
            IconComponentDrawable(icon = person.icon, size = 42.dp)
            Text(
                text = person.name, style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )

        }

        IconComponentImageVector(icon = Icons.Default.MoreVert, size = 24.dp, tint = Color.Black)
    }

}


@Composable
fun ChatRow(
    chat: ChatState,
    user: UserState,
    totalCnt: Int,
    readList: Set<String>
) {
    val name = user.name
    var checkMine: Boolean = chat.senderName == name

    val dateFormat = SimpleDateFormat("yyyy-MM-dd-hh-mm-a", Locale.getDefault())
    val sendTimeDate: Date = dateFormat.parse(chat.sendTime) ?: Date()
    val currentDate = Date()

    val calendarSendTime = Calendar.getInstance().apply { time = sendTimeDate }
    val calendarCurrentDate = Calendar.getInstance().apply { time = currentDate }
    val isSameDate =
        calendarSendTime.get(Calendar.YEAR) == calendarCurrentDate.get(Calendar.YEAR) &&
                calendarSendTime.get(Calendar.MONTH) == calendarCurrentDate.get(Calendar.MONTH) &&
                calendarSendTime.get(Calendar.DAY_OF_MONTH) == calendarCurrentDate.get(Calendar.DAY_OF_MONTH)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (!checkMine) Alignment.Start else Alignment.End
    ) {
        Row {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 6.dp)
            ) {
                if (!checkMine) {
                    IconComponentDrawable(
                        imageUrl = "https://mimg.segye.com/content/image/2022/05/23/20220523519355.jpg",
                        size = 30.dp
                    )
                    Text(
                        text = chat.senderName, style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    )
                }

            }
            if (checkMine) {
                Text(
                    modifier = Modifier.align(Bottom),
                    text = (totalCnt - readList.size).toString(),
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        if (chat.senderName == name) Orange300 else Yellow300,
                        RoundedCornerShape(100.dp)
                    )
                    .widthIn(max = 300.dp),
                contentAlignment = Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp), // 가로 간격 조절
                    modifier = Modifier.padding(6.dp) // 여백 추가
                ) {

                    Text(
                        text = chat.content,
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 15.sp
                        ),
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 15.dp),

                        textAlign = if (checkMine) TextAlign.End else TextAlign.Start,
                    )


                }
            }

            if (!checkMine) {
                Text(
                    modifier = Modifier.align(Bottom),
                    text = (totalCnt - readList.size).toString(),
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                )
            }
        }
        // 같은 날 메시지는 시간만 보이게
        val displayTime = if (isSameDate) {
            Log.d("sameDate", isSameDate.toString())
            SimpleDateFormat("hh:mm a", Locale.getDefault()).format(sendTimeDate)
        } else {
            chat.sendTime
        }
        Text(
            text = displayTime,
            style = TextStyle(
                color = Color.Gray,
                fontSize = 12.sp
            ),
            modifier = Modifier
                .padding(vertical = 7.dp, horizontal = 14.dp)
                .align(
                    if (checkMine) Alignment.End else Alignment.Start
                ),
        )

    }
}

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel,
    chatState: List<ChatState>,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    stompManager: StompManager,
    roomId: Long,
    userState: UserState,
    readList: Set<String>

) {
    val listState = rememberLazyListState()
    val totalCnt = chatViewModel.curChatroomTotalCnt

    // 스크롤 위치를 최하단으로 이동
    DisposableEffect(listState, chatState) {
        coroutineScope.launch {
            if (listState != null && chatState != null && chatState.isNotEmpty()) {
                listState.scrollToItem(chatState.size - 1)
            }
        }
        onDispose { /* 생명주기가 종료될 때 정리 작업을 수행하거나 해제할 수 있음 */ }
    }

    DisposableEffect(readList) {
        // readList 변경에 따른 작업 수행
        onDispose { /* 생명주기가 종료될 때 정리 작업을 수행하거나 해제할 수 있음 */ }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            UserNameRow(
                modifier = Modifier.padding(
                    top = 20.dp,
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 20.dp
                ),
                navController = navController
            )
            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(top = 25.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.padding(
                        start = 15.dp,
                        top = 25.dp,
                        end = 15.dp,
                        bottom = 75.dp
                    ),
                    state = listState, // LazyListState를 사용
                ) {
                    if (chatState.isNotEmpty()) {
                        itemsIndexed(chatState) { index, chat ->
                            val key =
                                "${index}_${chat.roomId}_${chat.senderId}_${chat.content}"
                            key(key) {
                                userState?.let { user ->
                                    ChatRow(chat = chat, user = user, totalCnt, readList)
                                }
                            }
                        }
                    }
                }
            }
        }
        CustomTextField(
            text = chatViewModel.curMessage, onValueChange = { chatViewModel.curMessage = it },
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .align(Alignment.BottomCenter),
            stompManager = stompManager,
            nickName = userState.name,
            roomId = roomId
        )
    }
}

@Composable
fun CommonIconButton(
    imageVector: ImageVector
) {
    Box(
        modifier = Modifier
            .background(Purple300, CircleShape)
            .size(33.dp), contentAlignment = Alignment.Center
    ) {
        IconComponentImageVector(icon = imageVector, size = 15.dp, tint = White)
    }
}

@Composable
fun CommonIconButtonDrawable(
    @DrawableRes icon: Int,
    message: String,
    stompManager: StompManager,
    roomId: Long,
    nickName: String
) {
    val combinedClickActions = {
        stompManager.sendStomp(roomId, nickName, message)
    }
    Box(
        modifier = Modifier
            .background(Purple300, CircleShape)
            .size(33.dp)
            .clickable { combinedClickActions() }, // 클릭 가능한 영역을 정의하고 onClick 함수 호출,
        contentAlignment = Alignment.Center

    ) {

        Icon(
            painter = painterResource(id = icon), contentDescription = "",
            tint = Color.Black,
            modifier = Modifier.size(15.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    text: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    stompManager: StompManager,
    nickName: String,
    roomId: Long
) {
    TextField(
        value = text, onValueChange = { onValueChange(it) },
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(id = R.string.type_message),
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black
                ),
                textAlign = TextAlign.Center
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Purple100,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        leadingIcon = { CommonIconButton(imageVector = Icons.Default.Add) },
        trailingIcon = {
            CommonIconButtonDrawable(
                icon = R.drawable.ic_launcher,
                message = text,
                stompManager = stompManager,
                roomId = roomId,
                nickName = nickName,
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = androidx.compose.ui.text.input.ImeAction.Done // Done 액션을 사용하면 엔터 키를 Done으로 변경
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                // 엔터 키를 눌렀을 때 수행할 작업을 여기에 추가
                stompManager.sendStomp(roomId, nickName, text)
            }
        ),

        modifier = modifier.fillMaxWidth(),
        shape = CircleShape
    )
}
