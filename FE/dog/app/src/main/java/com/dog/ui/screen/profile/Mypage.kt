package com.dog.ui.screen.profile

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.dog.data.model.dog.DogInfo
import com.dog.data.model.matching.DispositionMap
import com.dog.data.model.user.UserBody
import com.dog.data.viewmodel.user.MyPageViewModel
import com.dog.ui.theme.DogTheme
import com.dog.ui.theme.Purple400
import com.dog.ui.theme.Purple500
import com.dog.util.common.ImageLoader
import com.google.accompanist.pager.HorizontalPagerIndicator


@Composable
fun MypageScreen(
    navController: NavController,
    myPageViewModel: MyPageViewModel,
    userNickname: String? = null,
    modifier: Modifier = Modifier.fillMaxHeight()
) {
    val userInfoState = myPageViewModel.userInfo.collectAsState()
    val dogs = myPageViewModel.dogs.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    val isOwnProfile =
        myPageViewModel.loginUserNickname.value == myPageViewModel.currentUserNickname.value

    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(myPageViewModel)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(myPageViewModel)
        }
    }

    DogTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ProfileImage(userInfoState.value)
                    }
                    userInfoState.value?.let { UserInfo(it) }
                    // 강아지 정보
                    DogsListView(dogs = dogs.value)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        EditProfileButton(
                            navController, isOwnProfile,
                            Modifier
                                .padding(4.dp)
                                .height(80.dp)
                                .weight(1f)
                        )
                        EditDogButton(
                            navController, isOwnProfile,
                            Modifier
                                .padding(4.dp)
                                .height(80.dp)
                                .weight(1f)
                        )
                        FriendButtons(
                            myPageViewModel, navController,
                            Modifier
                                .padding(4.dp)
                                .height(80.dp)
                                .weight(1f)
                        )
                    }
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "사진",
                            color = Purple500,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Divider(
                            color = Color.Gray,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                        ArticleImageGrid(myPageViewModel)
                    }
                }
                IconButton(
                    onClick = {
                        // 로그아웃 처리 로직
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 16.dp, end = 16.dp)
                ) {
                    Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "로그아웃")
                }
            }
        }
    }
}

@Composable
fun ProfileImage(user: UserBody?) {
    val imageUrl = user?.userPicture ?: "1"

    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Purple400)
            .border(BorderStroke(2.dp, Color.White), CircleShape)
    ) {
        // 이미지 로더를 사용해 이미지 표시
        ImageLoader(
            imageUrl = imageUrl,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            type = "thumbnail"
        )
    }
}

@Composable
fun UserInfo(user: UserBody) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = user.userNickname,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = user.userAboutMe ?: "자기소개가 없습니다.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}


@Composable
fun EditProfileButton(
    navController: NavController, isOwnProfile: Boolean, modifier: Modifier = Modifier
) {
    if (isOwnProfile) {
        Button(
            onClick = {
                navController.navigate("edit_profile")
            }, modifier = modifier
        ) {
            Text(text = "프로필\n편집", textAlign = TextAlign.Center)
        }

    }
}

@Composable
fun EditDogButton(navController: NavController, isOwnProfile: Boolean, modifier: Modifier) {
    if (isOwnProfile) {
        Button(
            onClick = {
                navController.navigate("edit_dog")
            }, modifier = modifier
        ) {
            Text(text = "강아지\n편집", textAlign = TextAlign.Center)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendButtons(
    myPageViewModel: MyPageViewModel, navController: NavController, modifier: Modifier
) {
    val isOwnProfile =
        myPageViewModel.loginUserNickname.value == myPageViewModel.currentUserNickname.value
    val toastMessage = myPageViewModel.toastMessage.value
    val context = LocalContext.current
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            myPageViewModel.clearToastMessage()
        }
    }
    if (isOwnProfile) {
        var showDialog by remember { mutableStateOf(false) }
        val friendRequests = myPageViewModel.friendRequest.collectAsState()

        Button(modifier = modifier, onClick = {
            myPageViewModel.getFriendRequests()
            showDialog = true
        }) {
            Text(text = "새 친구\n요청", textAlign = TextAlign.Center)
        }

        if (showDialog) {
            FriendRequestDialog(
                friendRequests = friendRequests.value,
                onDismiss = { showDialog = false },
                myPageViewModel = myPageViewModel,
                navController
            )
        }
    } else {
        Button(
            onClick = {
                myPageViewModel.sendFriendRequest(myPageViewModel.currentUserNickname.value!!)
            }, modifier = modifier
        ) {
            Text(text = "친구 신청")
        }
    }

}


@Composable
fun FriendRequestDialog(
    friendRequests: List<UserBody>,
    onDismiss: () -> Unit,
    myPageViewModel: MyPageViewModel,
    navController: NavController
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "친구 요청", style = MaterialTheme.typography.bodySmall)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                LazyColumn {
                    items(friendRequests) { user ->
                        FriendRequestItem(user, myPageViewModel, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun FriendRequestItem(
    user: UserBody, myPageViewModel: MyPageViewModel, navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = user.userNickname, modifier = Modifier.clickable {
//                페이지 이동 로직
            navController.navigate("profile/${user.userNickname}")
        })
        Row {
            Button(
                onClick = {
                    myPageViewModel.acceptFriendRequest(user.userNickname)
                }, modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = "승인")
            }
            Button(onClick = {
                myPageViewModel.declineFriendRequest(user.userNickname)
            }) {
                Text(text = "거절")
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DogsListView(dogs: List<DogInfo>?) {
    dogs?.let { dogList ->
        if (dogList.isNotEmpty()) {
            val pagerState = rememberPagerState(
                initialPage = 0, initialPageOffsetFraction = 0f
            ) {
                dogList.size
            }
            Column(
                modifier = Modifier.padding(start = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPager(
                    state = pagerState, modifier = Modifier.fillMaxWidth(), userScrollEnabled = true
                ) { page ->
                    Column {
                        DogItemView(dog = dogList[page])
                    }
                }

                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    pageCount = dogList.size,
                    modifier = Modifier.padding(5.dp),
                    activeColor = MaterialTheme.colorScheme.primary,
                    inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            }
        } else {
            Text(text = "등록된 강아지가 없습니다.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}


@Composable
fun DispositionChip(disposition: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(end = 4.dp)
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "#${DispositionMap.getDisposition(disposition)}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DogItemView(dog: DogInfo) {

    Text(
        text = "Dog Name: ${dog.dogName} | ${dog.dogBreed}",
        style = MaterialTheme.typography.bodyMedium
    )

    FlowRow(
        modifier = Modifier
            .padding(top = 4.dp)
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        dog.dogDispositionList.forEach { disposition ->
            DispositionChip(disposition = disposition)
        }
    }
}
