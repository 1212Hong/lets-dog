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
fun AppNavigation(navController: NavHostController, isTokenEmpty: Boolean) {
    var startRoute = Screens.Home.route
    // Token이 비어있으면 로그인 화면을 표시하고, 그렇지 않으면 BottomNavigationBar를 표시합니다.
    if (isTokenEmpty) {
        // Token이 비어있는 경우: 로그인 또는 회원 가입 화면을 표시
        // 이후 Token을 저장하고 앱의 다음 단계로 이동합니다.
//        SignIn()
        startRoute = Screens.Signin.route
    } else {
        // Token이 있는 경우: BottomNavigationBar를 표시
        startRoute = Screens.Home.route
    }
    NavHost(
        navController = navController, startDestination = startRoute
    ) {
//        navigation(startDestination = Screens.Signin.route, route = Screens.Signin.route) {
//            composable(Screens.Signup.route) {
//                SignUp(navController)
//            }
//            composable(Screens.Signin.route) {
//                LoginScreen(navController)
//            }
//        }
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
