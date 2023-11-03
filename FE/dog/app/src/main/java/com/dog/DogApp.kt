package com.dog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.dog.ui.navigation.AppNavigation
import com.dog.util.common.DataStoreManager

@Composable
fun DogApp() {
    val navController = rememberNavController()

    val context = LocalContext.current
    val store = DataStoreManager(context)

    val tokenText = store.getAccessToken.collectAsState(initial = "")
    // Token이 비어있는지 확인합니다.
    val isTokenEmpty = tokenText.value.isEmpty()

    AppNavigation(navController, isTokenEmpty)

}
