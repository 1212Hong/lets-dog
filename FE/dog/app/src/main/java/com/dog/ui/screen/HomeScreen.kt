package com.dog.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dog.data.viewmodel.feed.CommentViewModel
import com.dog.data.viewmodel.feed.HomeViewModel
import com.dog.data.viewmodel.feed.LikeViewModel
import com.dog.data.viewmodel.user.UserViewModel
import com.dog.ui.screen.feed.CreateFeed
import com.dog.ui.screen.feed.Feed
import com.dog.ui.theme.DogTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val commentViewModel: CommentViewModel = hiltViewModel()
    val likeViewModel: LikeViewModel = hiltViewModel()
    val userViewModel: UserViewModel = hiltViewModel()
    var feedItems = homeViewModel.feedList

    DogTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {},
                        actions = {
                            CreateFeed(navController = navController)
                        },
                        modifier = Modifier.height(60.dp).padding(10.dp)
                    )
                }
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
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
}
