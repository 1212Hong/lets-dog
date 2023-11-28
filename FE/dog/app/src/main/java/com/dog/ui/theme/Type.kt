package com.dog.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dog.R

// Set of Material typography styles to start with
val jalnan = FontFamily(
    Font(R.font.jalnan, FontWeight.Thin, FontStyle.Normal),
    Font(R.font.jalnan, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.jalnan, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.jalnan, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.jalnan_otf, FontWeight.Thin, FontStyle.Normal),
    Font(R.font.jalnan_otf, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.jalnan_otf, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.jalnan_otf, FontWeight.Bold, FontStyle.Normal),
)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = jalnan,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
