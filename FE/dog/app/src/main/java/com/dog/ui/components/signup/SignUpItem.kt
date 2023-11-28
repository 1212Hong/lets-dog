package com.dog.ui.components.signup

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.dog.R

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpItem() {

    val passwordVector = painterResource(id = R.drawable.password_eye)
    val idValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }
    val passwordCheckValue = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = idValue.value,
        onValueChange = { idValue.value = it },
        label = { Text(text = "Phone") },
        placeholder = { Text(text = "Enter the phone number") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(0.8f),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
    )

    OutlinedTextField(
        value = passwordValue.value,
        onValueChange = { passwordValue.value = it },
        trailingIcon = {
            IconButton(onClick = {
                passwordVisibility.value = !passwordVisibility.value
            }) {
                Icon(
                    painter = passwordVector, contentDescription = "Password icon",
                    tint = if (passwordVisibility.value) Color.DarkGray else Color.Gray
                )
            }
        },
        label = { Text(text = "Password") },
        placeholder = { Text(text = "Enter the password") },
        singleLine = true,
        visualTransformation = if (passwordVisibility.value) VisualTransformation.None
        else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(0.8f)
    )
    OutlinedTextField(
        value = passwordCheckValue.value,
        onValueChange = { passwordCheckValue.value = it },
        trailingIcon = {
            IconButton(onClick = {
                passwordVisibility.value = !passwordVisibility.value
            }) {
                Icon(
                    painter = passwordVector, contentDescription = "Password icon",
                    tint = if (passwordVisibility.value) Color.DarkGray else Color.Gray
                )
            }
        },
        label = { Text(text = "Password") },
        placeholder = { Text(text = "Check the password") },
        singleLine = true,
        visualTransformation = if (passwordVisibility.value) VisualTransformation.None
        else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(0.8f)
    )
}
