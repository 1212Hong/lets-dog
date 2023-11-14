package com.dog.ui.screen.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.dog.data.model.user.UserUpdateRequest
import com.dog.data.viewmodel.ImageUploadViewModel
import com.dog.data.viewmodel.user.MyPageViewModel

@Composable
fun EditUserProfileScreen(
    navController: NavController,
    myPageViewModel: MyPageViewModel,
    imageUploadViewModel: ImageUploadViewModel
) {
    val uploadedImageUrls by imageUploadViewModel.uploadedImageUrls.collectAsState()
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUploadViewModel.uploadImage(it)
        }
    }
    //TODO: 수정한 이미지 썸네일로 표시 후 취소 버튼 추가
    //TODO: UI 개선
    val userInfoState = myPageViewModel.userInfo.collectAsState().value!!
    var userNickname by remember { mutableStateOf(userInfoState.userNickname ?: "") }
    var userPhone by remember { mutableStateOf(userInfoState.userPhone ?: "") }
    var userPicture by remember { mutableStateOf(userInfoState.userPicture ?: "1") }
    var userAboutMe by remember { mutableStateOf(userInfoState.userAboutMe ?: "") }
    var userAddress by remember { mutableStateOf(userInfoState.userAddress ?: "") }

    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = userNickname,
            onValueChange = { userNickname = it },
            label = { Text("닉네임") }
        )
        TextField(
            value = userPhone,
            onValueChange = { newValue ->
                if (newValue.length <= 11 && newValue.all { it.isDigit() }) {
                    userPhone = newValue
                }
            },
            label = { Text("전화번호") }
        )
        Text(
            text = "전화번호를 11자리 숫자로 입력해주세요.",
            style = MaterialTheme.typography.bodySmall
        )
        TextField(
            value = userAboutMe,
            onValueChange = { userAboutMe = it },
            label = { Text("자기소개") }
        )
        TextField(
            value = userAddress,
            onValueChange = { userAddress = it },
            label = { Text("주소") }
        )
        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("프로필 사진 수정")
        }

        Button(onClick = {
            val updatedUser = UserUpdateRequest(
                userNickname = userNickname,
                userPhone = userPhone,
                userPicture = uploadedImageUrls.firstOrNull() ?: userPicture,
                userAboutMe = userAboutMe,
                userGender = userInfoState.userGender, // 변경하지 않는 필드
                userLatitude = userInfoState.userLatitude, // 변경하지 않는 필드
                userLongitude = userInfoState.userLongitude, // 변경하지 않는 필드
                userAddress = userAddress
            )
            myPageViewModel.updateUserProfile(updatedUser)
            navController.popBackStack()
        }) {
            Text("프로필 업데이트")
        }
    }
}

