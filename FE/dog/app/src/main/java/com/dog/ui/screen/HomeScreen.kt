package com.dog.ui.screen

import android.util.Log
import android.widget.ImageView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dog.data.Screens
import com.dog.data.model.comment.AddCommentRequest
import com.dog.data.model.comment.CommentItem
import com.dog.data.model.feed.BoardItem
import com.dog.data.viewmodel.feed.CommentViewModel
import com.dog.data.viewmodel.feed.HomeViewModel
import com.dog.data.viewmodel.feed.LikeViewModel
import com.dog.data.viewmodel.user.UserViewModel
import com.dog.ui.components.DropdownMenuContent
import com.dog.ui.theme.DogTheme
import kotlinx.coroutines.runBlocking


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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),// 이미지의 높이를 제한,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(feedItems) { feedItem ->
                    FeedItemCard(
                        feedItem = feedItem,
                        homeViewModel = homeViewModel,
                        commentViewModel = commentViewModel,
                        likeViewModel = likeViewModel,
                        userViewModel = userViewModel

                    )
                }
            }
            FloatingActionButton(
                onClick = {
                    // Handle button click, e.g., navigate to a new screen
                    navController.navigate(Screens.PostFeed.route)
                },
                modifier = Modifier
                    .padding(16.dp) // Add padding for better visual appearance
                    .size(10.dp)
//                    .align(Alignment.BottomEnd) // Align the button to the bottom-right corner
            ) {
                Icon(
                    imageVector = Icons.Default.Add, // You can use any icon you prefer
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun FeedItemCard(
    feedItem: BoardItem,
    homeViewModel: HomeViewModel,
    commentViewModel: CommentViewModel,
    likeViewModel: LikeViewModel,
    userViewModel: UserViewModel
) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxSize()
            .height(600.dp) // 이미지의 높이를 제한
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            // User Info Section
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
                Text(text = feedItem.userNickname, fontWeight = FontWeight.Bold)
                // DropdownMenuContent to the right of the username
                Box(
                    modifier = Modifier
                        .weight(1f) // 여기에서 weight를 추가하여 남은 공간을 차지하도록 설정
                )
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
                            // 삭제 로직 수행
                            expanded = false
                            runBlocking { homeViewModel.deleteFeed(feedItem.boardId) }
                            // 예: commentViewModel.deleteComment(feedItem.commentId)
                        },
                        onReportClick = {
                            // 신고 로직 수행
                            expanded = false
                            // 예시: 신고 버튼 클릭 시 특정 동작 수행
                            // 예: report(feedItem)
                        }
                    )
                }
                // DropdownMenuContent is displayed only when expanded is true

            }
        }
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            HorizontalPager(feedItem = feedItem)
            ContentSection(feedItem = feedItem)
            Row(
                modifier = Modifier.padding(1.dp)
            ) {
                LikeSection(feedItem = feedItem, likeViewModel = likeViewModel)
                CommentSection(
                    feedItem = feedItem,
                    commentViewModel = commentViewModel,
                    userViewModel = userViewModel
                )
            }
        }


    }
}

@Composable
fun loadImage(imageView: ImageView, url: String) {
    Glide.with(imageView.context)
        .load(url)
        .into(imageView)
}

@Composable
fun ContentSection(feedItem: BoardItem) {
    Text(text = feedItem.boardContent)
}


@Composable
fun LikeSection(feedItem: BoardItem, likeViewModel: LikeViewModel) {
    val likes by likeViewModel.likes.collectAsState()
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
                imageVector = if (feedItem.likecheck) Icons.Outlined.ThumbUp else Icons.Filled.ThumbUp,
                contentDescription = null,
            )
            Text(text = "${likes} Likes")
        }
    }
}

@Composable
fun CommentSection(
    feedItem: BoardItem,
    commentViewModel: CommentViewModel,
    userViewModel: UserViewModel
) {
    var isCommentExpanded by remember { mutableStateOf(false) }
    var isSheetOpen by remember { mutableStateOf(false) }
    val commentsCount by commentViewModel.commentsCount.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable {
                isCommentExpanded = !isCommentExpanded
                isSheetOpen = isCommentExpanded
            }
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = null)
            Text(text = "${commentsCount} comments")
            if (isSheetOpen) {
                CustomBottomSheet(
                    feedItem = feedItem, isSheetOpen = isSheetOpen, onDismissSheet = {
                        // CustomBottomSheet가 닫힐 때 호출됩니다.
                        isSheetOpen = false
                        isCommentExpanded = false
                    },
                    commentViewModel = commentViewModel,
                    userViewModel = userViewModel
                )
            }
        }
    }
}

