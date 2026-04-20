package com.amro.feature.movies.domain.repository

import com.amro.core.model.Genre
import com.amro.core.model.Movie
import com.amro.core.model.MovieDetails
import com.amro.core.model.Result
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun observeTrendingMovies(): Flow<List<Movie>>
    fun observeGenres(): Flow<List<Genre>>
    suspend fun refreshTrendingMovies(): Result<Unit>
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetails>
}
