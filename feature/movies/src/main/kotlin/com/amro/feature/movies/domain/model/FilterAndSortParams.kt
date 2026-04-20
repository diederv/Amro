package com.amro.feature.movies.domain.model

data class FilterAndSortParams(
    val genreId: Int? = null,
    val sortOption: SortOption = SortOption.POPULARITY,
    val sortOrder: SortOrder = SortOrder.DESCENDING,
)
