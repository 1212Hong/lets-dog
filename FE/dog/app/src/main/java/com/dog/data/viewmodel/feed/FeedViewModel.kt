package com.dog.data.viewmodel.feed

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.data.repository.FeedRepository
import com.dog.util.common.RetrofitClient
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class MyViewModel : ViewModel() {
    private val apiService = RetrofitClient.getInstance().create(FeedRepository::class.java)

    fun uploadImages(imageUris: List<Uri>, context: Context) {
        viewModelScope.launch {
            try {
                val imageParts = imageUris.map { uri ->
                    val file = File(getRealPathFromUri(context, uri))
                    val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                    Log.d("requestBody", requestBody.toString())
                    MultipartBody.Part.createFormData("images", file.name, requestBody)
                }

                val response = apiService.uploadImage(imageParts)
                if (response.isSuccessful) {
                    Log.d("ImageUpload", "이미지 업로드 성공!")
                    // 성공 처리 필요한대로 처리
                } else {
                    Log.e("ImageUpload", "이미지 업로드 실패. ${response.errorBody()}")
                    // 실패 처리 필요한대로 처리
                }
            } catch (e: Exception) {
                Log.e("ImageUpload", "이미지 업로드 오류", e)
                // 실패 처리 필요한대로 처리
            }
        }
    }
}


private fun getRealPathFromUri(context: Context, uri: Uri): String {
    val projection = arrayOf(MediaStore.Images.Media._ID)
    val cursor = context.contentResolver.query(uri, projection, null, null, null)

    cursor?.use {
        val idColumnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        if (it.moveToFirst()) {
            val id = it.getLong(idColumnIndex)
            val contentUri =
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            val projectionPath = arrayOf(MediaStore.Images.Media.DATA)
            val cursorPath =
                context.contentResolver.query(contentUri, projectionPath, null, null, null)

            cursorPath?.use { cursorPathInner ->
                val pathColumnIndex =
                    cursorPathInner.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursorPathInner.moveToFirst()) {
                    return cursorPathInner.getString(pathColumnIndex)
                }
            }
        }
    }
    return uri.path ?: ""
}
