package com.amro.core.network

import com.amro.core.network.model.GenreListResponseDto
import com.amro.core.network.model.MovieDetailsDto
import com.amro.core.network.model.TrendingResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US",
    ): TrendingResponseDto

    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") movieId: Int,
        @Query("language") language: String = "en-US",
    ): MovieDetailsDto

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("language") language: String = "en-US",
    ): GenreListResponseDto
}
