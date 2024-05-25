package com.example.natifetestapp.ui.screens.mainGifs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.natifetestapp.remote.services.gifs.responses.GifsResponse
import com.example.natifetestapp.ui.screens.mainGifs.MainGifsViewModel.UiState.Failure
import com.example.natifetestapp.ui.screens.mainGifs.MainGifsViewModel.UiState.Loading
import com.example.natifetestapp.ui.screens.mainGifs.MainGifsViewModel.UiState.Success

@Composable
fun MainGifsScreen() {
    val viewModel = hiltViewModel<MainGifsViewModel>()

    MainGifsContent(
        uiState = viewModel.uiState.value
    )
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
            is Success -> MainGifsSuccessView(gifs = uiState.gifs)
        }
    }
}

@Composable
private fun MainGifsSuccessView(
    gifs: GifsResponse
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(200.dp)
    ) {
        items(gifs) { gif ->
            AsyncImage(
                modifier = Modifier.width(200.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(gif.images.original.url)
                    .crossfade(true)
                    .build(),
                contentDescription = gif.title,
                contentScale = ContentScale.FillWidth
            )
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

