package com.amro.feature.movies.domain.usecase

import com.amro.core.model.Genre
import com.amro.feature.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class ObserveGenresUseCase(private val repository: MovieRepository) {
    operator fun invoke(): Flow<List<Genre>> = repository.observeGenres()
}
