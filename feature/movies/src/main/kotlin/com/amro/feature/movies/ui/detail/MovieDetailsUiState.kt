package com.amro.feature.movies.ui.detail

import com.amro.core.model.MovieDetails

sealed class MovieDetailsUiState {
    data object Loading : MovieDetailsUiState()
    data class Content(val movieDetails: MovieDetails) : MovieDetailsUiState()
    data class Error(val message: String) : MovieDetailsUiState()
}
