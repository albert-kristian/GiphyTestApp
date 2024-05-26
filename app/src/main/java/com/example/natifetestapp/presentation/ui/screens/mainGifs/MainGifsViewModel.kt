package com.example.natifetestapp.presentation.ui.screens.mainGifs

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.natifetestapp.domain.models.GifDomain
import com.example.natifetestapp.domain.useCases.GetGifsPagingFlowUseCase
import com.example.natifetestapp.domain.useCases.GetGifsUseCase
import com.example.natifetestapp.presentation.ui.mapping.toUIModel
import com.example.natifetestapp.presentation.ui.models.GifUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class MainGifsViewModel @Inject constructor(
    private val getGifsUseCase: GetGifsUseCase,
    private val getGifsPagingFlowUseCase: GetGifsPagingFlowUseCase
): ViewModel() {

    private val _uiState = mutableStateOf<UiState>(UiState.Loading)
    val uiState: State<UiState> = _uiState

    private val _searchQuery = mutableStateOf("lofi")
    val searchQuery: State<String> = _searchQuery

    init {
        viewModelScope.launch {
            getGifs(_searchQuery.value)
            listenToSearchQueryChanges()
        }
    }

    fun onSearchQueryChange(searchQuery: String) {
        _searchQuery.value = searchQuery
    }

    private suspend fun listenToSearchQueryChanges() {
        snapshotFlow {
            _searchQuery.value
        }.catch {
            _uiState.value = UiState.Failure(message = it.message ?: "Something went wrong")
        }.drop(
            count = 1
        ).debounce(
            timeoutMillis = 500
        ).collectLatest { query ->
            getGifs(query)
        }
    }

    private suspend fun getGifs(searchQuery: String) {
        getGifsUseCase.execute(query = searchQuery).onSuccess { gifs ->
            if (gifs.isEmpty()) {
                _uiState.value = UiState.NoResults
            } else {
                getGifsPagingFlow(gifs = gifs, searchQuery = searchQuery)
            }
        }.onFailure { exception ->
            _uiState.value = UiState.Failure(
                message = exception.message ?: "Something went wrong"
            )
        }
    }

    private suspend fun getGifsPagingFlow(
        gifs: List<GifDomain>,
        searchQuery: String
    ) {
        val pagingDataFlow = getGifsPagingFlowUseCase.execute(
            initialValues = gifs,
            query = searchQuery
        ).map { pagingData ->
            pagingData.map { domainModel ->
                domainModel.toUIModel()
            }
        }.cachedIn(viewModelScope)
        _uiState.value = UiState.Success(gifs = pagingDataFlow)
    }

    sealed class UiState {
        data object Loading: UiState()
        data object NoResults: UiState()
        data class Success(val gifs: Flow<PagingData<GifUIModel>>): UiState()
        data class Failure(val message: String): UiState()
    }
}