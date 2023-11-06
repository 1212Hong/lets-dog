package com.dog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dog.data.Screens
import com.dog.ui.screen.signin.LoginScreen
import com.dog.ui.screen.signup.SignupScreen

@Composable
fun AppNavigation(navController: NavHostController, isTokenEmpty: Boolean) {
    var startRoute = Screens.Home.route
    // Token이 비어있으면 로그인 화면을 표시하고, 그렇지 않으면 BottomNavigationBar를 표시합니다.

    if (isTokenEmpty) {
        // Token이 비어있는 경우: 로그인 또는 회원 가입 화면을 표시
        // 이후 Token을 저장하고 앱의 다음 단계로 이동합니다.
//        SignIn()
        startRoute = Screens.Signin.route
        NavHost(
            navController = navController, startDestination = startRoute
        ) {
            composable(Screens.Signup.route) {
                SignupScreen(navController)
            }
            composable(Screens.Signin.route) {
//                val userViewModel: UserViewModel = viewModel()
                LoginScreen(navController)
            }
        }
    } else {
        // Token이 있는 경우: BottomNavigationBar를 표시
        startRoute = Screens.Home.route
        BottomNavigationBar(startRoute)
    }


}
