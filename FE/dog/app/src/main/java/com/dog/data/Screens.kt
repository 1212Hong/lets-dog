package com.dog.data

sealed class Screens(val route: String) {
    object DogApp : Screens("DogApp_screen")
    object Home : Screens("home_screen")
    object Walking : Screens("Walking_screen")
    object WalkingLog : Screens("WalkingLog_screen")
    object Chatting : Screens("Chatting_screen")
    object Mypage : Screens("Mypage_screen")
    object Signin : Screens("Login_screen")
    object Signup : Screens("Signup_screen")
}
