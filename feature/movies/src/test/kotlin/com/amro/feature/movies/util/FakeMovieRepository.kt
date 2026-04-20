package com.amro.feature.movies.util

import com.amro.core.model.Genre
import com.amro.core.model.Movie
import com.amro.core.model.MovieDetails
import com.amro.core.model.Result
import com.amro.feature.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeMovieRepository(
    movies: List<Movie> = emptyList(),
    genres: List<Genre> = emptyList(),
    private val refreshResult: Result<Unit> = Result.Success(Unit),
    private val detailsResult: Result<MovieDetails> = Result.Error(NotImplementedError()),
) : MovieRepository {

    private val moviesFlow = MutableStateFlow(movies)
    private val genresFlow = MutableStateFlow(genres)

    fun setMovies(movies: List<Movie>) { moviesFlow.value = movies }
    fun setGenres(genres: List<Genre>) { genresFlow.value = genres }

    override fun observeTrendingMovies(): Flow<List<Movie>> = moviesFlow
    override fun observeGenres(): Flow<List<Genre>> = genresFlow
    override suspend fun refreshTrendingMovies(): Result<Unit> = refreshResult
    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetails> = detailsResult
}
