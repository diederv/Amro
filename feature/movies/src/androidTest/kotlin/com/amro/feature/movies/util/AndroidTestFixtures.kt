package com.amro.feature.movies.util

import com.amro.core.model.Genre
import com.amro.core.model.Movie
import com.amro.core.model.MovieDetails

fun testMovie(
    id: Int = 1,
    title: String = "Test Movie",
    popularity: Double = 100.0,
    releaseDate: String = "2024-01-01",
    genreIds: List<Int> = emptyList(),
    voteAverage: Double = 7.5,
) = Movie(
    id = id,
    title = title,
    overview = "Overview for $title",
    posterPath = "/poster$id.jpg",
    backdropPath = null,
    voteAverage = voteAverage,
    releaseDate = releaseDate,
    genreIds = genreIds,
    popularity = popularity,
)

fun testMovieDetails(movieId: Int = 1) = MovieDetails(
    id = movieId,
    title = "Test Movie $movieId",
    overview = "Overview",
    tagline = "A tagline",
    posterPath = "/poster.jpg",
    backdropPath = null,
    voteAverage = 7.5,
    voteCount = 1000,
    releaseDate = "2024-01-01",
    runtime = 120,
    status = "Released",
    budget = 10_000_000L,
    revenue = 50_000_000L,
    genres = listOf(Genre(28, "Action")),
    imdbId = "tt1234567",
    popularity = 100.0,
)
