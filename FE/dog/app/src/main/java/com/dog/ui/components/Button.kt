package com.dog.ui.components

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dog.ui.theme.DogTheme
import com.dog.ui.theme.Orange300
import kotlinx.coroutines.runBlocking

@Composable
fun MainButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape? = null,
    onClick: suspend () -> Unit?
) {
    DogTheme {

        Button(
            onClick = { runBlocking { onClick() } },
            modifier = modifier
                .fillMaxWidth(0.4f)
                .height(50.dp),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange300,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
            ),

            ) {
            Text(
                text = text,
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 20.sp
            )
        }

    }
}
