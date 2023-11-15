package com.dog.ui.screen.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dog.data.model.dog.DogInfo
import com.dog.data.model.matching.DispositionMap
import com.dog.data.viewmodel.ImageUploadViewModel
import com.dog.data.viewmodel.user.MyPageViewModel
import com.dog.ui.theme.DogTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDogProfileScreen(
    navController: NavController,
    myPageViewModel: MyPageViewModel,
    imageUploadViewModel: ImageUploadViewModel
) {
    val dogs = myPageViewModel.dogs.collectAsState()
    var selectedDog by remember { mutableStateOf<DogInfo?>(null) }

    DogTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "강아지 프로필 수정") },
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                DropdownMenuForDogs(
                    dogs = dogs.value,
                    selectedDog = selectedDog,
                    onSelected = { dog -> selectedDog = dog })

                selectedDog?.let { dog ->
                    DogEditFields(
                        dog = dog,
                        imageUploadViewModel = imageUploadViewModel
                    ) { updatedDog ->
                        myPageViewModel.updateDogInfo(updatedDog)
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownMenuForDogs(
    dogs: List<DogInfo>,
    selectedDog: DogInfo?,
    onSelected: (DogInfo) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clickable { expanded = !expanded }
    ) {
        Text(
            text = selectedDog?.dogName ?: "강아지 선택",
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            dogs.forEach { dog ->
                DropdownMenuItem(
                    onClick = {
                        onSelected(dog)
                        expanded = false
                    }
                ) {
                    Text(text = dog.dogName)
                }
            }
        }
    }
}


@Composable
fun DogEditFields(
    dog: DogInfo,
    imageUploadViewModel: ImageUploadViewModel,
    onUpdate: (DogInfo) -> Unit
) {
    val uploadedImageUrls by imageUploadViewModel.uploadedImageUrls.collectAsState()
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUploadViewModel.uploadImage(it)
        }
    }

    var dogName by remember { mutableStateOf(dog.dogName) }
    var dogBirthdate by remember { mutableStateOf(dog.dogBirthdate) }
    var dogBreed by remember { mutableStateOf(dog.dogBreed) }
    var dogDispositionList by remember { mutableStateOf(dog.dogDispositionList) }
    var dogPicture by remember { mutableStateOf(dog.dogPicture ?: "1") }
    var dogAboutMe by remember { mutableStateOf(dog.dogAboutMe) }
    var dogSize by remember { mutableStateOf(dog.dogSize) }
    val selectedDispositionsSet = remember { mutableStateOf(dogDispositionList.toSet()) }

    LaunchedEffect(key1 = dog) {
        dogName = dog.dogName
        dogBirthdate = dog.dogBirthdate
        dogBreed = dog.dogBreed
        dogPicture = dog.dogPicture ?: "1"
        dogAboutMe = dog.dogAboutMe
        dogSize = dog.dogSize
        selectedDispositionsSet.value = dog.dogDispositionList.toSet()

    }

    TextField(
        value = dogName,
        onValueChange = { dogName = it },
        label = { Text("강아지 이름") },
        modifier = Modifier.fillMaxWidth()
    )

    TextField(
        value = dogAboutMe,
        onValueChange = {
            if (it.length <= 200) { // 최대 길이 제한
                dogAboutMe = it
            }
        },
        label = { Text("자기소개") },
        placeholder = { Text(" 강아지 자기소개를 입력해주세요 (최대 200자)") },
        singleLine = false,
        maxLines = 4,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp, max = 140.dp)
    )

    TextField(
        value = dogBirthdate,
        onValueChange = { dogBirthdate = it },
        label = { Text("강아지 생일") },
        modifier = Modifier.fillMaxWidth()
    )

    TextField(
        value = dogBreed,
        onValueChange = { dogBreed = it },
        label = { Text("강아지 견종") },
        modifier = Modifier.fillMaxWidth()
    )

    DogSizeDropdown(
        dogSize = dogSize,
        onSizeSelected = { newSize ->
            dogSize = newSize
        },
    )

    DogDispositionSelection(
        selectedDispositionsSet = selectedDispositionsSet,
        onDispositionsChanged = { newList ->
            dogDispositionList = newList
        }
    )

    Row {
        Button(
            onClick = { imagePickerLauncher.launch("image/*") },
            modifier = Modifier.weight(1f)
        ) {
            Text("강아지 사진 수정")
        }

        Button(onClick = {
            onUpdate(
                DogInfo(
                    dogId = dog.dogId,
                    dogName = dogName,
                    dogBirthdate = dogBirthdate,
                    dogBreed = dogBreed,
                    dogDispositionList = dogDispositionList,
                    dogAboutMe = dogAboutMe,
                    dogSize = dogSize,
                    dogPicture = uploadedImageUrls.firstOrNull() ?: dog.dogPicture,
                    userId = dog.userId
                )
            )
        }, modifier = Modifier.weight(1f)) {
            Text("강아지 정보 업데이트")
        }

    }
}

@Composable
fun DogSizeDropdown(
    dogSize: String,
    onSizeSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val dogSizes = listOf("SMALL", "MEDIUM", "LARGE")

    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { expanded = true }
    ) {
        Text(
            text = dogSize,
            modifier = Modifier.padding(16.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            dogSizes.forEach { size ->
                DropdownMenuItem(
                    onClick = {
                        onSizeSelected(size)
                        expanded = false
                    }
                ) {
                    Text(text = size)
                }
            }
        }
    }
}

@Composable
fun DogDispositionSelection(
    selectedDispositionsSet: MutableState<Set<String>>,
    onDispositionsChanged: (List<String>) -> Unit
) {
    val dispositionOptions = DispositionMap.map.entries.toList()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.padding(16.dp)
    ) {
        items(dispositionOptions) { (english, korean) ->
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .clickable {
                        val currentlyChecked = selectedDispositionsSet.value.contains(english)
                        val newSet = selectedDispositionsSet.value.toMutableSet()
                        if (currentlyChecked) {
                            newSet.remove(english)
                        } else if (newSet.size < 5) {
                            newSet.add(english)
                        }
                        if (newSet.size in 3..5) {
                            selectedDispositionsSet.value = newSet
                            onDispositionsChanged(newSet.toList())
                        }
                    }
            ) {
                Checkbox(
                    checked = selectedDispositionsSet.value.contains(english),
                    onCheckedChange = null
                )
                Text(
                    text = korean,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

