package com.amro.feature.movies.ui.detail

import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.amro.core.designsystem.theme.AmroTheme
import com.amro.feature.movies.util.testMovieDetails
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun contentRendersTitle() {
        composeTestRule.setContent {
            AmroTheme {
                DetailContent(movieDetails = testMovieDetails(movieId = 1))
            }
        }

        composeTestRule.onNodeWithText("Test Movie 1").assertIsDisplayed()
    }

    @Test
    fun contentRendersTagline() {
        composeTestRule.setContent {
            AmroTheme { DetailContent(movieDetails = testMovieDetails()) }
        }

        composeTestRule.onNodeWithText("A tagline").assertIsDisplayed()
    }

    @Test
    fun contentRendersGenreChip() {
        composeTestRule.setContent {
            AmroTheme { DetailContent(movieDetails = testMovieDetails()) }
        }

        composeTestRule.onNodeWithText("Action").assertIsDisplayed()
    }

    @Test
    fun contentRendersOverviewSection() {
        composeTestRule.setContent {
            AmroTheme { DetailContent(movieDetails = testMovieDetails()) }
        }

        composeTestRule.onNodeWithText("Overview").assertIsDisplayed()
        composeTestRule.onNodeWithText("Overview").assertIsDisplayed()
    }

    @Test
    fun imdbButtonShownWhenImdbIdPresent() {
        composeTestRule.setContent {
            AmroTheme { DetailContent(movieDetails = testMovieDetails()) }
        }

        composeTestRule.onNodeWithText("View on IMDb").assertIsDisplayed()
    }

    @Test
    fun imdbButtonHiddenWhenImdbIdNull() {
        composeTestRule.setContent {
            AmroTheme {
                DetailContent(movieDetails = testMovieDetails().copy(imdbId = null))
            }
        }

        composeTestRule.onNodeWithText("View on IMDb").assertDoesNotExist()
    }

    @Test
    fun taglineHiddenWhenBlank() {
        composeTestRule.setContent {
            AmroTheme {
                DetailContent(movieDetails = testMovieDetails().copy(tagline = ""))
            }
        }

        composeTestRule.onNodeWithText("A tagline").assertDoesNotExist()
    }
}
