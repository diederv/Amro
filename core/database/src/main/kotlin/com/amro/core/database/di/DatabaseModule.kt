package com.amro.core.database.di

import androidx.room.Room
import com.amro.core.database.AmroDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AmroDatabase::class.java,
            "amro.db",
        ).build()
    }
    single { get<AmroDatabase>().movieDao() }
    single { get<AmroDatabase>().genreDao() }
}
