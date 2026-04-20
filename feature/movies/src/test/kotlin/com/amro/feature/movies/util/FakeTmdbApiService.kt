package com.amro.feature.movies.util

import com.amro.core.network.TmdbApiService
import com.amro.core.network.model.GenreDto
import com.amro.core.network.model.GenreListResponseDto
import com.amro.core.network.model.MovieDetailsDto
import com.amro.core.network.model.MovieDto
import com.amro.core.network.model.TrendingResponseDto

class FakeTmdbApiService(
    var movieDtos: List<MovieDto> = emptyList(),
    var genreDtos: List<GenreDto> = emptyList(),
    var movieDetailsDto: MovieDetailsDto? = null,
    var shouldThrow: Throwable? = null,
) : TmdbApiService {

    override suspend fun getTrendingMovies(page: Int, language: String): TrendingResponseDto {
        shouldThrow?.let { throw it }
        return TrendingResponseDto(page = page, results = movieDtos, totalPages = 1, totalResults = movieDtos.size)
    }

    override suspend fun getMovieDetails(movieId: Int, language: String): MovieDetailsDto {
        shouldThrow?.let { throw it }
        return movieDetailsDto ?: error("No MovieDetailsDto configured in FakeTmdbApiService")
    }

    override suspend fun getGenres(language: String): GenreListResponseDto {
        shouldThrow?.let { throw it }
        return GenreListResponseDto(genres = genreDtos)
    }
}
