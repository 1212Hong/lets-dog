package com.dog.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dog.data.Screens
import com.dog.data.viewmodel.feed.CommentViewModel
import com.dog.data.viewmodel.feed.HomeViewModel
import com.dog.data.viewmodel.feed.LikeViewModel
import com.dog.data.viewmodel.feed.PostFeedViewModel
import com.dog.data.viewmodel.user.UserViewModel
import com.dog.ui.screen.feed.CreateFeed
import com.dog.ui.screen.feed.Feed
import com.dog.ui.theme.DogTheme

@Composable
fun HomeScreen(navController: NavController) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val commentViewModel: CommentViewModel = hiltViewModel()
    val likeViewModel: LikeViewModel = hiltViewModel()
    val userViewModel: UserViewModel = hiltViewModel()
    var feedItems = homeViewModel.feedList
    // viewmodel하나 만ㄷ르어서 내 개 있는지 확인후 없다면 개등록페이지로,
    // 마이페이지에 개 편집처럼 개 새로 등록하는거 이동하게 하기

    DogTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),// 이미지의 높이를 제한,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(feedItems) { feedItem ->
                    Feed(
                        feedItem = feedItem,
                        homeViewModel = homeViewModel,
                        commentViewModel = commentViewModel,
                        likeViewModel = likeViewModel,
                        userViewModel = userViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}
