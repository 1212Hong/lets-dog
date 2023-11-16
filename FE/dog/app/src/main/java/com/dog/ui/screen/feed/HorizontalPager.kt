package com.dog.ui.screen.feed

import android.widget.ImageView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dog.data.model.feed.BoardItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPager(feedItem: BoardItem) {
    val postImageUrls = feedItem.fileUrlLists
    val pagerState = rememberPagerState(pageCount = {
        postImageUrls.size
    })
    androidx.compose.foundation.pager.HorizontalPager(state = pagerState) { page ->
        val imageUrl = postImageUrls[page]
        ImageSection(imageUrl = imageUrl)
    }
}

@Composable
fun ImageSection(imageUrl: String) {
    val context = LocalContext.current
    val density = LocalDensity.current.density
    val imageWidth = (context.resources.displayMetrics.widthPixels - 32 * density).toInt()
    val imageHeight = (imageWidth * 0.4).toInt()
    val modifier = Modifier
        .fillMaxWidth()
        .height(imageHeight.dp)
    val postImageView = remember { ImageView(context) }
    loadImage(postImageView, imageUrl)
    AndroidView(
        factory = { postImageView },
        modifier = modifier
    )
}

@Composable
fun loadImage(imageView: ImageView, url: String) {
    Glide.with(imageView.context)
        .load(url)
        .into(imageView)
}


@Composable
fun LoadImage(url: String) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color.Gray)
    ) {
        AndroidView(
            factory = { context ->
                ImageView(context).apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
            },
            update = { view ->
                Glide.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions.circleCropTransform())
                    .into(view)
            }
        )
    }
}
