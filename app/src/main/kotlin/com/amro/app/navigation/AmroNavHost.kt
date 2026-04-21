package com.amro.app.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amro.feature.movies.ui.LocalAnimatedVisibilityScope
import com.amro.feature.movies.ui.LocalSharedTransitionScope
import com.amro.feature.movies.ui.detail.MovieDetailsScreen
import com.amro.feature.movies.ui.list.TrendingMoviesScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AmroNavHost(navController: NavHostController) {
    SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            NavHost(navController = navController, startDestination = MoviesListRoute) {
                composable<MoviesListRoute> {
                    CompositionLocalProvider(LocalAnimatedVisibilityScope provides this) {
                        TrendingMoviesScreen(
                            onMovieClick = { movieId ->
                                navController.navigate(MovieDetailsRoute(movieId))
                            }
                        )
                    }
                }
                composable<MovieDetailsRoute> {
                    CompositionLocalProvider(LocalAnimatedVisibilityScope provides this) {
                        MovieDetailsScreen(onBack = navController::navigateUp)
                    }
                }
            }
        }
    }
}
