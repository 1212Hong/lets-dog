package com.dog.ui.screen.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dog.data.model.feed.BoardItem
import com.dog.data.viewmodel.feed.LikeViewModel

@Composable
fun Like(feedItem: BoardItem, likeViewModel: LikeViewModel, navController: NavController) {
    val likes by likeViewModel.likes.collectAsState()
    Column(

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.clickable {
                    likeViewModel.toggleLikeStatus(feedItem)
                }
            ) {
                Icon(
                    imageVector = if (feedItem.likecheck) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                )
            }
            Row {
                CreateFeed(navController = navController)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "좋아요 ${if (likes == 0L) feedItem.boardLikes else likes} 개")
        }
    }
}
