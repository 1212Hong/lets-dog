package com.dog.ui.screen.signup

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
import com.dog.ui.components.MainButton
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterDogScreen(navController: NavController) {
    var dogName by remember { mutableStateOf("") }
    var dogBirthdate by remember { mutableStateOf(Date().toString()) }
    var dogBreed by remember { mutableStateOf("") }
    var dogDispositionList by remember {
        mutableStateOf(
            listOf(
                "FRIENDLY",
                "FRIENDLY",
                "FRIENDLY"
            )
        )
    }
    var dogAboutMe by remember { mutableStateOf("") }
    var dogSize by remember { mutableStateOf("") }

    // To show date picker
    var isDatePickerVisible by remember { mutableStateOf(false) }

    // To show dropdown for dog size
    var isDogSizeDropdownVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Format for displaying and parsing the date
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    val calendar = Calendar.getInstance()

    // Handle date picker dialog visibility
    var pickerState = rememberDatePickerState()

    // Handle dog size dropdown
    var dogSizeSelection by remember { mutableStateOf(dogSize) }

    val dogSizes = listOf("SMALL", "MEDIUM", "LARGE")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            item {
                // Back button
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }

            item {
                // Dog picture (you can add image upload functionality here)
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Dog Picture",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            // Handle image upload or selection
                            // Open an image picker, etc.
                        }
                )
            }

            item {
                // Dog name input
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    value = dogName,
                    onValueChange = { dogName = it },
                    label = { Text("Dog Name") }
                )
            }

            item {
                // Dog birthdate input with date picker
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    value = dateFormat.format(dogBirthdate),
                    onValueChange = { /* Do nothing, read-only */ },
                    label = { Text("Dog Birthdate") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { isDatePickerVisible = true }) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Pick Date"
                            )
                        }
                    }
                )
                // Show date picker dialog
                if (isDatePickerVisible) {
                    DatePicker(
                        state = pickerState,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    )
                }
            }

            item {
                // Dog breed input
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    value = dogBreed,
                    onValueChange = { dogBreed = it },
                    label = { Text("Dog Breed") }
                )
            }

            item {
                // Dog disposition list input
                Column {
                    Text("Dog Disposition List")
                    dogDispositionList.forEachIndexed { index, disposition ->
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            value = disposition,
                            onValueChange = {
                                val newList = dogDispositionList.toMutableList()
                                newList[index] = it
                                dogDispositionList = newList
                            },
                            label = { Text("Disposition ${index + 1}") }
                        )
                    }
                }
            }

            item {
                // Dog about me input
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
                // Dog size dropdown
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    value = dogSizeSelection,
                    onValueChange = { /* Do nothing, read-only */ },
                    label = { Text("Dog Size") },
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
                // Show dog size dropdown
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
                // Signup button
                MainButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    text = "Register Dog"
                ) {
                    // Handle dog registration logic here
//                        val dog = DogInfo(
//                            dogName = dogName,
//                            userId = 0,
//                            dogPicture = "string", // Replace with actual picture URL or path
//                            dogBirthdate = dogBirthdate,
//                            dogBreed = dogBreed,
//                            dogDispositionList = dogDispositionList,
//                            dogAboutMe = dogAboutMe,
//                            dogSize = dogSizeSelection
//                        )
                    // Call your registration API or perform necessary actions
                    // ...

                    // Navigate back or to the next screen
//                        navController.popBackStack()
                }
            }
        }
    }
}
