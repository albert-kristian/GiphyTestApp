package com.example.natifetestapp.ui.screens.mainGifs

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.natifetestapp.remote.services.gifs.repository.SearchRepository
import com.example.natifetestapp.remote.services.gifs.responses.GifResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainGifsViewModel @Inject constructor(
    private val searchRepository: SearchRepository
): ViewModel() {

    private val _uiState = mutableStateOf<UiState>(UiState.Loading)
    val uiState: State<UiState> = _uiState

    private val _searchQuery = mutableStateOf("lofi")
    val searchQuery: State<String> = _searchQuery

    init {
        viewModelScope.launch {
            snapshotFlow {
                _searchQuery.value
            }.catch {
                _uiState.value = UiState.Failure(message = it.message ?: "Something went wrong")
            }.collectLatest { query ->
                delay(500)
                getGifs(query)
            }
        }
    }

    fun onSearchQueryChange(searchQuery: String) {
        _searchQuery.value = searchQuery
    }

    private suspend fun getGifs(searchQuery: String) {
        searchRepository.getGifs(query = searchQuery).onSuccess { gifs ->
            if (gifs.isEmpty()) {
                _uiState.value = UiState.NoResults
            }  else {
                val pagingDataFlow = searchRepository.getGifsPaginated(
                    initialValues = gifs,
                    query = searchQuery
                ).cachedIn(viewModelScope)
                _uiState.value = UiState.Success(gifs = pagingDataFlow)
            }
        }.onFailure { exception ->
            _uiState.value = UiState.Failure(
                message = exception.message ?: "Something went wrong"
            )
        }
    }

    sealed class UiState {
        data object Loading: UiState()
        data object NoResults: UiState()
        data class Success(val gifs: Flow<PagingData<GifResponse>>): UiState()
        data class Failure(val message: String): UiState()
    }
}