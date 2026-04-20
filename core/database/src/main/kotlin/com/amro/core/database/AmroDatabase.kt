package com.amro.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amro.core.database.converter.Converters
import com.amro.core.database.dao.GenreDao
import com.amro.core.database.dao.MovieDao
import com.amro.core.database.entity.GenreEntity
import com.amro.core.database.entity.MovieEntity

@Database(
    entities = [MovieEntity::class, GenreEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class AmroDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun genreDao(): GenreDao
}
