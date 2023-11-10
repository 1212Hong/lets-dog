package com.dog.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dog.data.Screens
import com.dog.data.viewmodel.user.UserViewModel
import com.dog.ui.screen.signin.LoginScreen
import com.dog.ui.screen.signup.SignupScreen
import com.dog.util.common.DataStoreManager

@Composable
fun AppNavigation(
    navController: NavHostController,
    userViewModel: UserViewModel,
    store: DataStoreManager
) {
    var startRoute = Screens.Home.route
    // Token이 비어있으면 로그인 화면을 표시하고, 그렇지 않으면 BottomNavigationBar를 표시합니다.

    val tokenText = store.getAccessToken.collectAsState(initial = "")
    val isTokenEmpty = tokenText.value.isEmpty()
    Log.d("TokenInAppNavigation", tokenText.value)
    val isLogin = userViewModel.isLogin.value

    if (isLogin) {
        // 로그인이 성공한 경우 홈 화면으로 이동
        startRoute = Screens.Home.route
    } else {
        // 로그인이 되어있지 않은 경우 로그인 또는 회원 가입 화면 표시
        startRoute = Screens.Signin.route
    }
    if (!isLogin) {
        // Token이 비어있는 경우: 로그인 또는 회원 가입 화면을 표시
        // 이후 Token을 저장하고 앱의 다음 단계로 이동합니다.
//        SignIn()
//        startRoute = Screens.Signin.route
        NavHost(
            navController = navController, startDestination = startRoute
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
//        startRoute = Screens.Home.route
        BottomNavigationBar(startRoute)
    }


}
