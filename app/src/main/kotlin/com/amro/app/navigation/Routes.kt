package com.amro.app.navigation

import kotlinx.serialization.Serializable

@Serializable
data object MoviesListRoute

@Serializable
data class MovieDetailsRoute(val movieId: Int)

@Serializable
data object SettingsRoute
