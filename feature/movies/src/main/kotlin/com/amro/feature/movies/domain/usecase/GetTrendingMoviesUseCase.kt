package com.amro.feature.movies.domain.usecase

import com.amro.core.model.Movie
import com.amro.feature.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetTrendingMoviesUseCase(private val repository: MovieRepository) {
    operator fun invoke(): Flow<List<Movie>> = repository.observeTrendingMovies()
}
