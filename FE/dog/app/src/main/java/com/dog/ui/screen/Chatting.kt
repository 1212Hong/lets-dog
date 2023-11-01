package com.dog.ui.screen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dog.R
import com.dog.data.local.chatList
import com.dog.data.model.Chat
import com.dog.data.model.Person
import com.dog.data.viewmodel.chat.ChatViewModel
import com.dog.ui.components.IconComponentDrawable
import com.dog.ui.components.IconComponentImageVector
import com.dog.ui.theme.DogTheme
import com.dog.ui.theme.Orange300
import com.dog.ui.theme.Purple100
import com.dog.ui.theme.Purple300
import com.dog.ui.theme.White
import com.dog.ui.theme.Yellow300
import com.dog.util.common.StompManager

private val stompManager: StompManager by lazy { StompManager() }

@Composable
fun ChattingScreen(navController: NavController, chatViewModel: ChatViewModel = viewModel()) {

    val chatStates by chatViewModel.chatStates.collectAsState()

    // Use LaunchedEffect to initialize and connect StompManager
    DisposableEffect(Unit) {
        stompManager.initializeStompClient()
        stompManager.connectStomp()

        onDispose {
            // Composable 함수가 사라질 때, 여기에서 Stomp 연결을 해제합니다.
            stompManager.onDestroy()
        }
    }

    DogTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ChatScreen(chatViewModel)
        }
    }

}

@Composable
fun UserNameRow(
    modifier: Modifier = Modifier,
    person: Person
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            IconComponentDrawable(icon = person.icon, size = 42.dp)
            Column {
                Text(
                    text = person.name, style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
                Text(
                    text = stringResource(id = R.string.online), style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                )
            }
        }
        IconComponentImageVector(icon = Icons.Default.MoreVert, size = 24.dp, tint = Color.Black)
    }
}

@Composable
fun ChatRow(
    chat: Chat,
    person: Person
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (chat.direction) Alignment.Start else Alignment.End
    ) {
        Row {
            if (chat.direction) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconComponentDrawable(icon = person.icon, size = 30.dp)
                    Text(
                        text = chat.name, style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    )
                }

            }
            Box(
                modifier = Modifier
                    .background(
                        if (chat.direction) Orange300 else Yellow300,
                        RoundedCornerShape(100.dp)
                    ),
                contentAlignment = Center
            ) {

                Text(
                    text = chat.message, style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    ),
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 15.dp),
                    textAlign = TextAlign.End
                )
            }
        }

        Text(
            text = chat.time,
            style = TextStyle(
                color = Color.Gray,
                fontSize = 12.sp
            ),
            modifier = Modifier.padding(vertical = 7.dp, horizontal = 14.dp),
        )
    }
}

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel
) {
    var data =
        rememberNavController().previousBackStackEntry?.savedStateHandle?.get<Person>("data")
            ?: Person()
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
                person = data,
                modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
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
                    )
                ) {
                    items(chatList, key = { it.id }) {
                        ChatRow(chat = it, person = data)
                    }
                }
            }
        }
        CustomTextField(
            text = chatViewModel.curMessage, onValueChange = { chatViewModel.curMessage = it },
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .align(Alignment.BottomCenter)
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
    message: String
) {
    Box(
        modifier = Modifier
            .background(Purple300, CircleShape)
            .size(33.dp)
            .clickable { stompManager.sendStomp(message) }, // 클릭 가능한 영역을 정의하고 onClick 함수 호출,
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
    onValueChange: (String) -> Unit
) {
    TextField(
        value = text, onValueChange = { onValueChange(it) },
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
                message = text
            )
        },

        modifier = modifier.fillMaxWidth(),
        shape = CircleShape
    )
}
