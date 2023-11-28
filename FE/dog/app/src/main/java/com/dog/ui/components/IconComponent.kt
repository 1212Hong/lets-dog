package com.dog.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.dog.util.common.ImageLoader

@Composable
fun IconComponentImageVector(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    size: Dp
) {
    Icon(
        imageVector = icon,
        contentDescription = "",
        modifier = Modifier.size(size),
        tint = tint
    )
}

@Composable
fun IconComponentDrawable(
    @DrawableRes icon: Int? = null,
    imageUrl: String? = null, // Add imageUrl parameter
    size: Dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
    ) {
        if (imageUrl != null) {
            // If imageUrl is provided, use the ImageLoader with the profile type
            ImageLoader(imageUrl, modifier = Modifier.fillMaxSize(), type = "profile")
        } else {
            // If no imageUrl is provided, use the drawable icon
            if (icon != null) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.Black
                )
            } else Text(text = "no icon or image")
        }
    }
}
