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
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.dog.data.model.comment.AddCommentRequest
import com.dog.data.model.comment.CommentItem
import com.dog.data.model.feed.BoardItem
import com.dog.data.viewmodel.feed.HomeViewModel
import com.dog.ui.theme.DogTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(navController: NavController) {
    val homeViewModel: HomeViewModel = viewModel()
    var feedItems = homeViewModel.feedListState
    Log.d("temp", feedItems.toList().toString())
    DogTheme {
        // 가상의 피드 아이템 데이터를 생성하거나 가져온다고 가정합니다.
//        val feedItems = generateFeedItems()
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
                    FeedItemCard(feedItem = feedItem)
                }
            }
        }
    }
}

@Composable
fun FeedItemCard(feedItem: BoardItem) {
    val context = LocalContext.current
    val imageView = remember { ImageView(context) }
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxSize()
            .height(600.dp) // 이미지의 높이를 제한
//        backgroundColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // User Info Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    // Load user profile image using Glide
//                loadImage(postImageView, feedItem.userProfileImageUrl) // user profile
                }
                Text(text = feedItem.userNickname, fontWeight = FontWeight.Bold)
            }
        }
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Image Section
            // Load post image here
            HorizontalPager(feedItem = feedItem)
            ContentSection(feedItem = feedItem)
        }
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 좋아요(like) 섹션
            LikeSection(feedItem = feedItem, homeViewModel = HomeViewModel())
            CommentSection(feedItem = feedItem, homeViewModel = HomeViewModel())
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
//    val isDataLoaded by homeViewModel.isDataLoaded
//    var feedListState = homeViewModel.feedListState

//    if (isDataLoaded) {
    Text(text = feedItem.boardContent)
//    }

}


@Composable
fun LikeSection(feedItem: BoardItem, homeViewModel: HomeViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable {
                homeViewModel.toggleLikeStatus()
            }
        ) {
            Icon(
                imageVector = if (feedItem.likecheck) Icons.Outlined.ThumbUp else Icons.Filled.ThumbUp,
                contentDescription = null
            )
            Text(text = "${feedItem.boardLikes} Likes")
        }
    }
}

@Composable
fun CommentSection(feedItem: BoardItem, homeViewModel: HomeViewModel) {
    var isCommentExpanded by remember { mutableStateOf(false) }
    var newCommentText by remember { mutableStateOf(TextFieldValue()) }
    var commentsCount = remember { mutableStateOf(feedItem.boardComments) }
    var isSheetOpen by remember { mutableStateOf(false) }

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
            Text(text = "${feedItem.boardComments} Comments")
            if (isSheetOpen) {
                CustomBottomSheet(
                    feedItem = feedItem,
                    isSheetOpen = isSheetOpen,
                    onDismissSheet = {
                        // CustomBottomSheet가 닫힐 때 호출됩니다.
                        isSheetOpen = false
                    },
                    homeViewModel = homeViewModel
                )
            }
        }
    }
}

@Composable
fun CommentItem(comment: CommentItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${comment.userNickname}: ${comment.commentContent}"
        )
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
    Log.d("postImage", postImageUrls.toString())
    val pagerState = rememberPagerState(pageCount = {
        postImageUrls.size
    })
    HorizontalPager(state = pagerState) { page ->
        val imageUrl = postImageUrls[page]
        ImageSection(imageUrl = imageUrl)
    }
}

@Composable
fun CommentList(commentList: List<String>) {
    LazyColumn {
        items(commentList) { comment ->
            Text(text = comment, modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun CommentInput(
    commentText: TextFieldValue,
    onCommentTextChange: (TextFieldValue) -> Unit,
    onCommentSubmit: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            value = commentText,
            onValueChange = {
                onCommentTextChange(it)
            },
            singleLine = true,
            placeholder = { Text(text = "Enter your comment") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // 엔터 키를 눌렀을 때 메시지 전송
                    onCommentSubmit()
                    // Clear the comment text field after submitting the comment
                    onCommentTextChange(TextFieldValue())
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Button(
            onClick = {
                // 수동으로 버튼을 눌러도 댓글을 추가하고 텍스트 필드를 초기화합니다.
                onCommentSubmit()
                // Clear the comment text field after submitting the comment
                onCommentTextChange(TextFieldValue())
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Submit")
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CustomBottomSheet(
//    feedItem: BoardItem,
//    isSheetOpen: Boolean,
//    onDismissSheet: () -> Unit,
//    homeViewModel: HomeViewModel
//) {
//    var isCommentExpanded by remember { mutableStateOf(false) }
//    var newCommentText by remember { mutableStateOf(TextFieldValue()) }
//
//    ModalBottomSheet(
//        modifier = Modifier,
//        sheetState = rememberModalBottomSheetState(
//            confirmValueChange = { isSheetOpen }
//        ),
//        onDismissRequest = {
//            onDismissSheet()
//        },
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            LazyColumn {
//                items(homeViewModel.loadCommentListData(1L)) { comment ->
//                    Log.d("comment", comment.toString())
//                    CommentItem(comment = comment)
//                }
//                item {
//                    CommentInput(
//                        commentText = newCommentText,
//                        onCommentTextChange = { newCommentText = it },
//                        onCommentSubmit = {
//                            // Submit 버튼이 클릭되었을 때의 동작 처리
//                            if (newCommentText.text.isNotEmpty()) {
//                                val newComment = Comment(
//                                    id = homeViewModel.commentsList.size + 1,
//                                    username = "Your Username",
//                                    text = newCommentText.text
//                                )
//                                homeViewModel.addComment(newComment) // 댓글을 추가하고 즉시 새로고침
//                                newCommentText = TextFieldValue() // 입력 필드 초기화
//                            }
//                        }
//                    )
//                }
//            }
//        }
//    }
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(
    feedItem: BoardItem,
    isSheetOpen: Boolean,
    onDismissSheet: () -> Unit,
    homeViewModel: HomeViewModel
) {
    var isCommentExpanded by remember { mutableStateOf(false) }
    var newCommentText by remember { mutableStateOf(TextFieldValue()) }


    // 비동기적으로 데이터를 로드하고 상태를 갱신하는 부분
    LaunchedEffect(feedItem.boardId) {
        val commentList = homeViewModel.loadCommentListData(feedItem.boardId)
        commentList?.let { commentListApi ->
            Log.d("commentListAPi", commentListApi.comments.toString())
            homeViewModel.commentListState.clear()
            homeViewModel.commentListState.addAll(commentListApi.comments)
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
                items(homeViewModel.commentListState) { commentItem ->
                    CommentItem(comment = commentItem)
                }
                item {
                    val userName = feedItem.userNickname
                    val userProfileUrl = feedItem.profileUrl
                    CommentInput(
                        commentText = newCommentText,
                        onCommentTextChange = { newCommentText = it },
                        onCommentSubmit = {
                            // Submit 버튼이 클릭되었을 때의 동작 처리
                            if (newCommentText.text.isNotEmpty()) {
                                // Perform the asynchronous operation directly without coroutineScope
                                GlobalScope.launch {
                                    val commentAddRequest = AddCommentRequest(
                                        boardId = feedItem.boardId,
                                        commentContent = newCommentText.text,
                                        userNickname = userName
                                    )
                                    homeViewModel.addComment(commentAddRequest)
                                }
                                newCommentText = TextFieldValue() // 입력 필드 초기화
                            }
                        }
                    )
                }
            }
        }
    }
}
