package com.amro.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class GenreListResponseDto(
    val genres: List<GenreDto>,
)
