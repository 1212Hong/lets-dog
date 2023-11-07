package com.dog.data

sealed class Screens(val route: String) {
    object Home : Screens("home_screen")
    object Walking : Screens("Walking_screen")
    object Matching : Screens("Matching_screen")
    object ChatList : Screens("ChatList_screen")
    object Mypage : Screens("Mypage_screen")
    object Signin : Screens("Login_screen")
    object Signup : Screens("Signup_screen")
}
