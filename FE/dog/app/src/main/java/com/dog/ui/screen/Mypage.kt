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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dog.ui.components.MainButton
import com.dog.ui.theme.DogTheme
import com.dog.ui.theme.Purple400

@Composable
fun MypageScreen(navController: NavController) {
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
                verticalArrangement = Arrangement.Center
            ) {
                // 프로필 이미지
                Box(
                    modifier = Modifier
                        .size(120.dp) // 이미지 크기 조절
                        .clip(CircleShape) // 이미지를 원형으로 클리핑
                        .background(Purple400) // 배경색
                ) {
                    // 여기에 프로필 이미지를 로드하는 코드를 추가할 수 있습니다.
                    // 예: CoilImage 또는 painterResource를 사용하여 이미지 로드
                }

                // 사용자 이름
                Text(
                    text = "소영섭",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // 사용자 정보
                Text(
                    text = "새벽 산책 메이트 구해요",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                // 팔로워 및 팔로잉 수
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Followers: 1000",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = "Following: 500",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "#귀여움 #소형견",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Column {
                    // 편집 버튼
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
                    Row() {
                        MainButton(onClick = {}, text = "친구 신청")
                        MainButton(onClick = {}, text = "차단")
                    }

                }

            }
        }
    }
}
