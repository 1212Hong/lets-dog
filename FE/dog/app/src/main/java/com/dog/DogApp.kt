package com.dog

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.dog.data.viewmodel.user.UserViewModel
import com.dog.ui.navigation.AppNavigation
import com.dog.util.common.DataStoreManager


@Composable
fun DogApp() {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel()

    val context = LocalContext.current
    val store = DataStoreManager(context)

//    val tokenText = store.getAccessToken.collectAsState(initial = "")
//    // Token이 비어있는지 확인합니다.
//    val isTokenEmpty = tokenText.value.isEmpty()

    AppNavigation(navController, userViewModel, store)

}
