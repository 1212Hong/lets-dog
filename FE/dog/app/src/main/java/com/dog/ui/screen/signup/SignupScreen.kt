package com.dog.ui.screen.signup

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dog.R
import com.dog.data.Screens
import com.dog.data.viewmodel.signup.SignupViewModel
import com.dog.data.viewmodel.user.UserViewModel
import com.dog.ui.components.MainButton
import com.dog.ui.theme.Gray300
import com.dog.ui.theme.White
import kotlinx.coroutines.coroutineScope


@Composable
fun SignupScreen(navController: NavController, userViewModel: UserViewModel) {
    val viewModel: SignupViewModel = hiltViewModel()
    var email by remember { mutableStateOf("") }
    var emailVerificationCode by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var nickName by remember { mutableStateOf("") }
    val toastMessage = viewModel.message.value
    val context = LocalContext.current

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.initMessage()
        }
    }

    val validateEmail = suspend {
        if(email.isNullOrEmpty())
            Toast.makeText(context, "이메일이 비었습니다.", Toast.LENGTH_SHORT).show()
        else {
            coroutineScope {
                viewModel.sendMailCode(email)
            }
        }

    }

    val checkCode = suspend {
        coroutineScope {
            viewModel.checkCode(email, emailVerificationCode)
        }
    }


    val signupAction = suspend {
        if (email.isNullOrEmpty()) {
            Toast.makeText(context, "이메일이 비었습니다.", Toast.LENGTH_SHORT).show()
        } else if (password.isNullOrEmpty()) {
            Toast.makeText(context, "비밀번호가 비었습니다.", Toast.LENGTH_SHORT).show()
        } else if (confirmPassword.isNullOrEmpty()) {
            Toast.makeText(context, "비밀번호 확인란이 비었습니다.", Toast.LENGTH_SHORT).show()
        } else if (confirmPassword != password) {
            Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
        } else if (nickName.isNullOrEmpty()) {
            Toast.makeText(context, "닉네임이 비었습니다.", Toast.LENGTH_SHORT).show()
        }
        else {
            coroutineScope {
                try {
                    userViewModel.signup(email, password, confirmPassword, nickName, true)
                    if(userViewModel.message.value == email)
                        navController.navigate(Screens.Signin.route)
                    else Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e("signup","회원가입에 실패했습니다: ${e.message}")
                }
            }
        }
    }


    val checkDupName = suspend {
        coroutineScope {
            try {
                viewModel.checkNickname(nickName)
            } catch (e: Exception) {
            }
        }

    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        // 배경 이미지 및 기타 구성 요소를 추가
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.TopCenter
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Signup main",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.60f)
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(White)
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "회원 가입",
                style = TextStyle(fontWeight = FontWeight.Bold, letterSpacing = 2.sp),
                fontSize = 30.sp,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.padding(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp).align(CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 이메일 입력 필드
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp),
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("이메일") },
                    singleLine = true
                )
                MainButton(
                    modifier = Modifier,
                    text = "인증 요청",
                    onClick = validateEmail
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp).align(CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 이메일 인증번호 입력 필드
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp),
                    value = emailVerificationCode,
                    onValueChange = { emailVerificationCode = it },
                    label = { Text("이메일 인증번호") },
                    singleLine = true
                )
                MainButton(
                    modifier = Modifier,
                    text = "인증", onClick = checkCode
                )
            }


            // 비밀번호 입력 필드
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = password,
                onValueChange = { password = it },
                label = { Text("비밀번호") },
                singleLine = true
            )

            // 비밀번호 확인 입력 필드
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("비밀번호 확인") },
                singleLine = true
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp).align(CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 닉네임 입력 필드
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp),
                    value = nickName,
                    onValueChange = { nickName = it },
                    label = { Text("닉네임 입력") },
                    singleLine = true
                )
                MainButton(
                    modifier = Modifier,
                    text = "중복 확인", onClick = checkDupName
                )
            }

            Spacer(modifier = Modifier.padding(16.dp))

            // 회원 가입 버튼
            MainButton(
                modifier = Modifier.align(CenterHorizontally),
                onClick = signupAction,
                text = "회원 가입"
            )

            Spacer(modifier = Modifier.padding(20.dp))

            // 이미 계정이 있을 경우 로그인 화면으로 이동
            Text(
                text = "이미 계정이 있으신가요?",
                fontWeight = FontWeight.Bold,
                color = Gray300,
                modifier = Modifier.align(CenterHorizontally).clickable {
                    navController.navigate(Screens.Signin.route)
                }
            )

            Spacer(modifier = Modifier.padding(20.dp))
        }
    }
}

