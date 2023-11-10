package com.dog.ui.components.signin

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.dog.R
import com.dog.data.viewmodel.user.UserViewModel
import com.dog.ui.components.MainButton
import kotlinx.coroutines.coroutineScope
import showCustomToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SigninItem(userViewModel: UserViewModel) {

    val passwordVector = painterResource(id = R.drawable.password_eye)
    val emailValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val loginAction = suspend {
        coroutineScope {
            val email = emailValue.value
            val password = passwordValue.value
            try {
                userViewModel.login(email, password)
            } catch (e: Exception) {
                Log.d("error?", e.toString())
                showCustomToast(context, "로그인에 실패했습니다: ${e.message}")
            }
//            store.saveToken(userViewModel.jwtToken.value)
        }
    }

    OutlinedTextField(
        value = emailValue.value,
        onValueChange = { emailValue.value = it },
        label = { Text(text = "Email ID") },
        placeholder = { Text(text = "이메일 주소를 입력하세요!!") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(0.8f),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),

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
        placeholder = { Text(text = "") },
        singleLine = true,
        visualTransformation = if (passwordVisibility.value) VisualTransformation.None
        else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(0.8f)
    )

    Spacer(modifier = Modifier.padding(10.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        MainButton(onClick = loginAction, text = "로그인")
    }
    Spacer(modifier = Modifier.padding(4.dp))
}
