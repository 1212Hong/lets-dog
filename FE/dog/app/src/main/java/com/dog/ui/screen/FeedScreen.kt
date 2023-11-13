package com.dog.ui.screen


import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dog.data.viewmodel.feed.FeedViewModel
import com.dog.data.viewmodel.feed.HomeViewModel


@Composable
fun ForumScreen(viewModel: FeedViewModel = hiltViewModel()) {
    val localContext = LocalContext.current
    val selectedImageUris = remember { mutableStateListOf<Uri>() }

    // 갤러리에서 이미지를 가져오기 위한 런처 초기화
    val launcher: ActivityResultLauncher<String> = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri>? ->
        // 선택한 여러 이미지에 대한 작업을 수행
        uris?.let {
            selectedImageUris.addAll(it)
            Log.d("selectImage", selectedImageUris.toList().toString())
        }
    }

    DisposableEffect(selectedImageUris) {
        onDispose {
            if (selectedImageUris.isNotEmpty()) {
                viewModel.uploadImages(selectedImageUris, localContext)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 갤러리로 이동하는 버튼
        Button(
            onClick = {
                // 갤러리 열기
                launcher.launch("image/*")
            }
        ) {
            Icon(
                imageVector = Icons.Default.ThumbUp,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(24.dp)
            )
            Text(text = "갤러리에서 이미지 선택")
        }

        // 이미지 업로드 버튼
        Button(
            onClick = {
                if (selectedImageUris.isNotEmpty()) {
                    viewModel.uploadImages(selectedImageUris, localContext)
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.ThumbUp,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(24.dp)
            )
            Text(text = "이미지 업로드")
        }
    }
}





