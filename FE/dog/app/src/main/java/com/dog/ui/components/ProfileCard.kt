package com.dog.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.dog.R

@Composable
fun ProfileCard() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher),
            contentDescription = null,
        )
        Column {
            Text("wfew")
            Text("fwefefwe")
        }
    }
}

data class Artist(
    val name: String,
    val lastSeenOnline: String
)
