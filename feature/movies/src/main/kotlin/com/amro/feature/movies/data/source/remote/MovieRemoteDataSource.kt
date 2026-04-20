package com.amro.feature.movies.data.source.remote

import com.amro.core.network.TmdbApiService
import com.amro.core.network.model.GenreDto
import com.amro.core.network.model.MovieDetailsDto
import com.amro.core.network.model.MovieDto

private const val TRENDING_PAGE_COUNT = 5

class MovieRemoteDataSource(private val apiService: TmdbApiService) {

    suspend fun getTrendingMovies(): List<MovieDto> =
        (1..TRENDING_PAGE_COUNT).flatMap { page ->
            apiService.getTrendingMovies(page = page).results
        }

    suspend fun getMovieDetails(movieId: Int): MovieDetailsDto =
        apiService.getMovieDetails(movieId)

    suspend fun getGenres(): List<GenreDto> =
        apiService.getGenres().genres
}
