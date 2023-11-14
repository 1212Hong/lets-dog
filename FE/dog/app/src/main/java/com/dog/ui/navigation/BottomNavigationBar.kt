package com.dog.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dog.data.Screens
import com.dog.data.viewmodel.map.LocationTrackingHistoryViewModel
import com.dog.data.viewmodel.map.LocationTrackingViewModel
import com.dog.data.viewmodel.user.UserViewModel
import com.dog.ui.screen.HomeScreen
import com.dog.ui.screen.MatchingScreen
import com.dog.ui.screen.MypageScreen
import com.dog.ui.screen.chat.ChatListScreen
import com.dog.ui.screen.chat.ChattingScreen
import com.dog.ui.screen.walking.WalkingHistoryScreen
import com.dog.ui.screen.walking.WalkingScreen


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomNavigationBar(startRoute: String, userViewModel: UserViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var shouldShowBottomBar = rememberSaveable { (mutableStateOf(true)) }
    val locationTrackingViewModel: LocationTrackingViewModel = hiltViewModel()
    val locationTrackingHistoryViewModel: LocationTrackingHistoryViewModel = hiltViewModel()

    when (navBackStackEntry?.destination?.route) {
        // "roomId" 값이 1이 아닌 경우에 대한 조건을 추가합니다.
        is String -> {
            val route = navBackStackEntry!!.destination.route
            if (route != null) {
                if (route.startsWith("chatroom/")) {
                    val roomId = route.removePrefix("chatroom/").toIntOrNull()
                    shouldShowBottomBar.value = roomId == 1
                } else {
                    shouldShowBottomBar.value = true
                }
            }
        }

        "Walking_screen" -> {
            shouldShowBottomBar.value = false
        }

        else -> {
            shouldShowBottomBar.value = true
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (shouldShowBottomBar.value)
                NavigationBar {
                    BottomNavigationItem().bottomNavigationItems()
                        .forEachIndexed { _, navigationItem ->
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
        NavHost(
            navController = navController,
            startDestination = startRoute,
        ) {
            composable(Screens.Home.route) {
                HomeScreen(
                    navController
                )
            }
            composable(Screens.Walking.route) {
                WalkingScreen(
                    navController,
                    locationTrackingViewModel
                )
            }
            composable(Screens.WalkingHistory.route) {
                WalkingHistoryScreen(
                    navController, locationTrackingViewModel,
                    locationTrackingHistoryViewModel
                )
            }

            composable(Screens.Matching.route) {
                MatchingScreen(
                    navController
                )
            }
            composable(Screens.ChatList.route) {
                ChatListScreen(
                    navController
                )
            }
            composable(Screens.Mypage.route) {
                MypageScreen(
                    navController,
                    userViewModel
                )
            }
            composable(
                route = "chatroom/{roomId}",
                arguments = listOf(navArgument("roomId") { type = NavType.IntType })
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getInt("roomId") ?: -1
                ChattingScreen(navController, roomId, userViewModel)
            }
        }
    }
}
