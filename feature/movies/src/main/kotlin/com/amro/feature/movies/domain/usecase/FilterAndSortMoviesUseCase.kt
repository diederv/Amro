package com.amro.feature.movies.domain.usecase

import com.amro.core.model.Movie
import com.amro.feature.movies.domain.model.FilterAndSortParams
import com.amro.feature.movies.domain.model.SortOption
import com.amro.feature.movies.domain.model.SortOrder

class FilterAndSortMoviesUseCase {

    operator fun invoke(movies: List<Movie>, params: FilterAndSortParams): List<Movie> {
        val filtered = if (params.genreId != null) {
            movies.filter { params.genreId in it.genreIds }
        } else {
            movies
        }

        val comparator: Comparator<Movie> = when (params.sortOption) {
            SortOption.POPULARITY -> compareBy { it.popularity }
            SortOption.TITLE -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.title }
            SortOption.RELEASE_DATE -> compareBy { it.releaseDate }
        }

        val sorted = filtered.sortedWith(comparator)
        return if (params.sortOrder == SortOrder.DESCENDING) sorted.reversed() else sorted
    }
}
