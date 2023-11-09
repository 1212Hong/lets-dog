package com.dog.ui.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.dog.data.model.matching.DispositionMap
import com.dog.data.model.matching.Dog
import com.dog.data.model.matching.MatchingUserResponse
import com.dog.data.viewmodel.MatchingViewModel
import com.dog.util.common.ImageLoader
import com.google.accompanist.pager.*

@Composable
fun MatchingScreen(navController: NavController) {
    val viewModel = MatchingViewModel()
    Column {
        MatchingPge(viewModel = viewModel)
    }
}

@Composable
fun MatchingPge(viewModel: MatchingViewModel) {
    val listState = rememberLazyListState()
    val users = viewModel.users

    Column {
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(viewModel.users.size) { index ->
                val user = viewModel.users[index]
                val isSelected = user.userLoginId == viewModel.selectedUserId.value
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    UserThumbnail(user = user, isSelected = isSelected) {
                        viewModel.selectUser(user.userLoginId)
                    }
                    Text(user.userNickname, fontWeight = FontWeight.Black)
                }
            }
        }


        Spacer(modifier = Modifier.height(10.dp))

        UserDetailsScreen(viewModel = viewModel, listState = listState,)

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
            Text("추천 친구가 없습니다", style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Composable
fun UserThumbnail(user: MatchingUserResponse, isSelected: Boolean, onSelect: () -> Unit) {
    val thumbnailModifier = if (isSelected) {
        Modifier
            .padding(5.dp)
            .size(80.dp)
            .clip(CircleShape)
            .border(BorderStroke(3.dp, Color.Blue), CircleShape)
            .clickable(onClick = onSelect)
    } else {
        Modifier
            .padding(5.dp)
            .size(80.dp)
            .clip(CircleShape)
            .border(BorderStroke(2.dp, Color.Gray), CircleShape)
            .clickable(onClick = onSelect)
    }

    Box {
        ImageLoader(imageUrl = user.userPicture, modifier = thumbnailModifier, type = "thumbnail")
    }
}

@Composable
fun UserDetailsView(user: MatchingUserResponse, viewModel: MatchingViewModel) {

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
                    .weight(5.5f)
            ) {
                ImageLoader(imageUrl = user.userPicture)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserInformation(user = user, viewModel = viewModel)
                DogsListView(dogs = user.dogs)
                Spacer(modifier = Modifier.height(10.dp))
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInformation(user: MatchingUserResponse, viewModel: MatchingViewModel) {
    val context = LocalContext.current
    val toastMessage = viewModel.toastMessage.value

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToastMessage()
        }
    }
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        var showDialog by remember { mutableStateOf(false) }


        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.Transparent, shape = RoundedCornerShape(8.dp)),
                properties = DialogProperties(),
                content = {
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "친구 신청",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "${user.userNickname}님에게 친구 신청을 하시겠습니까?", color = Color.White)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row {
                            Button(
                                onClick = {
                                    showDialog = false
                                    viewModel.senFriendRequest(user.userNickname)
                                }
                            ) {
                                Text("신청")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedButton(
                                onClick = { showDialog = false }
                            ) {
                                Text("취소")
                            }
                        }
                    }
                }
            )
        }
        Row(horizontalArrangement = Arrangement.Start) {
            Text(
                text = user.userNickname,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { showDialog = true }) {
                Text(text = "친구 신청")
            }

        }
        Text(
            text = user.userAboutMe,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Gender: ${user.userGender}",
            style = MaterialTheme.typography.bodySmall
        )
        user.userAddress?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DogsListView(dogs: List<Dog>?) {
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
fun DogItemView(dog: Dog) {

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserDetailsScreen(viewModel: MatchingViewModel, listState: LazyListState) {
    if (viewModel.isDataLoaded.value && viewModel.users.isNotEmpty()) {
        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f
        ) {
            viewModel.users.size
        }
        viewModel.pagerState = pagerState

        LaunchedEffect(pagerState.currentPage) {
            viewModel.selectUser(viewModel.users[pagerState.currentPage].userLoginId)
            listState.animateScrollToItem(pagerState.currentPage)
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            UserDetailsView(user = viewModel.users[page], viewModel = viewModel)
        }
    } else if (viewModel.isDataLoaded.value) {
        EmptyStateView(true)
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
