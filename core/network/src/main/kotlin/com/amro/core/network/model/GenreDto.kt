package com.amro.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class GenreDto(
    val id: Int,
    val name: String,
)
