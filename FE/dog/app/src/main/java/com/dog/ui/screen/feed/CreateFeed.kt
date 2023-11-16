package com.dog.ui.screen.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dog.data.Screens

@Composable
fun CreateFeed(
    navController: NavController
) {
    Icon(
        imageVector = Icons.Default.Add,
        contentDescription = null,
        modifier = Modifier
            .size(40.dp)
            .clickable {
                navController.navigate(Screens.PostFeed.route)
            }
    )
}
