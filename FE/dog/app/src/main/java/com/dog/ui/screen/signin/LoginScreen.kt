package com.dog.ui.screen.signin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.navigation.NavController
import com.dog.R
import com.dog.data.Screens
import com.dog.data.viewmodel.user.UserViewModel
import com.dog.ui.components.signin.SigninItem
import com.dog.ui.theme.Pink300
import com.dog.ui.theme.White

@Composable
fun LoginScreen(
    navController: NavController,
    userViewModel: UserViewModel,
) {
    val context = LocalContext.current

    DisposableEffect(userViewModel.message) {
        val token = userViewModel.jwtToken.value
        val isLogin = userViewModel.isLogin.value
        if (!isLogin && !token.isNullOrEmpty()) {
            Toast.makeText(context, "Login Failed!", Toast.LENGTH_LONG).show()
        }

        onDispose {
            if (isLogin)
                Toast.makeText(context, "Login Success!", Toast.LENGTH_LONG).show()

        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.TopCenter
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Login main image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(0.8f)

            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.60f)
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(White)
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {


            Text(
                text = "같이가개",
                style = TextStyle(fontWeight = FontWeight.Bold, letterSpacing = 2.sp),
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.padding(10.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                SigninItem(userViewModel, context)

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .wrapContentHeight(), contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "비밀번호 찾기",
                        modifier = Modifier.clickable(onClick = {
                        }), color = Pink300, fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.padding(20.dp))
                Text(
                    text = "계정이 없으신가요?",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.clickable(onClick = {
                        navController.navigate(Screens.Signup.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }

                    })
                )
                Spacer(modifier = Modifier.padding(20.dp))
            }
        }
    }
}
