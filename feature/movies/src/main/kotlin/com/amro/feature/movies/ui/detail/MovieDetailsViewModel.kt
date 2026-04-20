package com.amro.feature.movies.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amro.core.model.Result
import com.amro.feature.movies.domain.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getMovieDetails: GetMovieDetailsUseCase,
) : ViewModel() {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

    private val _uiState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    init {
        loadDetails()
    }

    fun retry() = loadDetails()

    private fun loadDetails() {
        viewModelScope.launch {
            _uiState.value = MovieDetailsUiState.Loading
            _uiState.value = when (val result = getMovieDetails(movieId)) {
                is Result.Success -> MovieDetailsUiState.Content(result.data)
                is Result.Error -> MovieDetailsUiState.Error(
                    result.exception.message ?: "Failed to load movie details"
                )
            }
        }
    }
}
