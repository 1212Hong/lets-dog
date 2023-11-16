package com.dog.ui.screen.feed

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dog.data.model.feed.BoardItem
import com.dog.data.viewmodel.feed.CommentViewModel
import com.dog.data.viewmodel.feed.HomeViewModel
import com.dog.data.viewmodel.feed.LikeViewModel
import com.dog.data.viewmodel.user.UserViewModel
import com.dog.ui.components.DropdownMenuContent
import kotlinx.coroutines.runBlocking

@Composable
fun Feed(
    feedItem: BoardItem,
    homeViewModel: HomeViewModel,
    commentViewModel: CommentViewModel,
    likeViewModel: LikeViewModel,
    userViewModel: UserViewModel,
    navController: NavController
) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    LoadImage(url = feedItem.profileUrl)
                }
                Box(

                ) {
                    Text(text = feedItem.userNickname, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .clickable {
                            expanded = !expanded
                        }
                        .background(MaterialTheme.colorScheme.background)
                        .padding(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                    DropdownMenuContent(
                        onDeleteClick = {
                            expanded = false
                            runBlocking { homeViewModel.deleteFeed(feedItem.boardId) }
                        },
                        onReportClick = {
                            expanded = false
                        }
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            HorizontalPager(feedItem = feedItem)
            Row(
                modifier = Modifier.padding(1.dp)
            ) {
            }
        }
    }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Like(feedItem = feedItem, likeViewModel = likeViewModel, navController = navController)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = feedItem.boardContent)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Comment(
                feedItem = feedItem,
                commentViewModel = commentViewModel,
                userViewModel = userViewModel
            )
        }
    }
}
