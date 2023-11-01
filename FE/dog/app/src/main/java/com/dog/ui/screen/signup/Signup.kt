package com.dog.ui.screen.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dog.R
import com.dog.ui.components.CommonInput
import com.dog.ui.components.HeadingText
import com.dog.ui.components.signup.SignUpItem
import com.dog.ui.theme.DogTheme
import com.dog.ui.theme.White


@Preview
@Composable
fun SignUp() {
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
//                verticalArrangement = Arrangement.SpaceAround,
            ) {
                HeadingText(modifier = Modifier, value = stringResource(id = R.string.signup))
                CommonInput(modifier = Modifier, "핸드폰 번호")
                CommonInput(modifier = Modifier, "인증 번호", auth = true)

                SignUpItem()
            }
        }
    }

}
