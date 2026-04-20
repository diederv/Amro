package com.amro.feature.movies.ui.screenshot

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.amro.core.designsystem.theme.AmroTheme
import com.amro.core.model.Genre
import com.amro.feature.movies.domain.model.SortOption
import com.amro.feature.movies.domain.model.SortOrder
import com.amro.feature.movies.ui.detail.DetailContent
import com.amro.feature.movies.ui.list.EmptyContent
import com.amro.feature.movies.ui.list.ErrorContent
import com.amro.feature.movies.ui.list.LoadingContent
import com.amro.feature.movies.ui.list.MovieListContent
import com.amro.feature.movies.ui.list.TrendingMoviesUiState
import com.amro.feature.movies.util.testMovie
import com.amro.feature.movies.util.testMovieDetails
import org.junit.Rule
import org.junit.Test

class MovieScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    // --- List screen states ---

    @Test
    fun listLoading() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = false) { LoadingContent() }
        }
    }

    @Test
    fun listEmpty() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = false) { EmptyContent() }
        }
    }

    @Test
    fun listError() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = false) {
                ErrorContent(message = "Failed to load movies. Check your connection.", onRetry = {})
            }
        }
    }

    @Test
    fun listContentLight() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = false) {
                MovieListContent(
                    state = TrendingMoviesUiState.Content(
                        movies = listOf(
                            testMovie(id = 1, title = "Inception", voteAverage = 8.3, genreIds = listOf(28, 878)),
                            testMovie(id = 2, title = "Interstellar", voteAverage = 8.6, genreIds = listOf(878, 18)),
                            testMovie(id = 3, title = "The Dark Knight", voteAverage = 9.0, genreIds = listOf(28, 80)),
                        ),
                        allGenres = listOf(Genre(28, "Action"), Genre(878, "Sci-Fi"), Genre(18, "Drama"), Genre(80, "Crime")),
                        selectedGenreId = null,
                        sortOption = SortOption.POPULARITY,
                        sortOrder = SortOrder.DESCENDING,
                    ),
                    onMovieClick = {},
                    onGenreFilter = {},
                    onSortOption = {},
                    onSortOrder = {},
                )
            }
        }
    }

    @Test
    fun listContentDark() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = true) {
                MovieListContent(
                    state = TrendingMoviesUiState.Content(
                        movies = listOf(
                            testMovie(id = 1, title = "Inception", voteAverage = 8.3, genreIds = listOf(28)),
                            testMovie(id = 2, title = "Interstellar", voteAverage = 8.6, genreIds = listOf(878)),
                        ),
                        allGenres = listOf(Genre(28, "Action"), Genre(878, "Sci-Fi")),
                        selectedGenreId = null,
                        sortOption = SortOption.POPULARITY,
                        sortOrder = SortOrder.DESCENDING,
                    ),
                    onMovieClick = {},
                    onGenreFilter = {},
                    onSortOption = {},
                    onSortOrder = {},
                )
            }
        }
    }

    // --- Detail screen states ---

    @Test
    fun detailContentLight() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = false) {
                DetailContent(movieDetails = testMovieDetails(movieId = 1))
            }
        }
    }

    @Test
    fun detailContentDark() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = true) {
                DetailContent(movieDetails = testMovieDetails(movieId = 1))
            }
        }
    }
}
