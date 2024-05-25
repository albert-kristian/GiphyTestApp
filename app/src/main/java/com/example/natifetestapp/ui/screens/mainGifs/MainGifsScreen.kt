package com.example.natifetestapp.ui.screens.mainGifs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.natifetestapp.remote.services.gifs.responses.GifResponse
import com.example.natifetestapp.ui.screens.mainGifs.MainGifsViewModel.UiState.Failure
import com.example.natifetestapp.ui.screens.mainGifs.MainGifsViewModel.UiState.Loading
import com.example.natifetestapp.ui.screens.mainGifs.MainGifsViewModel.UiState.NoResults
import com.example.natifetestapp.ui.screens.mainGifs.MainGifsViewModel.UiState.Success
import kotlin.random.Random

@Composable
fun MainGifsScreen() {
    val viewModel = hiltViewModel<MainGifsViewModel>()

    MainGifsContent(uiState = viewModel.uiState.value)
}

@Composable
private fun MainGifsContent(
    uiState: MainGifsViewModel.UiState
) {
    Surface(
        modifier = Modifier
    ) {
        when (uiState) {
            is Failure -> MainGifsFailureView(message = uiState.message)
            is Loading -> MainGifsLoadingView()
            is NoResults -> MainGifsNoResultsView()
            is Success -> MainGifsSuccessView(gifs = uiState.gifs.collectAsLazyPagingItems())
        }
    }
}

@Composable
private fun MainGifsSuccessView(
    gifs: LazyPagingItems<GifResponse>
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(200.dp)
    ) {
        items(gifs.itemCount) { gifIndex ->
            gifs[gifIndex]?.let { gif ->
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(gif.images.original.url)
                        .crossfade(true)
                        .build(),
                    contentDescription = gif.title,
                    contentScale = ContentScale.FillWidth,
                    loading = {
                        Box(
                            modifier = Modifier
                                .background(Color(Random.nextInt(255)))
                                .fillMaxWidth(.5f)
                                .height(150.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 3.dp
                            )
                        }
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

