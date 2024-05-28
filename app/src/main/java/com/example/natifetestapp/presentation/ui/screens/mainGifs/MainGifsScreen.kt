package com.example.natifetestapp.presentation.ui.screens.mainGifs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.filter
import coil.imageLoader
import com.example.natifetestapp.presentation.ui.components.GifImage
import com.example.natifetestapp.presentation.ui.components.topBars.SearchTopBar
import com.example.natifetestapp.presentation.ui.models.GifUIModel
import com.example.natifetestapp.presentation.ui.screens.mainGifs.MainGifsViewModel.UiState.Failure
import com.example.natifetestapp.presentation.ui.screens.mainGifs.MainGifsViewModel.UiState.Loading
import com.example.natifetestapp.presentation.ui.screens.mainGifs.MainGifsViewModel.UiState.NoResults
import com.example.natifetestapp.presentation.ui.screens.mainGifs.MainGifsViewModel.UiState.Success
import com.example.natifetestapp.utils.extensions.isImageCached
import kotlinx.coroutines.flow.map

@Composable
fun MainGifsScreen(
    onGifPressed: (Int) -> Unit
) {
    val viewModel = hiltViewModel<MainGifsViewModel>()

    MainGifsContent(
        searchQuery = viewModel.searchQuery.value,
        isSearchVisible = viewModel.isSearchVisible.value,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onDeleteGifClicked = viewModel::onDeleteGif,
        onGifPressed = onGifPressed,
        uiState = viewModel.uiState.value
    )
}

@Composable
private fun MainGifsContent(
    searchQuery: String,
    isSearchVisible: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onDeleteGifClicked: (id: String) -> Unit,
    onGifPressed: (Int) -> Unit,
    uiState: MainGifsViewModel.UiState
) {
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            if (isSearchVisible) {
                SearchTopBar(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange
                )
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues)
        ) {
            when (uiState) {
                is Failure -> MainGifsFailureView(message = uiState.message)
                is Loading -> MainGifsLoadingView()
                is NoResults -> MainGifsNoResultsView()
                is Success -> {
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
                    MainGifsSuccessView(
                        gifs = gifs.collectAsLazyPagingItems(),
                        onDeleteGifClicked = onDeleteGifClicked,
                        onGifPressed = onGifPressed
                    )
                }
            }
        }
    }
}

// region Views

@Composable
private fun MainGifsSuccessView(
    gifs: LazyPagingItems<GifUIModel>,
    onDeleteGifClicked: (id: String) -> Unit,
    onGifPressed: (Int) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(200.dp)
    ) {
        items(gifs.itemCount) { gifIndex ->
            gifs[gifIndex]?.let { gif ->
                GifImage(
                    id = gif.id,
                    gifTitle = gif.title,
                    url = gif.thumbnailGifUrl,
                    aspectRation = gif.thumbnailAspectRatio,
                    onDeleteGifClicked = onDeleteGifClicked,
                    onGifPressed = {
                        onGifPressed(gifIndex)
                    }
                )
            }
        }
        gifs.loadState.let { loadState ->
            when (loadState.append) {
                is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 3.dp
                            )
                        }
                    }
                }
                is LoadState.Error -> {
                    item {
                        Text(text = "Failed to load next Items")
                    }
                }
                is LoadState.NotLoading -> {}
            }
        }
    }
}

@Composable
private fun MainGifsFailureView(
    message: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message)
    }
}

@Composable
private fun MainGifsLoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun MainGifsNoResultsView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "No results")
    }
}

// endregion Views