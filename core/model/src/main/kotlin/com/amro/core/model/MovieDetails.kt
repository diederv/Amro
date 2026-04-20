package com.amro.core.model

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val tagline: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val releaseDate: String,
    val runtime: Int?,
    val status: String,
    val budget: Long,
    val revenue: Long,
    val genres: List<Genre>,
    val imdbId: String?,
    val popularity: Double,
)
