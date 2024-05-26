package com.example.natifetestapp.presentation.ui.components.topBars

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = query,
        onValueChange = onQueryChange,
        maxLines = 1,
        trailingIcon = {
            AnimatedVisibility(
                visible = query.isBlank(),
                enter = slideInHorizontally { it / 2 },
                exit = slideOutHorizontally { it / 2 }
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
            }
        },
        placeholder = {
            AnimatedVisibility(
                visible = query.isBlank(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(text = "Search gifs by title")
            }
        }
    )
}