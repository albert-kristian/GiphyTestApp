package com.example.natifetestapp.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.SubcomposeAsyncImage
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest

@OptIn(ExperimentalCoilApi::class)
@Composable
fun GifImage(
    id: String,
    shouldShowNonCachedGifs: Boolean,
    gifTitle: String,
    url: String,
    aspectRation: Float,
    onDeleteGifClicked: (id: String) -> Unit,
    onGifPressed: () -> Unit
) {
    var showDeleteButton by remember { mutableStateOf(false) }
    var wasDeleted by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    @Composable
    fun isImageCached(): Boolean {
        return try {
            context.imageLoader.diskCache?.openSnapshot(id)?.use {
                it.data.toFile().exists()
            } ?: false
        }
        catch (t : Throwable) {
            false
        }
    }

    if ((isImageCached() || shouldShowNonCachedGifs)) {
        AnimatedVisibility(
            visible = !wasDeleted,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRation)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                showDeleteButton = !showDeleteButton
                            },
                            onTap = {
                                onGifPressed()
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(aspectRation),
                    model = ImageRequest.Builder(LocalContext.current)
                        .diskCachePolicy(policy = CachePolicy.ENABLED)
                        .diskCacheKey(id)
                        .data(url)
                        .crossfade(true)
                        .build(),
                    contentDescription = gifTitle,
                    contentScale = ContentScale.FillWidth,
                    loading = {
                        Surface(
                            modifier = Modifier
                                .size(24.dp)
                                .padding(80.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 3.dp
                            )
                        }
                    },
                    error = {
                        Text(text = "Failed to load Gif")
                    }
                )
                if (showDeleteButton) {
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .fillMaxWidth(.3f)
                            .aspectRatio(1f)
                            .padding(8.dp),
                        onClick = {
                            onDeleteGifClicked(id)
                            wasDeleted = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}