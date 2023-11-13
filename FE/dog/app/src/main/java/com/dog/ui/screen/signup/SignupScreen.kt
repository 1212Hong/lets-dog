package com.dog.ui.screen.signup

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.dog.data.viewmodel.mail.MailViewModel
import com.dog.data.viewmodel.user.UserViewModel
import com.dog.ui.components.MainButton
import com.dog.ui.theme.Gray300
import com.dog.ui.theme.White
import kotlinx.coroutines.coroutineScope
import showCustomToast


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController, userViewModel: UserViewModel) {
    val viewModel: MailViewModel = hiltViewModel()
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
        coroutineScope {
            try {
                viewModel.sendMailCode(email)
                showCustomToast(context, "인증요청을 발송했습니다. : ")
            } catch (e: Exception) {
                showCustomToast(context, "인증요청에 실패했습니다. : ${e.message}")
            }
        }
    }

    val checkCode = suspend {
        coroutineScope {
            try {
                viewModel.checkCode(email, emailVerificationCode)
                showCustomToast(context, toastMessage.toString())
            } catch (e: Exception) {
                showCustomToast(context, toastMessage.toString() + "${e.message}")
            }
        }
    }


    val signupAction = suspend {
        if (email.isNullOrEmpty()) {
            showCustomToast(context, "이메일이 비었습니다.")
        } else if (password.isNullOrEmpty()) {
            showCustomToast(context, "비밀번호가 비었습니다.")
        } else if (confirmPassword.isNullOrEmpty()) {
            showCustomToast(context, "비밀번호 확인란이 비었습니다.")
        } else if (nickName.isNullOrEmpty()) {
            showCustomToast(context, "닉네임이 비었습니다.")
        }
        if (confirmPassword != password) {
            showCustomToast(context, "비밀번호가 일치하지 않습니다.")
        }
        coroutineScope {
            try {
                userViewModel.signup(email, password, confirmPassword, nickName, true)
            } catch (e: Exception) {
                showCustomToast(context, "회원가입에 실패했습니다: ${e.message}")
            }
        }

        navController.navigate(Screens.Home.route)
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
            ) {
                // 이메일 입력 필드
                OutlinedTextField(
                    modifier = Modifier
                        .padding(16.dp)
                        .width(240.dp),
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("이메일") },
                    singleLine = true
                )
                MainButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "인증 요청", onClick = validateEmail
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // 이메일 인증번호 입력 필드
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = emailVerificationCode,
                    onValueChange = { emailVerificationCode = it },
                    label = { Text("이메일 인증번호") },
                    singleLine = true
                )
                MainButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "인증", onClick = validateEmail
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

            // 닉네임 입력 필드
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = nickName,
                onValueChange = { nickName = it },
                label = { Text("닉네임 입력") },
                singleLine = true
            )

            Spacer(modifier = Modifier.padding(16.dp))

            // 회원 가입 버튼
            MainButton(
                onClick = signupAction,
                text = "회원 가입"
            )

            Spacer(modifier = Modifier.padding(20.dp))

            // 이미 계정이 있을 경우 로그인 화면으로 이동
            Text(
                text = "이미 계정이 있으신가요?",
                fontWeight = FontWeight.Bold,
                color = Gray300,
                modifier = Modifier.clickable {
                    navController.navigate(Screens.Signin.route)
                }
            )

            Spacer(modifier = Modifier.padding(20.dp))
        }
    }
}

