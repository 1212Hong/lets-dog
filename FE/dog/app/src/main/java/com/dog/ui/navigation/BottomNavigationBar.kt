package com.dog.ui.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.dog.data.viewmodel.ImageUploadViewModel
import com.dog.data.viewmodel.chat.ChatViewModel
import com.dog.data.viewmodel.feed.HomeViewModel
import com.dog.data.viewmodel.map.LocationTrackingHistoryViewModel
import com.dog.data.viewmodel.map.LocationTrackingViewModel
import com.dog.data.viewmodel.user.MyPageViewModel
import com.dog.data.viewmodel.user.UserViewModel
import com.dog.ui.screen.HomeScreen
import com.dog.ui.screen.MatchingScreen
import com.dog.ui.screen.PostFeedScreen
import com.dog.ui.screen.chat.ChatListScreen
import com.dog.ui.screen.chat.ChattingScreen
import com.dog.ui.screen.chat.CreateChatting
import com.dog.ui.screen.profile.EditDogProfileScreen
import com.dog.ui.screen.profile.EditUserProfileScreen
import com.dog.ui.screen.profile.MypageScreen
import com.dog.ui.screen.profile.RegisterDogScreen
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
    val myPageViewModel: MyPageViewModel = hiltViewModel()
    val imageUploadViewModel: ImageUploadViewModel = hiltViewModel()
    val chatViewModel: ChatViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()

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
    }

    shouldShowBottomBar.value = when (navBackStackEntry?.destination?.route) {
        "profile/{userNickname}" -> false
        "chatroom/{roomId}" -> false
        "edit_profile", "edit_dog", "RegisterDog_screen" -> false
        else -> true
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
                LaunchedEffect(Unit) {
                    homeViewModel.loadBoarderNearData(null, null)
                }
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
                    navController,
                    chatViewModel
                )
            }
            composable(Screens.Mypage.route) {
                myPageViewModel.getUser(null)
                MypageScreen(
                    navController, myPageViewModel, userNickname = null
                )
            }
            composable(Screens.PostFeed.route) {
                PostFeedScreen(
                    navController,
                )
            }
            composable(
                route = "chatroom/{roomId}",
                arguments = listOf(navArgument("roomId") { type = NavType.LongType })
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getLong("roomId") ?: -1
                Log.d("roomId", roomId.toString())
                ChattingScreen(navController, roomId, userViewModel, chatViewModel)
            }
            composable("newChatting") {
                CreateChatting(
                    navController,
                    chatViewModel,
                    userViewModel
                )
            }

            composable("profile/{userNickname}") { backStackEntry ->
                var userNickname = backStackEntry.arguments?.getString("userNickname")
                Log.i("프로필 이동", "${userNickname}")
                myPageViewModel.updateUserNickname(userNickname!!)
                MypageScreen(navController, myPageViewModel, userNickname)
            }

            composable("edit_profile") {
                EditUserProfileScreen(navController, myPageViewModel, imageUploadViewModel)
            }

            composable("edit_dog") {
                EditDogProfileScreen(navController, myPageViewModel, imageUploadViewModel)
            }

            composable(Screens.RegisterDog.route) {
                RegisterDogScreen(navController, imageUploadViewModel, myPageViewModel)
            }

        }
    }
}
