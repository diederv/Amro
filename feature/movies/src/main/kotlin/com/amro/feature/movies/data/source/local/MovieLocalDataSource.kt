package com.amro.feature.movies.data.source.local

import com.amro.core.database.dao.GenreDao
import com.amro.core.database.dao.MovieDao
import com.amro.core.database.entity.GenreEntity
import com.amro.core.database.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

class MovieLocalDataSource(
    private val movieDao: MovieDao,
    private val genreDao: GenreDao,
) {
    fun observeMovies(): Flow<List<MovieEntity>> = movieDao.observeMovies()

    fun observeGenres(): Flow<List<GenreEntity>> = genreDao.observeGenres()

    suspend fun getMovieById(movieId: Int): MovieEntity? = movieDao.getMovieById(movieId)

    suspend fun getAllGenres(): List<GenreEntity> = genreDao.getAllGenres()

    suspend fun upsertMovies(movies: List<MovieEntity>) = movieDao.upsertMovies(movies)

    suspend fun upsertMovie(movie: MovieEntity) = movieDao.upsertMovie(movie)

    suspend fun upsertGenres(genres: List<GenreEntity>) = genreDao.upsertGenres(genres)
}
