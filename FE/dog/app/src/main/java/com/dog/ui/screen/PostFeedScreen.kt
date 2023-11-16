package com.dog.ui.screen

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dog.data.Screens
import com.dog.data.model.feed.BoardRequest
import com.dog.data.viewmodel.ImageUploadViewModel
import com.dog.data.viewmodel.feed.PostFeedViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PostFeedContent(
    navController: NavController,
    postFeedViewModel: PostFeedViewModel,
    imageUploadViewModel: ImageUploadViewModel,
    onPostFeedClick: (BoardRequest) -> Unit
) {
    var boardContent by remember { mutableStateOf("") }
    val boardScope by postFeedViewModel.boardScope.collectAsState()
    val fileUrls by imageUploadViewModel.uploadedImageUrls.collectAsState()
    var isUploadComplete by remember { mutableStateOf(false) }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row() {
                OutlinedTextField(
                    value = boardContent,
                    onValueChange = { boardContent = it },
                    label = { Text("게시글 내용") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .height(300.dp)
                )
            }
            var expanded by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("공개 범위: ", modifier = Modifier.clickable { expanded = !expanded })
                Spacer(modifier = Modifier.width(8.dp))
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    DropdownMenuItem(onClick = {
                        postFeedViewModel.setBoardScope("Everyone")
                        expanded = false
                    }) {
                        Text("전체 공개")
                    }
                    DropdownMenuItem(onClick = {
                        postFeedViewModel.setBoardScope("Friends")
                        expanded = false
                    }) {
                        Text("친구만 공개")
                    }
                    DropdownMenuItem(onClick = {
                        postFeedViewModel.setBoardScope("MeOnly")
                        expanded = false
                    }) {
                        Text("비공개")
                    }
                }
                Text(postFeedViewModel.boardScope.value)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ImageSelectionWithUrl(
                    onImageUrlsSelected = { imageUrls ->
                        Log.d("image", imageUrls.toString())
                    },
                    imageUploadViewModel = imageUploadViewModel,
                    onUploadComplete = { uploadComplete ->
                        isUploadComplete = uploadComplete
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val boardRequest = BoardRequest(
                            boardContent = boardContent,
                            boardScope = boardScope,
                            boardLikes = 0,
                            fileUrlLists = fileUrls,
                            boardComments = 0
                        )
                        onPostFeedClick(boardRequest)
                        navController.navigate(Screens.Home.route)
                    }, enabled = isUploadComplete,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("게시")
                }
            }

        }

    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PostFeedScreen(navController: NavController) {
    val postFeedViewModel: PostFeedViewModel = hiltViewModel()
    val imageUploadViewModel: ImageUploadViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("게시글 작성") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp)
            ) {
                PostFeedContent(
                    navController = navController,
                    postFeedViewModel = postFeedViewModel,
                    imageUploadViewModel = imageUploadViewModel
                ) { boardRequest ->
                    postFeedViewModel.postFeed(
                        userNickname = "",
                        boardContent = boardRequest.boardContent,
                        fileUrls = boardRequest.fileUrlLists,
                        boardScope = boardRequest.boardScope
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    )
}

@Composable
fun ImageSelectionWithUrl(
    onImageUrlsSelected: (List<Uri>) -> Unit,
    imageUploadViewModel: ImageUploadViewModel,
    onUploadComplete: (Boolean) -> Unit
) {
    var selectedImageUrls by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val fileUrls by imageUploadViewModel.uploadedImageUrls.collectAsState()

    val getContent = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        uris?.let {
            selectedImageUrls = uris
            onImageUrlsSelected(selectedImageUrls)
        }
    }

    LaunchedEffect(selectedImageUrls) {
        selectedImageUrls.forEach { uri ->
            imageUploadViewModel.multiUploadImage(uri)
        }
    }
    LaunchedEffect(fileUrls.size) {
        onUploadComplete(selectedImageUrls.size == fileUrls.size && selectedImageUrls.isNotEmpty())
    }

    Spacer(modifier = Modifier.height(16.dp))
    Button(onClick = { getContent.launch("image/*") }) {
        Text("이미지 선택")
    }

}

