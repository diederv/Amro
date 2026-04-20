package com.amro.feature.movies.util

import com.amro.core.database.dao.GenreDao
import com.amro.core.database.entity.GenreEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeGenreDao : GenreDao {

    val store = mutableMapOf<Int, GenreEntity>()
    private val flow = MutableStateFlow<List<GenreEntity>>(emptyList())

    override fun observeGenres(): Flow<List<GenreEntity>> = flow

    override suspend fun getAllGenres(): List<GenreEntity> = store.values.toList()

    override suspend fun upsertGenres(genres: List<GenreEntity>) {
        genres.forEach { store[it.id] = it }
        flow.value = store.values.toList()
    }
}
