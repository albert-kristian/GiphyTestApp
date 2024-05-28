package com.example.natifetestapp.presentation.ui.screens.detailsGifs

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.natifetestapp.domain.useCases.GetGifsPagingFlowUseCase
import com.example.natifetestapp.presentation.ui.mapping.toUIModel
import com.example.natifetestapp.presentation.ui.models.GifUIModel
import com.example.natifetestapp.utils.NetworkConnectionHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsGifsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getGifsPagingFlowUseCase: GetGifsPagingFlowUseCase,
    private val connectionHelper: NetworkConnectionHelper
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
                pagingData.filter { domainModel ->
                    domainModel.shouldBeShown
                }.map { domainModel ->
                    domainModel.toUIModel()
                }
            }.cachedIn(viewModelScope)
            _uiState.value = UiState.Success(
                gifs = pagingDataFlow,
                initialItemIndex = initialItemIndex,
                shouldShowNonCachedGifs = connectionHelper.isOnline
            )
        }
    }

    sealed class UiState {
        data object Loading: UiState()
        data class Success(
            val gifs: Flow<PagingData<GifUIModel>>,
            val initialItemIndex: Int,
            val shouldShowNonCachedGifs: Boolean,
        ): UiState()
    }
}