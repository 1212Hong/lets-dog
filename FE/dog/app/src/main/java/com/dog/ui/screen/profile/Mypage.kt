package com.dog.ui.screen.profile

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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.dog.data.model.dog.DogInfo
import com.dog.data.model.matching.DispositionMap
import com.dog.data.model.matching.Dog
import com.dog.data.model.user.UserBody
import com.dog.data.model.user.UserUpdateRequest
import com.dog.data.viewmodel.user.MyPageViewModel
import com.dog.ui.screen.DogsListView
import com.dog.ui.theme.DogTheme
import com.dog.ui.theme.Pink400
import com.dog.ui.theme.Purple400
import com.dog.ui.theme.Purple500
import com.dog.util.common.ImageLoader
import com.google.accompanist.pager.HorizontalPagerIndicator

@Composable
fun MypageScreen(
    navController: NavController,
    myPageViewModel: MyPageViewModel,
    userNickname: String? = null
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val userInfoState = myPageViewModel.userInfo.collectAsState()
    val dogs = myPageViewModel.dogs.collectAsState()

    val isOwnProfile =
        myPageViewModel.loginUserNickname.value == myPageViewModel.currentUserNickname.value

    DogTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

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
                EditProfileButton(navController, isOwnProfile, userInfoState.value!!) // 본인이면 내정보를 편집할 페이지로
                FriendButtons(myPageViewModel, navController)
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "사진", color = Purple500, modifier = Modifier.align(Alignment.CenterHorizontally))
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    GridItem(navController, myPageViewModel)
                }
            }
        }
    }
}

@Composable
fun GridItem(navController: NavController,myPageViewModel: MyPageViewModel) {
    Text(text = "그리드")
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
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
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
fun EditProfileButton(navController: NavController, isOwnProfile:Boolean, user: UserBody) {
    if(isOwnProfile) {
        Button(
            onClick = {
                 navController.navigate("edit_profile")
            },
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "프로필 편집")
        }
        Button(
            onClick = {
                 navController.navigate("edit_dog")
            },
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "내 강아지 편집")
        }
    }
}

@Composable
fun FriendButtons(myPageViewModel: MyPageViewModel, navController: NavController) {
    val isOwnProfile =
        myPageViewModel.loginUserNickname.value == myPageViewModel.currentUserNickname.value
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        if (isOwnProfile) {
            FriendRequestsScreen(myPageViewModel, navController)
        } else {
            // 다른 사람의 프로필 페이지일 경우
            Button(
                onClick = {
                    // 친구 신청 로직
                },
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "친구 신청")
            }
        }
    }
}

@Composable
fun FriendRequestsScreen(myPageViewModel: MyPageViewModel, navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    val friendRequests = myPageViewModel.friendRequest.collectAsState()
    Button(onClick = {
        myPageViewModel.getFriendRequests()
        showDialog = true
    }) {
        Text(text = "받은 친구 신청 확인")
    }

    if (showDialog) {
        FriendRequestDialog(
            friendRequests = friendRequests.value,
            onDismiss = { showDialog = false },
            myPageViewModel = myPageViewModel,
            navController
        )
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
    user: UserBody,
    myPageViewModel: MyPageViewModel,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = user.userNickname,
            modifier = Modifier.clickable {
//                페이지 이동 로직
                navController.navigate("profile/${user.userNickname}")
            }
        )
        Row {
            Button(
                onClick = {
                    myPageViewModel.acceptFriendRequest(user.userNickname)
                },
                modifier = Modifier.padding(end = 8.dp)
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
                initialPage = 0,
                initialPageOffsetFraction = 0f
            ) {
                dogList.size
            }
            Column(modifier = Modifier.padding(start = 5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth(),
                    userScrollEnabled = true
                ) { page ->
                    Column {
                        DogItemView(dog = dogList[page])
                    }
                }

                HorizontalPagerIndicator(pagerState = pagerState,
                    pageCount = dogList.size,
                    modifier = Modifier
                        .padding(5.dp),
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
