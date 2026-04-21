package com.amro.feature.movies.ui.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.amro.core.designsystem.theme.AmroTheme
import com.amro.core.model.Genre
import com.amro.feature.movies.domain.model.SortOption
import com.amro.feature.movies.domain.model.SortOrder
import com.amro.feature.movies.util.testMovie
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TrendingMoviesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val genres = listOf(Genre(28, "Action"), Genre(35, "Comedy"))
    private val movies = listOf(
        // The first movie becomes the featured hero; the rest populate the grid.
        testMovie(id = 1, title = "Inception", genreIds = listOf(28), voteAverage = 8.3),
        testMovie(id = 2, title = "The Mask", genreIds = listOf(35), voteAverage = 6.5),
        testMovie(id = 3, title = "Tenet", genreIds = listOf(28), voteAverage = 7.3),
    )

    private fun contentState(selectedGenreId: Int? = null) = TrendingMoviesUiState.Content(
        movies = movies,
        allGenres = genres,
        selectedGenreId = selectedGenreId,
        sortOption = SortOption.POPULARITY,
        sortOrder = SortOrder.DESCENDING,
    )

    @Test
    fun contentStateShowsMovieTitles() {
        composeTestRule.setContent {
            AmroTheme(darkTheme = true) {
                MovieListContent(
                    state = contentState(),
                    onMovieClick = {},
                    onGenreFilter = {},
                    onSortOption = {},
                    onSortOrder = {},
                )
            }
        }

        // Hero renders the featured movie title in uppercase; grid items likewise.
        composeTestRule.onNodeWithText("INCEPTION").assertIsDisplayed()
        composeTestRule.onNodeWithText("THE MASK").assertIsDisplayed()
        composeTestRule.onNodeWithText("TENET").assertIsDisplayed()
    }

    @Test
    fun contentStateShowsGenreFilterChips() {
        composeTestRule.setContent {
            AmroTheme(darkTheme = true) {
                MovieListContent(
                    state = contentState(),
                    onMovieClick = {},
                    onGenreFilter = {},
                    onSortOption = {},
                    onSortOrder = {},
                )
            }
        }

        composeTestRule.onNodeWithText("FOR YOU").assertIsDisplayed()
        composeTestRule.onNodeWithText("ACTION").assertIsDisplayed()
        composeTestRule.onNodeWithText("COMEDY").assertIsDisplayed()
    }

    @Test
    fun clickingGenreChipInvokesCallback() {
        var selectedId: Int? = -1

        composeTestRule.setContent {
            AmroTheme(darkTheme = true) {
                MovieListContent(
                    state = contentState(),
                    onMovieClick = {},
                    onGenreFilter = { selectedId = it },
                    onSortOption = {},
                    onSortOrder = {},
                )
            }
        }

        composeTestRule.onNodeWithText("ACTION").performClick()
        assertEquals(28, selectedId)
    }

    @Test
    fun clickingForYouChipClearsFilter() {
        var selectedId: Int? = 28

        composeTestRule.setContent {
            AmroTheme(darkTheme = true) {
                MovieListContent(
                    state = contentState(selectedGenreId = 28),
                    onMovieClick = {},
                    onGenreFilter = { selectedId = it },
                    onSortOption = {},
                    onSortOrder = {},
                )
            }
        }

        composeTestRule.onNodeWithText("FOR YOU").performClick()
        assertEquals(null, selectedId)
    }

    @Test
    fun errorStateShowsMessageAndRetryButton() {
        var retryClicked = false

        composeTestRule.setContent {
            AmroTheme(darkTheme = true) {
                ErrorContent(message = "Network error", onRetry = { retryClicked = true })
            }
        }

        composeTestRule.onNodeWithText("SIGNAL LOST").assertIsDisplayed()
        composeTestRule.onNodeWithText("Network error").assertIsDisplayed()
        composeTestRule.onNodeWithText("RETRY").assertIsDisplayed()

        composeTestRule.onNodeWithText("RETRY").performClick()
        assertEquals(true, retryClicked)
    }

    @Test
    fun emptyStateShowsMessage() {
        composeTestRule.setContent {
            AmroTheme(darkTheme = true) { EmptyContent() }
        }

        composeTestRule.onNodeWithText("No movies match your filter.", substring = true).assertIsDisplayed()
    }
}
