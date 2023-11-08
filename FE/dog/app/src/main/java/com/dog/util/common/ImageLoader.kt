package com.dog.util.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.dog.R


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageLoader(imageUrl: String, modifier: Modifier = Modifier, type:String = "") {
    val JWT_TOKEN =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMDEiLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNzMwODU1NjU3fQ.A-LpWLT_ZjyAjB1_kFwk25SoiR1vCIIA_ikg6_600RA"

    val glideUrl = GlideUrl(
        imageUrl, LazyHeaders.Builder()
            .addHeader("Authorization", "Bearer $JWT_TOKEN")
            .build()
    )
    if (type == "thumbnail") {
        GlideImage(
            model = glideUrl,
            contentDescription = "이미지 설명",
            modifier = modifier
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            GlideImage(
                model = glideUrl,
                contentDescription = "이미지 설명",
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                alpha = 0.4f
            )
            GlideImage(
                model = glideUrl,
                contentDescription = "이미지 설명",
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize(),
                contentScale = ContentScale.Fit,
                loading = placeholder(R.drawable.background),
                failure = placeholder(R.drawable.background)
            )
        }

    }

}

