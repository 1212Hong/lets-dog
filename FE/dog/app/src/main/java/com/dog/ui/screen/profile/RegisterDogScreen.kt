package com.dog.ui.screen.profile

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dog.data.model.dog.RegisterDogInfo
import com.dog.data.viewmodel.ImageUploadViewModel
import com.dog.data.viewmodel.user.MyPageViewModel
import com.dog.ui.components.MainButton
import com.dog.ui.components.calander.CustomDatePicker
import com.dog.util.common.ImageLoader
import com.dog.util.common.formatDate
import java.util.Date

@Composable
fun RegisterDogScreen(
    navController: NavController,
    imageUploadViewModel: ImageUploadViewModel,
    myPageViewModel: MyPageViewModel
) {
    var dogName by remember { mutableStateOf("") }
    var dogBirthdate by remember {
        mutableStateOf(
            formatDate(
                Date().toString(),
                "EEE MMM dd HH:mm:ss zzz yyyy"
            )
        )
    }
    var dogBreed by remember { mutableStateOf("") }
    var dogDispositionList by remember { mutableStateOf(listOf<String>()) }
    val selectedDispositionsSet = remember { mutableStateOf(setOf<String>()) }
    var dogAboutMe by remember { mutableStateOf("") }
    var dogSize by remember { mutableStateOf("") }
    var isDatePickerVisible by remember { mutableStateOf(false) }
    var isDogSizeDropdownVisible by remember { mutableStateOf(false) }
    var dogSizeSelection by remember { mutableStateOf(dogSize) }
    val dogSizes = listOf("SMALL", "MEDIUM", "LARGE")
    val uploadedImageUrls by imageUploadViewModel.uploadedImageUrls.collectAsState()
    val context = LocalContext.current
    val toastMessage = myPageViewModel.toastMessage.value

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUploadViewModel.uploadImage(it)
            Log.d("image", uploadedImageUrls.firstOrNull().toString())
        }
    }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            myPageViewModel.clearToastMessage()
        }
    }

    val clickChangeImage = {
        imagePickerLauncher.launch("image/*")
    }

    val clickRegisterDog = {
        // 입력한 데이터 기반으로 개 등록 api 호출 필요
        val dog = RegisterDogInfo(
            dogName = dogName,
            dogPicture = uploadedImageUrls.firstOrNull()
                ?: "",
            dogBirthdate = dogBirthdate,
            dogBreed = dogBreed,
            dogDispositionList = dogDispositionList,
            dogAboutMe = dogAboutMe,
            dogSize = dogSizeSelection,
        )
        Log.d("dog", dog.toString())
        myPageViewModel.registerNewDog(dog)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                // Back button
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                        .heightIn(100.dp, 250.dp)
                ) {
                    ImageLoader(
                        imageUrl = uploadedImageUrls.firstOrNull()
                            ?: "https://image.utoimage.com/preview/cp872722/2022/12/202212008462_500.jpg",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.primary),
                        type = "profile"
                    )
                }

                MainButton(
                    modifier = Modifier
                        .fillMaxWidth(), text = "사진 등록", onClick = clickChangeImage
                )
            }

            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    value = dogName,
                    onValueChange = { dogName = it },
                    label = { Text("강아지 이름") },
                    singleLine = true
                )
            }

            item {
                CustomDatePicker(
                    curDate = dogBirthdate,
                    curOpen = isDatePickerVisible
                )
            }

            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    value = dogBreed,
                    onValueChange = { dogBreed = it },
                    label = { Text("견종") },
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    value = dogAboutMe,
                    onValueChange = { dogAboutMe = it },
                    label = { Text("강아지 소개 문구") },
                    maxLines = 3,
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    value = dogSizeSelection,
                    onValueChange = { /* Do nothing, read-only */ },
                    label = { Text("강아지 크기") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { isDogSizeDropdownVisible = true }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Pick Size"
                            )
                        }
                    }
                )
                if (isDogSizeDropdownVisible) {
                    DropdownMenu(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background),
                        expanded = isDogSizeDropdownVisible,
                        onDismissRequest = { isDogSizeDropdownVisible = false }
                    ) {
                        dogSizes.forEach { size ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = size)
                                },
                                onClick = {
                                    dogSizeSelection = size
                                    isDogSizeDropdownVisible = false
                                }
                            )
                        }

                    }
                }
            }

            item {
                DogDispositionSelection(
                    selectedDispositionsSet = selectedDispositionsSet,
                    onDispositionsChanged = { newList ->
                        dogDispositionList = newList
                    },
                    context = context
                )
                Log.i("checktest_register_success", selectedDispositionsSet.value.toString())

            }

            item {
                MainButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    text = "강아지 등록하기",
                    onClick = clickRegisterDog
                )

            }
        }
    }
}
