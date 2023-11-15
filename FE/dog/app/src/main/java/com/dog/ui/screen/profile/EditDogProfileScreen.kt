package com.dog.ui.screen.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.dog.data.model.user.UserUpdateRequest
import com.dog.data.viewmodel.ImageUploadViewModel
import com.dog.data.viewmodel.user.MyPageViewModel

@Composable
fun EditDogProfileScreen(
    navController: NavController,
    myPageViewModel: MyPageViewModel,
    imageUploadViewModel: ImageUploadViewModel
) {
    val dogs = myPageViewModel.dogs.collectAsState()
    var selectedDog by remember { mutableStateOf<DogInfo?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        DropdownMenuForDogs(dogs = dogs.value, selectedDog = selectedDog, onSelected = { dog -> selectedDog = dog })

        selectedDog?.let { dog ->
            DogEditFields(dog = dog, imageUploadViewModel = imageUploadViewModel) { updatedDog ->
                myPageViewModel.updateDogInfo(updatedDog)
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
        Text(text = selectedDog?.dogName ?: "강아지 선택", modifier = Modifier.padding(16.dp))
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
        label = { Text("강아지 이름") }
    )

    TextField(
        value = dogAboutMe,
        onValueChange = { dogAboutMe = it },
        label = { Text("강아지 자기소개") }
    )

    TextField(
        value = dogBirthdate,
        onValueChange = { dogBirthdate = it },
        label = { Text("강아지 생일") }
    )

    TextField(
        value = dogBreed,
        onValueChange = { dogBreed = it },
        label = { Text("강아지 견종") }
    )

    DogSizeDropdown(
        dogSize = dogSize,
        onSizeSelected = { newSize ->
            dogSize = newSize
        }
    )

    DogDispositionSelection(
        selectedDispositionsSet = selectedDispositionsSet,
        onDispositionsChanged = { newList ->
            dogDispositionList = newList
        }
    )

    Button(onClick = { imagePickerLauncher.launch("image/*") }) {
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
    }) {
        Text("강아지 정보 업데이트")
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
        columns = GridCells.Fixed(3), // 한 줄에 3개씩 표시
        modifier = Modifier.padding(16.dp)
    ) {
        items(dispositionOptions) { (english, korean) ->
            Box(modifier = Modifier.padding(4.dp)) { // 각 항목에 대한 패딩
                Row {
                    Checkbox(
                        checked = selectedDispositionsSet.value.contains(english),
                        onCheckedChange = { checked ->
                            val newSet = selectedDispositionsSet.value.toMutableSet()
                            if (checked) {
                                newSet.add(english)
                            } else {
                                newSet.remove(english)
                            }
                            selectedDispositionsSet.value = newSet
                            onDispositionsChanged(newSet.toList())
                        }
                    )
                    Text(
                        text = korean,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}
