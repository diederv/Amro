package com.amro.feature.movies.data.repository

import com.amro.core.model.Genre
import com.amro.core.model.Movie
import com.amro.core.model.MovieDetails
import com.amro.core.model.Result
import com.amro.feature.movies.data.mapper.toDetailsDomain
import com.amro.feature.movies.data.mapper.toDomain
import com.amro.feature.movies.data.mapper.toEntity
import com.amro.feature.movies.data.source.local.MovieLocalDataSource
import com.amro.feature.movies.data.source.remote.MovieRemoteDataSource
import com.amro.feature.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(
    private val remoteDataSource: MovieRemoteDataSource,
    private val localDataSource: MovieLocalDataSource,
) : MovieRepository {

    // Room is the single source of truth — UI observes this Flow.
    override fun observeTrendingMovies(): Flow<List<Movie>> =
        localDataSource.observeMovies().map { entities -> entities.map { it.toDomain() } }

    override fun observeGenres(): Flow<List<Genre>> =
        localDataSource.observeGenres().map { entities -> entities.map { it.toDomain() } }

    // Called by ViewModel on launch, on pull-to-refresh, and on error-state retry.
    override suspend fun refreshTrendingMovies(): Result<Unit> = runCatching {
        val movies = remoteDataSource.getTrendingMovies()
        localDataSource.upsertMovies(movies.map { it.toEntity() })
        val genres = remoteDataSource.getGenres()
        localDataSource.upsertGenres(genres.map { it.toEntity() })
    }.fold(
        onSuccess = { Result.Success(Unit) },
        onFailure = { Result.Error(it) },
    )

    // Fetches details from network, caches to Room, then reads from Room.
    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetails> = runCatching {
        val detailsDto = remoteDataSource.getMovieDetails(movieId)
        localDataSource.upsertMovie(detailsDto.toEntity())
        val entity = localDataSource.getMovieById(movieId)
            ?: error("Movie $movieId not found after upsert")
        val genres = localDataSource.getAllGenres()
        entity.toDetailsDomain(genres)
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { Result.Error(it) },
    )
}
