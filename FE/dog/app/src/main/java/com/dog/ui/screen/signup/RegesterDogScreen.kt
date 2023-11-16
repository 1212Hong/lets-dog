package com.dog.ui.screen.signup

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dog.R
import com.dog.data.model.dog.DogInfo
import com.dog.data.viewmodel.ImageUploadViewModel
import com.dog.ui.components.MainButton
import com.dog.ui.components.calander.CustomDatePicker
import com.dog.ui.screen.profile.DogDispositionSelection
import com.dog.util.common.ImageLoader
import com.dog.util.common.formatDate
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RegisterDogScreen(navController: NavController, imageUploadViewModel: ImageUploadViewModel) {
    var dogName by remember { mutableStateOf("") }
    var dogBirthdate by remember { mutableStateOf(formatDate(Date().toString(), "EEE MMM dd HH:mm:ss zzz yyyy")) }
    var dogBreed by remember { mutableStateOf("") }
    var dogDispositionList by remember { mutableStateOf(listOf<String>()) }
    val selectedDispositionsSet = remember { mutableStateOf(dogDispositionList.toSet()) }
    var dogAboutMe by remember { mutableStateOf("") }
    var dogSize by remember { mutableStateOf("") }
    var isDatePickerVisible by remember { mutableStateOf(false) }
    var isDogSizeDropdownVisible by remember { mutableStateOf(false) }
    var dogSizeSelection by remember { mutableStateOf(dogSize) }
    val dogSizes = listOf("SMALL", "MEDIUM", "LARGE")
    val uploadedImageUrls by imageUploadViewModel.uploadedImageUrls.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUploadViewModel.uploadImage(it)
        }
    }

    val clickChangeImage = {
        imagePickerLauncher.launch("image/*")
    }

    LaunchedEffect(selectedDispositionsSet.value) {
        selectedDispositionsSet.value = dogDispositionList.toSet()

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
                ImageLoader(
                    imageUrl = uploadedImageUrls.firstOrNull() ?: "https://image.utoimage.com/preview/cp872722/2022/12/202212008462_500.jpg",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.primary)
                )
                MainButton(modifier = Modifier
                    .fillMaxWidth(), text = "사진 등록", onClick = clickChangeImage)
            }

            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    value = dogName,
                    onValueChange = { dogName = it },
                    label = { Text("강아지 이름") }
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
                    label = { Text("견종") }
                )
            }

            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    value = dogAboutMe,
                    onValueChange = { dogAboutMe = it },
                    label = { Text("About Me") },
                    maxLines = 3
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
                                imageVector = Icons.Default.Check,
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
                DogDispositionSelection(selectedDispositionsSet = selectedDispositionsSet,
                    onDispositionsChanged = { newList ->
                        dogDispositionList = newList
                    })
            }

            item {
                MainButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    text = "강아지 등록하기"
                ) {
                    // 입력한 데이터 기반으로 개 등록 api 호출 필요
//                        val dog = DogInfo(
//                            dogName = dogName,
//                            dogPicture = "string", // Replace with actual picture URL or path
//                            dogBirthdate = dogBirthdate,
//                            dogBreed = dogBreed,
//                            dogDispositionList = dogDispositionList,
//                            dogAboutMe = dogAboutMe,
//                            dogSize = dogSizeSelection
//                        )
                }
            }
        }
    }
}
