package com.amro.feature.movies.data.mapper

import com.amro.core.database.entity.GenreEntity
import com.amro.core.database.entity.MovieEntity
import com.amro.core.model.Genre
import com.amro.core.model.Movie
import com.amro.core.model.MovieDetails
import com.amro.core.network.model.GenreDto
import com.amro.core.network.model.MovieDetailsDto
import com.amro.core.network.model.MovieDto

// DTO → Entity
fun MovieDto.toEntity(): MovieEntity = MovieEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    voteAverage = voteAverage,
    releaseDate = releaseDate,
    genreIds = genreIds,
    popularity = popularity,
)

fun MovieDetailsDto.toEntity(): MovieEntity = MovieEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    voteAverage = voteAverage,
    voteCount = voteCount,
    releaseDate = releaseDate,
    genreIds = genres.map { it.id },
    popularity = popularity,
    tagline = tagline,
    runtime = runtime,
    status = status,
    budget = budget,
    revenue = revenue,
    imdbId = imdbId,
)

fun GenreDto.toEntity(): GenreEntity = GenreEntity(id = id, name = name)

// Entity → Domain
fun MovieEntity.toDomain(): Movie = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    voteAverage = voteAverage,
    releaseDate = releaseDate,
    genreIds = genreIds,
    popularity = popularity,
)

fun MovieEntity.toDetailsDomain(genreEntities: List<GenreEntity>): MovieDetails {
    val genreMap = genreEntities.associate { it.id to it.name }
    return MovieDetails(
        id = id,
        title = title,
        overview = overview,
        tagline = tagline,
        posterPath = posterPath,
        backdropPath = backdropPath,
        voteAverage = voteAverage,
        voteCount = voteCount,
        releaseDate = releaseDate,
        runtime = runtime,
        status = status,
        budget = budget,
        revenue = revenue,
        genres = genreIds.mapNotNull { id -> genreMap[id]?.let { Genre(id, it) } },
        imdbId = imdbId,
        popularity = popularity,
    )
}

fun GenreEntity.toDomain(): Genre = Genre(id = id, name = name)
