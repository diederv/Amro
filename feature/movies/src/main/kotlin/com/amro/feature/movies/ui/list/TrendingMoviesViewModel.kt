package com.amro.feature.movies.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amro.core.model.Result
import com.amro.feature.movies.domain.model.FilterAndSortParams
import com.amro.feature.movies.domain.model.SortOption
import com.amro.feature.movies.domain.model.SortOrder
import com.amro.feature.movies.domain.repository.MovieRepository
import com.amro.feature.movies.domain.usecase.FilterAndSortMoviesUseCase
import com.amro.feature.movies.domain.usecase.GetTrendingMoviesUseCase
import com.amro.feature.movies.domain.usecase.ObserveGenresUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrendingMoviesViewModel(
    private val getTrendingMovies: GetTrendingMoviesUseCase,
    private val observeGenres: ObserveGenresUseCase,
    private val filterAndSort: FilterAndSortMoviesUseCase,
    private val repository: MovieRepository,
) : ViewModel() {

    private val filterParams = MutableStateFlow(FilterAndSortParams())
    private val refreshError = MutableStateFlow<Throwable?>(null)

    val uiState = combine(
        getTrendingMovies(),
        observeGenres(),
        filterParams,
        refreshError,
    ) { movies, genres, params, error ->
        when {
            movies.isEmpty() && error != null ->
                TrendingMoviesUiState.Error(error.message ?: "Failed to load movies")

            movies.isEmpty() ->
                TrendingMoviesUiState.Loading

            else -> {
                val filtered = filterAndSort(movies, params)
                if (filtered.isEmpty()) TrendingMoviesUiState.Empty
                else TrendingMoviesUiState.Content(
                    movies = filtered,
                    allGenres = genres,
                    selectedGenreId = params.genreId,
                    sortOption = params.sortOption,
                    sortOrder = params.sortOrder,
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TrendingMoviesUiState.Loading,
    )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            refreshError.value = null
            val result = repository.refreshTrendingMovies()
            if (result is Result.Error) refreshError.value = result.exception
        }
    }

    fun setGenreFilter(genreId: Int?) {
        filterParams.value = filterParams.value.copy(genreId = genreId)
    }

    fun setSortOption(option: SortOption) {
        filterParams.value = filterParams.value.copy(sortOption = option)
    }

    fun setSortOrder(order: SortOrder) {
        filterParams.value = filterParams.value.copy(sortOrder = order)
    }
}
