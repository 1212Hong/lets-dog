package com.dog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dog.data.Screens
import com.dog.ui.screen.HomeScreen
import com.dog.ui.screen.signin.LoginScreen
import com.dog.ui.screen.signup.SignUp

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.Signin.route,
    ) {
        composable(route = Screens.Home.route) {
            HomeScreen(
                navController
            )
        }
        composable(route = Screens.Signup.route) {
            SignUp(navController)
        }
        composable(route = Screens.Signin.route) {
            LoginScreen(navController)
        }
    }
}
