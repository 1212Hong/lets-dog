package com.dog;

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dog.ui.navigation.BottomNavigationBar
import com.dog.ui.theme.DogTheme
import com.dog.util.common.StompManager

class MainActivity : ComponentActivity() {
    private val stompManager: StompManager by lazy {
        StompManager("ws://k9c205.p.ssafy.io:8000/ws-stomp")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    connectStomp()
                    BottomNavigationBar()

                }
            }
        }
    }
}


