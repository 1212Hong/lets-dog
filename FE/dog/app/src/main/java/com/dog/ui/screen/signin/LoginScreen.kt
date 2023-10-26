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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dog.R
import com.dog.ui.components.signin.SigninItem
import com.dog.ui.theme.Pink300
import com.dog.ui.theme.White

@Preview
@Composable
fun LoginScreen() {


    val loginState = false

    if (loginState) {
        Toast.makeText(LocalContext.current, "LoginSuccess!", Toast.LENGTH_LONG).show()
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
                text = "Sign In",
                style = TextStyle(fontWeight = FontWeight.Bold, letterSpacing = 2.sp),
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.padding(10.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                SigninItem()

                Spacer(modifier = Modifier.padding(10.dp))
                Button(
                    onClick = {

//                        navController.popBackStack()
//
//                        navController.navigate(Screen.AppScaffold.route) {
//
//                            popUpTo(navController.graph.startDestinationId)
//                            launchSingleTop = true
//                        }
                    }, modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                ) {
                    Text(text = "Sign In", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.padding(4.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .wrapContentHeight(), contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "Forgot my password",
                        modifier = Modifier.clickable(onClick = {
                        }), color = Pink300, fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.padding(20.dp))
                Text(
                    text = "Create an account",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.clickable(onClick = {
//                        navController.navigate(Screen.RegisterScreen.route) {
//                            popUpTo(navController.graph.startDestinationId)
//                            launchSingleTop = true
//                        }

                    })
                )
                Spacer(modifier = Modifier.padding(20.dp))
            }
        }
    }
}
