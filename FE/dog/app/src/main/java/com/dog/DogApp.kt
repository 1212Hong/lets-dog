package com.dog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.dog.ui.navigation.BottomNavigationBar
import com.dog.ui.screen.signin.LoginScreen
import com.dog.util.common.DataStoreManager

@Composable
fun DogApp(navController: NavController) {
//    val navController = rememberNavController()
//    AppNavigation(navController)

    val context = LocalContext.current
    val store = DataStoreManager(context)

    val tokenText = store.getAccessToken.collectAsState(initial = "test")
    // Token이 비어있는지 확인합니다.
    val isTokenEmpty = tokenText.value.isEmpty()

    // Token이 비어있으면 로그인 화면을 표시하고, 그렇지 않으면 BottomNavigationBar를 표시합니다.
    if (isTokenEmpty) {
        // Token이 비어있는 경우: 로그인 또는 회원 가입 화면을 표시
        // 이후 Token을 저장하고 앱의 다음 단계로 이동합니다.
//        SignIn()
        LoginScreen(navController)
    } else {
        // Token이 있는 경우: BottomNavigationBar를 표시
        BottomNavigationBar()
    }
}
