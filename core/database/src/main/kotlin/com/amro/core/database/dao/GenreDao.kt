package com.amro.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amro.core.database.entity.GenreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {

    @Query("SELECT * FROM genres ORDER BY name ASC")
    fun observeGenres(): Flow<List<GenreEntity>>

    @Query("SELECT * FROM genres")
    suspend fun getAllGenres(): List<GenreEntity>

    @Upsert
    suspend fun upsertGenres(genres: List<GenreEntity>)
}
