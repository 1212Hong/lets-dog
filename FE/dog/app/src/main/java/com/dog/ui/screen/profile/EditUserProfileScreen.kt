package com.dog.ui.screen.profile

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
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
import androidx.navigation.NavController
import com.dog.data.model.user.UserBody
import com.dog.data.model.user.UserUpdateRequest
import com.dog.data.viewmodel.ImageUploadViewModel
import com.dog.data.viewmodel.user.MyPageViewModel
import com.dog.ui.components.MainButton
import com.dog.ui.theme.DogTheme
import com.dog.util.common.ImageLoader


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserProfileScreen(
    navController: NavController,
    myPageViewModel: MyPageViewModel,
    imageUploadViewModel: ImageUploadViewModel,
    modifier: Modifier = Modifier.fillMaxHeight()
) {
    val uploadStatus by imageUploadViewModel.uploadStatus.collectAsState()
    val uploadedImageUrls by imageUploadViewModel.uploadedImageUrls.collectAsState()
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUploadViewModel.uploadImage(it)
        }
    }

    val userInfoState = myPageViewModel.userInfo.collectAsState().value!!
    var userNickname by remember { mutableStateOf(userInfoState.userNickname ?: "ㅅ") }
    var userPhone by remember { mutableStateOf(userInfoState.userPhone ?: "") }
    var userAboutMe by remember { mutableStateOf(userInfoState.userAboutMe ?: "") }
    var userAddress by remember { mutableStateOf(userInfoState.userAddress ?: "") }
    var userPicture by remember { mutableStateOf(userInfoState.userPicture ?: "1") }
    val originalUserPicture = userInfoState.userPicture ?: "1"

    LaunchedEffect(uploadedImageUrls) {
        if (uploadedImageUrls.isNotEmpty()) {
            userPicture = uploadedImageUrls.first()
        }
    }

    fun handleDeleteImage() {
        if (uploadStatus == ImageUploadViewModel.UploadStatus.COMPLETED) {
            uploadedImageUrls.firstOrNull()?.let { url ->
                // Implement the deleteImage method in the imageUploadViewModel
                imageUploadViewModel.deleteImage(url)
                userPicture = userInfoState.userPicture ?: "1"
            }
        }
    }
    DogTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "프로필 수정") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, "뒤로가기")
                        }
                    },
                    modifier = Modifier
                        .height(40.dp)
                        .padding(top = 5.dp)
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = userNickname,
                        onValueChange = { userNickname = it },
                        label = { Text("닉네임") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = userPhone,
                        onValueChange = { newValue ->
                            if (newValue.length <= 11 && newValue.all { it.isDigit() }) {
                                userPhone = newValue
                            }
                        },
                        label = { Text("휴대폰번호") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = userAboutMe,
                        onValueChange = {
                            if (it.length <= 100) { // 최대 길이 제한
                                userAboutMe = it
                            }
                        },
                        label = { Text("자기소개") },
                        placeholder = { Text("자기소개를 입력해주세요 (최대 100자)") },
                        singleLine = false,
                        maxLines = 4,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp, max = 140.dp)
                    )

                    MainButton(
                        text = "프로필 이미지 수정",
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        onClick = { imagePickerLauncher.launch("image/*") })

                    Spacer(modifier = Modifier.size(4.dp))

                    ProfileImageSection(
                        uploadStatus = uploadStatus,
                        userPicture = userPicture,
                        originalUserPicture = originalUserPicture,
                        handleDeleteImage = ::handleDeleteImage
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    ProfileUpdateButton(
                        uploadStatus = uploadStatus,
                        userNickname = userNickname,
                        userPhone = userPhone,
                        userPicture = userPicture,
                        userAboutMe = userAboutMe,
                        userBody = userInfoState,
                        myPageViewModel = myPageViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}


@Composable
fun ProfileImageSection(
    uploadStatus: ImageUploadViewModel.UploadStatus,
    userPicture: String,
    originalUserPicture: String,
    handleDeleteImage: () -> Unit
) {
    when (uploadStatus) {
        ImageUploadViewModel.UploadStatus.IDLE,
        ImageUploadViewModel.UploadStatus.COMPLETED -> {
            ProfileImageWithDeleteButton(
                currentImageUrl = userPicture,
                originalImageUrl = originalUserPicture,
                onDelete = handleDeleteImage
            )
        }

        ImageUploadViewModel.UploadStatus.UPLOADING -> CircularProgressIndicator()
        ImageUploadViewModel.UploadStatus.FAILED -> {
            Text("업로드에 실패했습니다. 다시 시도해주세요")
        }
    }
}

@Composable
fun ProfileImageWithDeleteButton(
    currentImageUrl: String,
    originalImageUrl: String,
    onDelete: () -> Unit
) {
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        ImageLoader(imageUrl = currentImageUrl)
        if (currentImageUrl != originalImageUrl) {
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Close, contentDescription = "Delete Image")
            }
        }
    }
}

@Composable
fun ProfileUpdateButton(
    uploadStatus: ImageUploadViewModel.UploadStatus,
    userNickname: String,
    userPhone: String,
    userPicture: String,
    userAboutMe: String,
    userBody: UserBody,
    myPageViewModel: MyPageViewModel,
    navController: NavController
) {
    MainButton(
        text = "프로필 수정",
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            val updatedUser = UserUpdateRequest(
                userNickname = userNickname,
                userPhone = userPhone,
                userPicture = userPicture,
                userAboutMe = userAboutMe,
                userGender = userBody.userGender,
                userLatitude = userBody.userLatitude,
                userLongitude = userBody.userLongitude,
                userAddress = userBody.userAddress
            )
            myPageViewModel.updateUserProfile(updatedUser)
            navController.popBackStack()
        },
        enabled = uploadStatus != ImageUploadViewModel.UploadStatus.UPLOADING)
}
