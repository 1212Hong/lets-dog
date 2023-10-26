package com.dog.ui.screen.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dog.R
import com.dog.ui.components.HeadingText
import com.dog.ui.components.MainButton
import com.dog.ui.components.SignInIdInput
import com.dog.ui.components.SignInPwInput
import com.dog.ui.components.signin.SigninItem
import com.dog.ui.theme.DogTheme
import com.dog.ui.theme.White

@Preview
@Composable
fun SignIn() {


    DogTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(28.dp)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(White)
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

                ) {
                HeadingText(modifier = Modifier, value = stringResource(id = R.string.signin))
                SignInIdInput(modifier = Modifier, "id", supportText = "아이디")
                SignInPwInput(modifier = Modifier, "password", supportText = "비밀번호")

                SigninItem()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    MainButton(onClick = {}, text = "로그인")
                    MainButton(onClick = {}, text = "회원가입")
                }

            }
        }
    }

}
