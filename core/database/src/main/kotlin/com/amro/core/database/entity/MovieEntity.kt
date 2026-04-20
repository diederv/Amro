package com.amro.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val voteCount: Int = 0,
    val releaseDate: String,
    val genreIds: List<Int>,
    val popularity: Double,
    // Detail-only fields — populated after getMovieDetails()
    val tagline: String = "",
    val runtime: Int? = null,
    val status: String = "",
    val budget: Long = 0L,
    val revenue: Long = 0L,
    val imdbId: String? = null,
)
