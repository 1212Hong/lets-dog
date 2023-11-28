package com.dog.util.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun ImageLoader(imageUrl: String? = "1", modifier: Modifier = Modifier, type: String = "") {
    val glideUrl = GlideUrl(
        imageUrl, LazyHeaders.Builder().build()
    )

    when (type) {
        "thumbnail" -> {
            GlideImage(
                model = glideUrl,
                contentDescription = "이미지 설명",
                modifier = modifier
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                failure = placeholder(R.drawable.background)
            )
        }

        "grid" -> {
            GlideImage(
                model = glideUrl,
                contentDescription = "이미지 설명",
                modifier = modifier
                    .padding(5.dp)
                    .fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                loading = placeholder(R.drawable.background),
                failure = placeholder(R.drawable.background)
            )
        }

        "chat" -> {
            GlideImage(
                model = glideUrl,
                contentDescription = "이미지 설명",
                modifier = modifier
                    .fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                loading = placeholder(R.drawable.background),
                failure = placeholder(R.drawable.background)
            )
        }

        "profile" -> {
            GlideImage(
                model = glideUrl,
                contentDescription = "이미지 설명",
                contentScale = ContentScale.FillBounds,
                loading = placeholder(R.drawable.background),
                failure = placeholder(R.drawable.background)
            )
        }

        else -> {
            Box(modifier = Modifier.fillMaxSize()) {
                GlideImage(
                    model = glideUrl,
                    contentDescription = "이미지 설명",
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                    alpha = 0.4f,
                    failure = placeholder(R.drawable.background)
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
}