@Composable
fun CommentItem(comment: CommentItem, commentViewModel: CommentViewModel) {
    var expanded by remember { mutableStateOf(false) }

    DisposableEffect(expanded) {
        onDispose {
            expanded = false
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LoadImage(url = comment.userProfileUrl)
            Text(
                text = "${comment.userNickname}: ${comment.commentContent}",
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .background(MaterialTheme.colorScheme.background)
                    .padding(8.dp)
            ) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                DropdownMenuContent(
                    onDeleteClick = {
                        // 삭제 로직 수행
                        expanded = false
                        runBlocking {
                            commentViewModel.deleteComment(comment.commentId)
                        }

                    },
                    onReportClick = {
                        // 신고 로직 수행
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun ImageSection(imageUrl: String) {
    val context = LocalContext.current
    val density = LocalDensity.current.density
    val imageWidth = (context.resources.displayMetrics.widthPixels - 32 * density).toInt()
    val imageHeight = (imageWidth * 0.7).toInt()
    val modifier = Modifier
        .fillMaxWidth()
    val postImageView = remember { ImageView(context) }
    loadImage(postImageView, imageUrl)
    AndroidView(
        factory = { postImageView },
        modifier = modifier
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPager(feedItem: BoardItem) {
    val postImageUrls = feedItem.fileUrlLists
    val pagerState = rememberPagerState(pageCount = {
        postImageUrls.size
    })
    HorizontalPager(state = pagerState) { page ->
        val imageUrl = postImageUrls[page]
        ImageSection(imageUrl = imageUrl)
    }
}


@Composable
fun CommentInput(
    commentText: TextFieldValue,
    onCommentTextChange: (TextFieldValue) -> Unit,
    onCommentSubmit: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = commentText,
            onValueChange = {
                onCommentTextChange(it)
            },
            singleLine = true,
            placeholder = { Text(text = "메시지를 입력하세요.") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onCommentSubmit()
                    onCommentTextChange(TextFieldValue())
                }
            ),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        )
        Button(
            onClick = {
                onCommentSubmit()
                onCommentTextChange(TextFieldValue())
            },
            modifier = Modifier
                .height(56.dp)
                .padding(start = 8.dp)
        ) {
            Text(text = "전송")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(
    feedItem: BoardItem,
    isSheetOpen: Boolean,
    onDismissSheet: () -> Unit,
    commentViewModel: CommentViewModel,
    userViewModel: UserViewModel
) {
    var newCommentText by remember { mutableStateOf(TextFieldValue()) }
    // 비동기적으로 데이터를 로드하고 상태를 갱신하는 부분
    LaunchedEffect(feedItem.boardId) {
        val commentList = commentViewModel.loadCommentListData(feedItem.boardId)
        commentList?.let { commentListApi ->
            Log.d("commentListAPi", commentListApi.comments.toString())
            commentViewModel.commentListState.clear()
            commentViewModel.commentListState.addAll(commentListApi.comments)
        }
    }

    ModalBottomSheet(
        modifier = Modifier,
        sheetState = rememberModalBottomSheetState(
            confirmValueChange = { isSheetOpen }
        ),
        onDismissRequest = {
            onDismissSheet()
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 비동기적으로 로드한 데이터를 보여주는 부분
            LazyColumn {
                items(commentViewModel.commentListState) { commentItem ->
                    CommentItem(comment = commentItem, commentViewModel = commentViewModel)
                }
                item {
                    CommentInput(
                        commentText = newCommentText,
                        onCommentTextChange = { newCommentText = it },
                        onCommentSubmit = {
                            // Submit 버튼이 클릭되었을 때의 동작 처리
                            if (newCommentText.text.isNotEmpty()) {
                                // Perform the asynchronous operation directly without coroutineScope
                                val commentAddRequest = AddCommentRequest(
                                    boardId = feedItem.boardId,
                                    commentContent = newCommentText.text
                                )
                                commentViewModel.addComment(
                                    commentAddRequest,
                                    userViewModel = userViewModel
                                )
                                newCommentText = TextFieldValue() // 입력 필드 초기화
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LoadImage(url: String) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color.Gray)
    ) {
        AndroidView(
            factory = { context ->
                ImageView(context).apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
            },
            update = { view ->
                Glide.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions.circleCropTransform())
                    .into(view)
            }
        )
    }
}
