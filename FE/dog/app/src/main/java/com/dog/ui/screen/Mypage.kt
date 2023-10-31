package com.dog.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dog.ui.components.MainButton
import com.dog.ui.theme.DogTheme
import com.dog.ui.theme.Pink400
import com.dog.ui.theme.Purple400
import com.dog.ui.theme.Purple500

@Composable
fun MypageScreen(navController: NavController) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    var images = listOf(
        // List of image URLs or resource IDs
        "test1",
        "test2",
        "333",
        // Add more image URLs or resource IDs
    )
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
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ProfileImage()
                    FollowCounts()
                }
                Text(
                    text = "#귀여움 #소형견",
                    style = MaterialTheme.typography.bodySmall
                )
                UserInfo()
                EditProfileButton(navController) // 본인이면 내정보를 편집할 페이지로
                FriendButtons()

                // 탭 선택
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.fillMaxWidth(),
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            color = Pink400, height = 2.dp,
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                    ) {
                        Text(text = "프로필", color = Purple500)
                    }

                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },

                        ) {
                        Text(text = "사진", color = Purple500)
                    }

                    // 선택한 탭에 따라 다른 화면 표시
                    when (selectedTabIndex) {
                        0 -> {
                            // 이 부분에 본인이 올린 피드에 대한 사진들 그리드로
//                            LazyColumn(
//                                modifier = Modifier.fillMaxSize(),
//                                content = {
//                                    items(images) { s ->
//                                        GridItem(imageUrl = s)
//                                    }
//                                }
//                            )
                        }

                        1 -> {
                            // 내가 좋아요 표시한 피드들 모아보기
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GridItem(imageUrl: String) {
    Text(text = imageUrl)
//    val glidePainter = rememberGlidePainter(request = imageUrl) {
//        RequestOptions().error(R.drawable.ic_error) // Placeholder image if loading fails
//    }

//    glidePainter.toPainter().let { painter ->
//        // Adjust size, padding, or other styling as needed
//        GlideImage(
//            painter = painter,
//            contentDescription = null, // Content description can be added
//            modifier = Modifier.padding(4.dp)
//        )
//    }
}


@Composable
fun ProfileImage() {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Purple400)
    ) {
        // 여기에 프로필 이미지 로딩 코드 추가
    }
}

@Composable
fun UserInfo() {
    Text(
        text = "소영섭",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(vertical = 8.dp)
    )

    Text(
        text = "새벽 산책 메이트 구해요",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun FollowCounts() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "1000",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "팔로워",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Column {
            Text(
                text = "500",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "친구수",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Column {
            Text(
                text = "300",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "팔로잉",
                style = MaterialTheme.typography.bodySmall
            )
        }


    }
}

@Composable
fun EditProfileButton(navController: NavController) {
    Button(
        onClick = {
            // navController.navigate("edit_profile")
        },
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Text(text = "프로필 편집")
    }
}

@Composable
fun FriendButtons() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        MainButton(onClick = {}, text = "친구 신청")
        MainButton(onClick = {}, text = "차단")
    }
}
