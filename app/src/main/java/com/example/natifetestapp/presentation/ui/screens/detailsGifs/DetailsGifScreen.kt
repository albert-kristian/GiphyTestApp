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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.filter
import coil.imageLoader
import com.example.natifetestapp.presentation.ui.components.GifImage
import com.example.natifetestapp.presentation.ui.models.GifUIModel
import com.example.natifetestapp.presentation.ui.screens.detailsGifs.DetailsGifsViewModel.UiState
import com.example.natifetestapp.utils.extensions.isImageCached
import kotlinx.coroutines.flow.map

@Composable
fun DetailsGifsScreen() {
    val viewModel = hiltViewModel<DetailsGifsViewModel>()

    DetailsGiftsContent(uiState = viewModel.uiState.value)
}

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
            val imageLoader = LocalContext.current.imageLoader
            val gifs by remember {
                mutableStateOf(
                    uiState.gifs.map {
                        it.filter { gif ->
                            uiState.shouldShowNonCachedGifs || imageLoader.isImageCached(gif.id)
                        }
                    }
                )
            }
            DetailsGifsSuccessView(
                gifs = gifs.collectAsLazyPagingItems(),
                initialItemIndex = uiState.initialItemIndex
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DetailsGifsSuccessView(
    gifs: LazyPagingItems<GifUIModel>,
    initialItemIndex: Int
) {
    val state = rememberPagerState(
        initialPage = initialItemIndex
    ) { gifs.itemCount }

    LaunchedEffect(gifs.itemCount) {
        if (gifs.itemCount > initialItemIndex && state.currentPage != initialItemIndex) {
            state.scrollToPage(initialItemIndex)
        }
    }

    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        state = state
    ) { page ->
        gifs[page]?.let { gif ->
            GifImage(
                id = gif.id,
                gifTitle = gif.title,
                url = gif.thumbnailGifUrl,
                aspectRation = gif.thumbnailAspectRatio,
                onDeleteGifClicked = {},
                onGifPressed = {}
            )
        }
    }
}