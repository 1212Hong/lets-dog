package com.dog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dog.data.Screens
import com.dog.data.viewmodel.user.UserViewModel
import com.dog.ui.screen.signin.LoginScreen
import com.dog.ui.screen.signup.SignupScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    userViewModel: UserViewModel = hiltViewModel(),
) {
    var startRoute = Screens.Home.route
    val isLogin = userViewModel.isLogin.value
    if (!isLogin) {
        // Token이 비어있는 경우(로그인 안된 경우) : 로그인 또는 회원 가입 화면을 표시
        // 이후 Token을 저장하고 앱의 다음 단계로 이동합니다.
        NavHost(
            navController = navController, startDestination = Screens.Signin.route
        ) {
            composable(Screens.Signup.route) {
                SignupScreen(navController, userViewModel)
            }
            composable(Screens.Signin.route) {
                LoginScreen(navController, userViewModel)
            }
        }
    } else {
        // Token이 있는 경우: BottomNavigationBar를 표시
        BottomNavigationBar(startRoute, userViewModel)
    }
}
