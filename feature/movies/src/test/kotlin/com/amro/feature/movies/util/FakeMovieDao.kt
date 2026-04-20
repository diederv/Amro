package com.amro.feature.movies.util

import com.amro.core.database.dao.MovieDao
import com.amro.core.database.entity.MovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeMovieDao : MovieDao {

    val store = mutableMapOf<Int, MovieEntity>()
    private val flow = MutableStateFlow<List<MovieEntity>>(emptyList())

    override fun observeMovies(): Flow<List<MovieEntity>> = flow

    override suspend fun getMovieById(movieId: Int): MovieEntity? = store[movieId]

    override suspend fun upsertMovies(movies: List<MovieEntity>) {
        movies.forEach { store[it.id] = it }
        flow.value = store.values.toList()
    }

    override suspend fun upsertMovie(movie: MovieEntity) = upsertMovies(listOf(movie))

    override suspend fun clearAll() {
        store.clear()
        flow.value = emptyList()
    }
}
