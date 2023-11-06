package com.dog.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dog.data.model.user.Dog
import com.dog.data.model.user.MatchingUserResponse
import com.dog.data.viewmodel.MatchingViewModel
import com.google.accompanist.pager.*

@Composable
fun MatchingScreen(viewModel: MatchingViewModel) {
    val listState = rememberLazyListState()
    val users = viewModel.users

    Column {

        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(viewModel.users.size) { index ->
                val user = viewModel.users[index]
                val isSelected = user.loginId == viewModel.selectedUserId.value
                UserThumbnail(user = user, isSelected = isSelected) {
                    viewModel.selectUser(user.loginId)
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        UserDetailsScreen(viewModel = viewModel, listState = listState)

        EmptyStateView(visible = users.isEmpty())
    }
}

@Composable
fun EmptyStateView(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = EnterTransition.None,
        exit = ExitTransition.None
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            // Replace this Text with your designed empty state UI and animations
            Text("추천 친구가 없습니다", style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Composable
fun UserThumbnail(user: MatchingUserResponse, isSelected: Boolean, onSelect: () -> Unit) {
    val thumbnailModifier = if (isSelected) {
        Modifier
            .padding(10.dp)
            .size(80.dp)
            .clip(CircleShape)
            .border(BorderStroke(2.dp, Color.Blue), CircleShape)
            .clickable(onClick = onSelect)
    } else {
        Modifier
            .padding(10.dp)
            .size(80.dp)
            .clip(CircleShape)
            .border(BorderStroke(2.dp, Color.Gray), CircleShape)
            .clickable(onClick = onSelect)
    }

    Box(modifier = thumbnailModifier) {
        // TODO: 이미지 로드 코드로 변경
        Text(text = user.nickname.first().toString(), modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun UserDetailsView(user: MatchingUserResponse) {
    // The main card that holds user details.
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(8.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f)
            ) {
                Text(text = user.picture, modifier = Modifier.padding(16.dp))
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserInformation(user = user)
                DogsListView(dogs = user.dogs)
            }
        }
    }
}

@Composable
fun UserInformation(user: MatchingUserResponse) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = user.nickname,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = user.aboutMe,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Gender: ${user.gender}",
            style = MaterialTheme.typography.bodySmall
        )
        user.address?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
fun DogsListView(dogs: List<Dog>?) {
    dogs?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "User's Dogs:",
                style = MaterialTheme.typography.titleMedium
            )
            it.forEach { dog ->
                DogItemView(dog)
            }
        }
    }
}

@Composable
fun DogItemView(dog: Dog) {
    // You can customize this composable to display dog details in a card or a row.
    Text(
        text = "Dog Name: ${dog.dogName}",
        style = MaterialTheme.typography.bodyMedium
    )
    Row(
        modifier = Modifier
            .padding(top = 4.dp)
            .wrapContentWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        dog.dogDispositionList.forEach { disposition ->
            DispositionChip(disposition = disposition)
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
            text = "#$disposition",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserDetailsScreen(viewModel: MatchingViewModel, listState: LazyListState) {
    if (viewModel.isDataLoaded.value && viewModel.users.isNotEmpty()) {
        // 데이터가 로드되었을 때의 UI 표시
        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f
        ) {
            viewModel.users.size
        }
        viewModel.pagerState = pagerState

        LaunchedEffect(pagerState.currentPage) {
            viewModel.selectUser(viewModel.users[pagerState.currentPage].loginId)
            listState.animateScrollToItem(pagerState.currentPage)
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            UserDetailsView(user = viewModel.users[page])
        }
    } else if (viewModel.isDataLoaded.value) {
        EmptyStateView(true)
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator() // 로딩 인디케이터를 표시합니다.
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val viewModel = MatchingViewModel()
    MatchingScreen(viewModel = viewModel)
}