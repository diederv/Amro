package com.amro.feature.movies.ui.list

import com.amro.core.model.Genre
import com.amro.core.model.Movie
import com.amro.feature.movies.domain.model.FilterAndSortParams
import com.amro.feature.movies.domain.model.SortOption
import com.amro.feature.movies.domain.model.SortOrder

sealed class TrendingMoviesUiState {
    data object Loading : TrendingMoviesUiState()
    data class Content(
        val movies: List<Movie>,
        val allGenres: List<Genre>,
        val selectedGenreId: Int?,
        val sortOption: SortOption,
        val sortOrder: SortOrder,
        val isRefreshing: Boolean = false,
    ) : TrendingMoviesUiState()
    data object Empty : TrendingMoviesUiState()
    data class Error(val message: String) : TrendingMoviesUiState()
}
