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
        testMovie(id = 1, title = "Inception", genreIds = listOf(28), voteAverage = 8.3),
        testMovie(id = 2, title = "The Mask", genreIds = listOf(35), voteAverage = 6.5),
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
            AmroTheme {
                MovieListContent(
                    state = contentState(),
                    onMovieClick = {},
                    onGenreFilter = {},
                    onSortOption = {},
                    onSortOrder = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Inception").assertIsDisplayed()
        composeTestRule.onNodeWithText("The Mask").assertIsDisplayed()
    }

    @Test
    fun contentStateShowsGenreFilterChips() {
        composeTestRule.setContent {
            AmroTheme {
                MovieListContent(
                    state = contentState(),
                    onMovieClick = {},
                    onGenreFilter = {},
                    onSortOption = {},
                    onSortOrder = {},
                )
            }
        }

        composeTestRule.onNodeWithText("All").assertIsDisplayed()
        composeTestRule.onNodeWithText("Action").assertIsDisplayed()
        composeTestRule.onNodeWithText("Comedy").assertIsDisplayed()
    }

    @Test
    fun clickingGenreChipInvokesCallback() {
        var selectedId: Int? = -1

        composeTestRule.setContent {
            AmroTheme {
                MovieListContent(
                    state = contentState(),
                    onMovieClick = {},
                    onGenreFilter = { selectedId = it },
                    onSortOption = {},
                    onSortOrder = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Action").performClick()
        assertEquals(28, selectedId)
    }

    @Test
    fun clickingAllChipClearsFilter() {
        var selectedId: Int? = 28

        composeTestRule.setContent {
            AmroTheme {
                MovieListContent(
                    state = contentState(selectedGenreId = 28),
                    onMovieClick = {},
                    onGenreFilter = { selectedId = it },
                    onSortOption = {},
                    onSortOrder = {},
                )
            }
        }

        composeTestRule.onNodeWithText("All").performClick()
        assertEquals(null, selectedId)
    }

    @Test
    fun errorStateShowsMessageAndRetryButton() {
        var retryClicked = false

        composeTestRule.setContent {
            AmroTheme {
                ErrorContent(message = "Network error", onRetry = { retryClicked = true })
            }
        }

        composeTestRule.onNodeWithText("Something went wrong").assertIsDisplayed()
        composeTestRule.onNodeWithText("Network error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()

        composeTestRule.onNodeWithText("Retry").performClick()
        assertEquals(true, retryClicked)
    }

    @Test
    fun emptyStateShowsMessage() {
        composeTestRule.setContent {
            AmroTheme { EmptyContent() }
        }

        composeTestRule.onNodeWithText("No movies match your filter.", substring = true).assertIsDisplayed()
    }
}
