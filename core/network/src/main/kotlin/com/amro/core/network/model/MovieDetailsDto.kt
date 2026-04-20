package com.amro.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDto(
    val id: Int,
    val title: String,
    val overview: String,
    val tagline: String = "",
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("vote_count") val voteCount: Int = 0,
    @SerialName("release_date") val releaseDate: String = "",
    val runtime: Int? = null,
    val status: String = "",
    val budget: Long = 0,
    val revenue: Long = 0,
    val genres: List<GenreDto> = emptyList(),
    @SerialName("imdb_id") val imdbId: String? = null,
    val popularity: Double = 0.0,
)
