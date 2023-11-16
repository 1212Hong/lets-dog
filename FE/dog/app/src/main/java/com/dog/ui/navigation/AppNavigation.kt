package com.dog.ui.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dog.data.Screens
import com.dog.data.viewmodel.map.LocationTrackingViewModel
import com.dog.data.viewmodel.user.UserViewModel
import com.dog.ui.screen.signin.LoginScreen
import com.dog.ui.screen.signup.SignupScreen
import com.dog.util.common.DataStoreManager
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppNavigation(
    navController: NavHostController,
    userViewModel: UserViewModel,
    store: DataStoreManager,
    isLoggedIn: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    val locationTrackingViewModel: LocationTrackingViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        // 앱이 생성될 때 즉시 Firebase messaging token을 생성
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String> ->
                if (!task.isSuccessful) {
                    Log.w("FCM Log", "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }
                val token = task.result
                coroutineScope.launch(Dispatchers.IO) {
                    store.saveFCM(token)
                }
                Log.d("FCM Log", "Current token: $token")
            }
    }


    if (!isLoggedIn) {
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
        locationTrackingViewModel.getCurrentLocationAndUpdateUserInfo()
        BottomNavigationBar(Screens.Home.route, userViewModel)
    }
}
