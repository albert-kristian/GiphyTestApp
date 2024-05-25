package com.example.natifetestapp.ui.screens.mainGifs

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natifetestapp.remote.services.gifs.repository.SearchRepository
import com.example.natifetestapp.remote.services.gifs.responses.GifsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainGifsViewModel @Inject constructor(
    private val searchRepository: SearchRepository
): ViewModel() {

    private val _uiState = mutableStateOf<UiState>(UiState.Loading)
    val uiState: State<UiState> = _uiState

    init {
        viewModelScope.launch {
            searchRepository.getGifs("lofi")
                .onSuccess { gifs ->
                    _uiState.value = UiState.Success(gifs)
                }.onFailure { exception ->
                    val message = exception.localizedMessage ?: "Something went wrong"
                    _uiState.value = UiState.Failure(message = message)
                }
        }
    }

    sealed class UiState {
        data object Loading: UiState()
        data class Success(val gifs: GifsResponse): UiState()
        data class Failure(val message: String): UiState()
    }
}