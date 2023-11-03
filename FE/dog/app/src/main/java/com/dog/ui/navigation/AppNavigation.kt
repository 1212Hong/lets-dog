package com.dog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dog.data.Screens
import com.dog.ui.screen.ChattingScreen
import com.dog.ui.screen.HomeScreen
import com.dog.ui.screen.MypageScreen
import com.dog.ui.screen.WalkingLogScreen
import com.dog.ui.screen.WalkingScreen
import com.dog.ui.screen.signin.LoginScreen
import com.dog.ui.screen.signup.SignUp

@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        composable(Screens.Home.route) {
            HomeScreen(navController)
        }
        composable(Screens.Walking.route) {
            WalkingScreen(navController)
        }
        composable(Screens.WalkingLog.route) {
            WalkingLogScreen(navController)
        }
        composable(Screens.Chatting.route) {
            ChattingScreen(navController)
        }
        composable(Screens.Mypage.route) {
            MypageScreen(navController)
        }
        composable(Screens.Signup.route) {
            SignUp(navController)
        }
        composable(Screens.Signin.route) {
            LoginScreen(navController)
        }
    }
}


