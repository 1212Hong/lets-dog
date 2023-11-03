package com.dog.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                BottomNavigationItem().bottomNavigationItems().forEachIndexed { _, navigationItem ->
                    NavigationBarItem(
                        selected = navigationItem.route == currentDestination?.route,
                        label = {
                            Text(navigationItem.label)
                        },
                        icon = {
                            Icon(
                                navigationItem.icon,
                                contentDescription = navigationItem.label
                            )
                        },
                        onClick = {
                            navController.navigate(navigationItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) {
//            paddingValues ->
//        NavHost(
//            navController = navController,
//            startDestination = Screens.Home.route,
//            modifier = Modifier.padding(paddingValues = paddingValues)
//        ) {
//            composable(Screens.Home.route) {
//                HomeScreen(
//                    navController
//                )
//            }
//            composable(Screens.Walking.route) {
//                WalkingScreen(
//                    navController
//                )
//            }
//            composable(Screens.WalkingLog.route) {
//                WalkingLogScreen(
//                    navController
//                )
//            }
//            composable(Screens.Chatting.route) {
//                ChattingScreen(
//                    navController
//                )
//            }
//            composable(Screens.Mypage.route) {
//                MypageScreen(
//                    navController
//                )
//            }
//            composable(Screens.Signup.route) {
//                SignUp(navController)
//            }
//            composable(Screens.Signin.route) {
//                LoginScreen(navController)
//            }
//        }
    }
}
