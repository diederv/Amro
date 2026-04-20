package com.amro.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amro.feature.movies.ui.detail.MovieDetailsScreen
import com.amro.feature.movies.ui.list.TrendingMoviesScreen

@Composable
fun AmroNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = MoviesListRoute) {
        composable<MoviesListRoute> {
            TrendingMoviesScreen(
                onMovieClick = { movieId ->
                    navController.navigate(MovieDetailsRoute(movieId))
                }
            )
        }
        composable<MovieDetailsRoute> {
            MovieDetailsScreen(onBack = navController::navigateUp)
        }
    }
}
