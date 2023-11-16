package com.dog.data.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.repository.UploadRepository
import com.dog.util.common.DataStoreManager
import com.dog.util.common.UploadRetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class ImageUploadViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    enum class UploadStatus {
        IDLE, UPLOADING, COMPLETED, FAILED
    }

    private val _uploadStatus = MutableStateFlow(UploadStatus.IDLE)
    val uploadStatus: StateFlow<UploadStatus> = _uploadStatus.asStateFlow()

    private val interceptor = UploadRetrofitClient.RequestInterceptor(dataStoreManager)
    private val uploadApi: UploadRepository = UploadRetrofitClient.getInstance(interceptor).create(
        UploadRepository::class.java
    )
    val uploadedImageUrls = MutableStateFlow<List<String>>(emptyList())

    fun uploadImage(uri: Uri) {
        viewModelScope.launch {
            try {
                _uploadStatus.value = UploadStatus.UPLOADING
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val file = File(context.cacheDir, "upload_image.jpg").apply {
                    outputStream().use { fileOut ->
                        inputStream?.copyTo(fileOut)
                    }
                }
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

                val body = MultipartBody.Part.createFormData("files", file.name, requestFile)

                val response = uploadApi.uploadImage(body)

                if (response.isSuccessful) {
                    val uploadResponse = response.body()
                    val urls = uploadResponse?.map { it.body.url } ?: emptyList()
                    uploadedImageUrls.value = urls
                    _uploadStatus.value = UploadStatus.COMPLETED
                    Log.d("ImageUpload", "업로드된 이미지 URL: ${uploadResponse!!.get(0).body.url}")
                } else {
                    _uploadStatus.value = UploadStatus.FAILED
                    Log.e("ImageUpload", "업로드 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ImageUpload", "업로드 중 예외 발생", e)
            }
        }
    }

    fun deleteImage(url: String) {
        viewModelScope.launch {
            try {
                val response = uploadApi.deleteImage(url)
                if (response.isSuccessful) {
                    // 성공적으로 삭제되면 상태를 업데이트
                    uploadedImageUrls.value = uploadedImageUrls.value - url
                } else {
                    // 실패 로그 출력
                    Log.e("ImageDelete", "삭제 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ImageDelete", "삭제 중 예외 발생", e)
            }
        }
    }
}
