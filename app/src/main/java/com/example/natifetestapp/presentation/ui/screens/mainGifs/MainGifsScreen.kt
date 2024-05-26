package com.example.natifetestapp.presentation.ui.screens.mainGifs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.natifetestapp.presentation.ui.components.topBars.SearchTopBar
import com.example.natifetestapp.presentation.ui.models.GifUIModel
import com.example.natifetestapp.presentation.ui.screens.mainGifs.MainGifsViewModel.UiState.Failure
import com.example.natifetestapp.presentation.ui.screens.mainGifs.MainGifsViewModel.UiState.Loading
import com.example.natifetestapp.presentation.ui.screens.mainGifs.MainGifsViewModel.UiState.NoResults
import com.example.natifetestapp.presentation.ui.screens.mainGifs.MainGifsViewModel.UiState.Success

@Composable
fun MainGifsScreen() {
    val viewModel = hiltViewModel<MainGifsViewModel>()

    MainGifsContent(
        searchQuery = viewModel.searchQuery.value,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onDeleteGifClicked = viewModel::onDeleteGif,
        uiState = viewModel.uiState.value
    )
}

@Composable
private fun MainGifsContent(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onDeleteGifClicked: (id: String) -> Unit,
    uiState: MainGifsViewModel.UiState
) {
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            SearchTopBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues)
        ) {
            when (uiState) {
                is Failure -> MainGifsFailureView(message = uiState.message)
                is Loading -> MainGifsLoadingView()
                is NoResults -> MainGifsNoResultsView()
                is Success -> MainGifsSuccessView(
                    gifs = uiState.gifs.collectAsLazyPagingItems(),
                    onDeleteGifClicked = onDeleteGifClicked
                )
            }
        }
    }
}

// region Views

@Composable
private fun MainGifsSuccessView(
    gifs: LazyPagingItems<GifUIModel>,
    onDeleteGifClicked: (id: String) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(200.dp)
    ) {
        items(gifs.itemCount) { gifIndex ->
            gifs[gifIndex]?.let { gif ->
                GifImage(
                    id = gif.id,
                    gifTitle = gif.title,
                    thumbnailUrl = gif.thumbnailGifUrl,
                    aspectRation = gif.thumbnailAspectRatio,
                    onDeleteGifClicked = onDeleteGifClicked
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
                is LoadState.NotLoading -> { }
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

// region Components

@Composable
fun GifImage(
    id: String,
    gifTitle: String,
    thumbnailUrl: String,
    aspectRation: Float,
    onDeleteGifClicked: (id: String) -> Unit
) {
    var showDeleteButton by remember { mutableStateOf(false) }
    var wasDeleted by rememberSaveable { mutableStateOf(false) }

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
                    .data(thumbnailUrl)
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

// endregion Components