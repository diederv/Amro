package com.amro.feature.movies.di

import com.amro.feature.movies.data.repository.MovieRepositoryImpl
import com.amro.feature.movies.data.source.local.MovieLocalDataSource
import com.amro.feature.movies.data.source.remote.MovieRemoteDataSource
import com.amro.feature.movies.domain.repository.MovieRepository
import org.koin.dsl.module

val moviesModule = module {
    single { MovieRemoteDataSource(get()) }
    single { MovieLocalDataSource(get(), get()) }
    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }
}
