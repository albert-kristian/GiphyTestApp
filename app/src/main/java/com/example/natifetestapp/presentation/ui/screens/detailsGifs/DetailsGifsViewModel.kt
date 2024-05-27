package com.example.natifetestapp.presentation.ui.screens.detailsGifs

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.natifetestapp.domain.useCases.GetGifsPagingFlowUseCase
import com.example.natifetestapp.presentation.ui.mapping.toUIModel
import com.example.natifetestapp.presentation.ui.models.GifUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsGifsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getGifsPagingFlowUseCase: GetGifsPagingFlowUseCase
): ViewModel() {

    private val initialItemIndex: Int = savedStateHandle.get<Int>("initialIndex") ?: 0

    private val _uiState = mutableStateOf<UiState>(UiState.Loading)
    val uiState: State<UiState> = _uiState

    init {
        viewModelScope.launch {
            val pagingDataFlow = getGifsPagingFlowUseCase.execute( // Paging flow should be already created
                initialValues = listOf(),
                query = ""
            ).map { pagingData ->
                pagingData.map { domainModel ->
                    domainModel.toUIModel()
                }
            }.cachedIn(viewModelScope)
            _uiState.value = UiState.Success(
                gifs = pagingDataFlow,
                initialItemIndex = initialItemIndex
            )
        }
    }

    sealed class UiState {
        data object Loading: UiState()
        data class Success(
            val gifs: Flow<PagingData<GifUIModel>>,
            val initialItemIndex: Int
        ): UiState()
    }
}