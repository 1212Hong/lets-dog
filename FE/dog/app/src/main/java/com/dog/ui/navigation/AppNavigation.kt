package com.dog.ui.navigation

import androidx.navigation.NavController
import com.dog.data.Screens

class Navigator(
    private val navController: NavController,
    private val isTokenEmpty: Boolean
) {


    fun navigateToSignUp() {
        navController.navigate(Screens.Signup.route) {
            popUpTo(Screens.Signup.route) {
                inclusive = true
            }
        }
    }
}
