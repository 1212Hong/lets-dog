package com.dog;

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dog.ui.theme.DogTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DogApp()
//                    val navController = rememberNavController()
//                    val userViewModel: UserViewModel = viewModel()
//                    Log.d("userViewModel_inMain", userViewModel.toString())
//                    val context = LocalContext.current
//                    val store = DataStoreManager(context)
//                    Log.d("store_token_inMain", store.getAccessToken.toString())
//    val tokenText = store.getAccessToken.collectAsState(initial = "")
//    // Token이 비어있는지 확인합니다.
//    val isTokenEmpty = tokenText.value.isEmpty()

//                    AppNavigation(navController, userViewModel, store)
                }
            }
        }


    }

}


