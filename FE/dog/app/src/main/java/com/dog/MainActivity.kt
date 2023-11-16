package com.dog;

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dog.ui.theme.DogTheme
import com.dog.util.common.DataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DogApp(dataStoreManager) { finish() }
                }

//                runBlocking {
////                     비동기 함수 호출을 runBlocking 블록 안에서 수행
//                    val response = HomeViewModel().loadBoarderNearData(127.11, 35.11, "test1")
//                    Log.d("model", response.body().toString())
//                }
//                ForumScreen()
            }
        }
    }
}


