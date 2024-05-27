package com.example.natifetestapp.presentation.ui.screens.detailsGifs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.natifetestapp.presentation.ui.components.GifImage
import com.example.natifetestapp.presentation.ui.screens.detailsGifs.DetailsGifsViewModel.UiState

@Composable
fun DetailsGifsScreen() {
    val viewModel = hiltViewModel<DetailsGifsViewModel>()

    DetailsGiftsContent(uiState = viewModel.uiState.value)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DetailsGiftsContent(
    uiState: UiState
) {
    when (uiState) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        is UiState.Success -> {
            val gifs = uiState.gifs.collectAsLazyPagingItems()
            val state = rememberPagerState(
                initialPage = uiState.initialItemIndex
            ) { gifs.itemCount }

            LaunchedEffect(key1 = gifs.itemCount) {
                if (gifs.itemCount > uiState.initialItemIndex && state.currentPage != uiState.initialItemIndex) {
                    state.scrollToPage(uiState.initialItemIndex)
                }
            }

            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = state
            ) { page ->
                gifs[page]?.let { gif ->
                    GifImage(
                        id = gif.id,
                        shouldShowNonCachedGifs = true, //TODO
                        gifTitle = gif.title,
                        url = gif.thumbnailGifUrl,
                        aspectRation = gif.thumbnailAspectRatio,
                        onDeleteGifClicked = {},
                        onGifPressed = {}
                    )
                }
            }
        }
    }
}