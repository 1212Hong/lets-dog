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
    private val interceptor = UploadRetrofitClient.RequestInterceptor(dataStoreManager)
    private val uploadApi: UploadRepository = UploadRetrofitClient.getInstance(interceptor).create(
        UploadRepository::class.java
    )
    val uploadedImageUrls = MutableStateFlow<List<String>>(emptyList())

    fun uploadImage(uri: Uri) {
        viewModelScope.launch {
            try {
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
                    Log.d("ImageUpload", "업로드된 이미지 URL: ${uploadResponse!!.get(0).body.url}")
                } else {
                    // 실패했을 때의 처리
                    Log.e("ImageUpload", "업로드 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ImageUpload", "업로드 중 예외 발생", e)
            }
        }
    }
}