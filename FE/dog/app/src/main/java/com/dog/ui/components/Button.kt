package com.dog.ui.components

import android.graphics.drawable.shapes.Shape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dog.ui.theme.DogTheme
import com.dog.ui.theme.Orange300

@Composable
fun MainButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape? = null,
    onClick: () -> Unit
) {
    DogTheme {

        Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange300,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
            )

        ) {
            Text(text = text)
        }

    }
}
