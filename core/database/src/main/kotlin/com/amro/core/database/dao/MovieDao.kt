package com.amro.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amro.core.database.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    fun observeMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieEntity?

    @Upsert
    suspend fun upsertMovies(movies: List<MovieEntity>)

    @Upsert
    suspend fun upsertMovie(movie: MovieEntity)

    @Query("DELETE FROM movies")
    suspend fun clearAll()
}
