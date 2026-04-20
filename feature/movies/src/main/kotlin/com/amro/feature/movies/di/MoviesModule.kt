package com.amro.feature.movies.di

import com.amro.feature.movies.data.repository.MovieRepositoryImpl
import com.amro.feature.movies.data.source.local.MovieLocalDataSource
import com.amro.feature.movies.data.source.remote.MovieRemoteDataSource
import com.amro.feature.movies.domain.repository.MovieRepository
import com.amro.feature.movies.domain.usecase.FilterAndSortMoviesUseCase
import com.amro.feature.movies.domain.usecase.GetMovieDetailsUseCase
import com.amro.feature.movies.domain.usecase.GetTrendingMoviesUseCase
import com.amro.feature.movies.domain.usecase.ObserveGenresUseCase
import com.amro.feature.movies.ui.detail.MovieDetailsViewModel
import com.amro.feature.movies.ui.list.TrendingMoviesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val moviesModule = module {
    // Data
    single { MovieRemoteDataSource(get()) }
    single { MovieLocalDataSource(get(), get()) }
    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }

    // Use cases
    factory { GetTrendingMoviesUseCase(get()) }
    factory { ObserveGenresUseCase(get()) }
    factory { FilterAndSortMoviesUseCase() }
    factory { GetMovieDetailsUseCase(get()) }

    // ViewModels
    viewModel { TrendingMoviesViewModel(get(), get(), get(), get()) }
    viewModel { params -> MovieDetailsViewModel(get(), get()) }
}
