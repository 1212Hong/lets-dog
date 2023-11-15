package com.dog.ui.screen.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dog.data.viewmodel.user.MyPageViewModel
import com.dog.util.common.ImageLoader
import com.google.accompanist.pager.HorizontalPagerIndicator

@Composable
fun ArticleImageGrid(
    myPageViewModel: MyPageViewModel,
    imageLoader: @Composable (String) -> Unit = { imageUrl ->
        ImageLoader(
            imageUrl = imageUrl,
            type = "grid"
        )
    }
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedImageUrls by remember { mutableStateOf<List<String>>(listOf()) }
    val articles = myPageViewModel.articles.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(4.dp),
    ) {
        items(articles.value) { article ->
            article.fileUrlLists.firstOrNull()?.let { imageUrl ->
                ImageGridItem(imageUrl = imageUrl, imageLoader = imageLoader, gridSize=150.dp) {
                    selectedImageUrls = article.fileUrlLists
                    showDialog = true
                }
            }
        }
    }

    if (showDialog) {
        FullScreenImageDialog(
            imageUrls = selectedImageUrls,
            onDismiss = { showDialog = false },
            imageLoader = { imageUrl ->
                ImageLoader(
                    imageUrl = imageUrl,
                )
            }
        )
    }
}

@Composable
fun ImageGridItem(
    imageUrl: String,
    imageLoader: @Composable (String) -> Unit,
    gridSize: Dp,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(gridSize)
            .padding(4.dp)
            .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(4.dp)) // 경계선 추가
            .clickable(onClick = onClick)
            .padding(1.dp)
    ) {
        imageLoader(imageUrl)
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FullScreenImageDialog(
    imageUrls: List<String>,
    onDismiss: () -> Unit,
    imageLoader: @Composable (String) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        imageUrls.size
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.6f)
        ) {
            Box {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth(),
                    userScrollEnabled = true
                ) { page ->
                    imageLoader(imageUrls[page])
                }

                HorizontalPagerIndicator(pagerState = pagerState,
                    pageCount = imageUrls.size,
                    modifier = Modifier
                        .padding(5.dp)
                        .align(Alignment.BottomCenter),
                    activeColor = MaterialTheme.colorScheme.primary,
                    inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            }
        }
    }
}