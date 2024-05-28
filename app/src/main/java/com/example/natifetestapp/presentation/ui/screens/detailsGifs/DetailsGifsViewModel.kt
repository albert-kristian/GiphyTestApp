package com.example.natifetestapp.presentation.ui.screens.detailsGifs

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.natifetestapp.domain.useCases.GetLoadedGifsUseCase
import com.example.natifetestapp.presentation.ui.mapping.toUIModel
import com.example.natifetestapp.presentation.ui.models.GifUIModel
import com.example.natifetestapp.utils.NetworkConnectionHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsGifsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getLoadedGifsUseCase: GetLoadedGifsUseCase,
    connectionHelper: NetworkConnectionHelper
): ViewModel() {

    private val initialItemIndex: Int = savedStateHandle.get<Int>("initialIndex") ?: 0

    private val _uiState = mutableStateOf<UiState>(UiState.Loading)
    val uiState: State<UiState> = _uiState

    init {
        val gifs = getLoadedGifsUseCase.execute()
            .filter { domainModel ->
                domainModel.shouldBeShown
            }.map { domainModel ->
                domainModel.toUIModel()
            }
        _uiState.value = UiState.Success(
            gifs = gifs,
            initialItemIndex = initialItemIndex,
            shouldShowNonCachedGifs = connectionHelper.isOnline
        )
    }

    sealed class UiState {
        data object Loading: UiState()
        data class Success(
            val gifs: List<GifUIModel>,
            val initialItemIndex: Int,
            val shouldShowNonCachedGifs: Boolean,
        ): UiState()
    }
}