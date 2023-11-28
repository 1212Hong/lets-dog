//package com.dog.data.viewmodel.feed
//
//import android.net.Uri
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.ui.platform.LocalContext
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.dog.data.repository.FeedRepository
//import kotlinx.coroutines.launch
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//import java.io.File
//
//class UploadImageToServerViewModel : ViewModel() {
//    // 갤러리에서 선택한 이미지를 업로드하는 함수
//    fun uploadImages(selectedImageUris: List<Uri>, apiService: FeedRepository) {
//        // 업로드 중인지 여부를 나타내는 상태를 관리하는 변수
//        val uploading = mutableStateOf(true)
//
//        // 이미지 업로드 요청 보내기
//        viewModelScope.launch {
//            try {
//                selectedImageUris.forEach { imageUri ->
//                    // 이미지 파일을 MultipartBody.Part로 변환
//                    val imagePart =
//
//                    // 이미지 업로드 요청 보내기
//                    val response = apiService.uploadImage(imagePart)
//                    if (response.isSuccessful) {
//                        // 업로드 성공
//                        // 처리 완료 후 업로드 상태를 false로 변경
//                        uploading.value = false
//                    } else {
//                        // 업로드 실패
//                        // 처리 완료 후 업로드 상태를 false로 변경
//                        uploading.value = false
//                    }
//                }
//            } catch (e: Exception) {
//                // 네트워크 오류 등 예외 처리
//                // 처리 완료 후 업로드 상태를 false로 변경
//                uploading.value = false
//            }
//        }
//    }
//
//}
//
