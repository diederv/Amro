package com.amro.feature.movies.domain.usecase

import com.amro.core.model.MovieDetails
import com.amro.core.model.Result
import com.amro.feature.movies.domain.repository.MovieRepository

class GetMovieDetailsUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movieId: Int): Result<MovieDetails> =
        repository.getMovieDetails(movieId)
}
