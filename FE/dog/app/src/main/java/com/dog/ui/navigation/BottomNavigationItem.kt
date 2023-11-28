package com.dog.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.datastore.dataStore
import com.dog.data.Screens
import com.dog.data.viewmodel.user.MyPageViewModel

data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = "",
) {
    fun bottomNavigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Home",
                icon = Icons.Filled.Home,
                route = Screens.Home.route
            ),
            BottomNavigationItem(
                label = "Walking",
                icon = Icons.Filled.Search,
                route = Screens.Walking.route
            ),
            BottomNavigationItem(
                label = "Matching",
                icon = Icons.Filled.AccountCircle,
                route = Screens.Matching.route
            ),
            BottomNavigationItem(
                label = "Chatting",
                icon = Icons.Filled.AccountCircle,
                route = Screens.ChatList.route
            ),
            BottomNavigationItem(
                label = "Mypage",
                icon = Icons.Filled.AccountCircle,
                route = Screens.Mypage.route,
            ),
        )
    }
}
