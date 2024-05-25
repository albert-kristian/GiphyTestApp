package com.example.natifetestapp.ui.screens.mainGifs

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.natifetestapp.remote.services.gifs.repository.SearchRepository
import com.example.natifetestapp.remote.services.gifs.responses.GifResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainGifsViewModel @Inject constructor(
    private val searchRepository: SearchRepository
): ViewModel() {

    private val _uiState = mutableStateOf<UiState>(UiState.Loading)
    val uiState: State<UiState> = _uiState

    private val query = "lofi"

    init {
        viewModelScope.launch {
            searchRepository.getGifs(query = query).onSuccess { gifs ->
                if (gifs.isEmpty()) {
                    _uiState.value = UiState.NoResults
                }  else {
                    val pagingDataFlow = searchRepository.getGifsPaginated(
                        initialValues = gifs,
                        query = query
                    ).cachedIn(viewModelScope)
                    _uiState.value = UiState.Success(gifs = pagingDataFlow)
                }
            }.onFailure { exception ->
                _uiState.value = UiState.Failure(
                    message = exception.message ?: "Something went wrong"
                )
            }
        }
    }

    sealed class UiState {
        data object Loading: UiState()
        data object NoResults: UiState()
        data class Success(val gifs: Flow<PagingData<GifResponse>>): UiState()
        data class Failure(val message: String): UiState()
    }
}