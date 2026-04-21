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
            AmroTheme(darkTheme = true) {
                DetailContent(movieDetails = testMovieDetails(movieId = 1))
            }
        }

        composeTestRule.onNodeWithText("TEST MOVIE 1").assertIsDisplayed()
    }

    @Test
    fun contentRendersTagline() {
        composeTestRule.setContent {
            AmroTheme(darkTheme = true) { DetailContent(movieDetails = testMovieDetails()) }
        }

        composeTestRule.onNodeWithText("A TAGLINE").assertIsDisplayed()
    }

    @Test
    fun contentRendersGenreChip() {
        composeTestRule.setContent {
            AmroTheme(darkTheme = true) { DetailContent(movieDetails = testMovieDetails()) }
        }

        composeTestRule.onNodeWithText("ACTION").assertIsDisplayed()
    }

    @Test
    fun contentRendersOverviewSection() {
        composeTestRule.setContent {
            AmroTheme(darkTheme = true) { DetailContent(movieDetails = testMovieDetails()) }
        }

        composeTestRule.onNodeWithText("OVERVIEW").assertIsDisplayed()
    }

    @Test
    fun imdbButtonShownWhenImdbIdPresent() {
        composeTestRule.setContent {
            AmroTheme(darkTheme = true) { DetailContent(movieDetails = testMovieDetails()) }
        }

        composeTestRule.onNodeWithText("VIEW ON IMDB").assertIsDisplayed()
    }

    @Test
    fun imdbButtonHiddenWhenImdbIdNull() {
        composeTestRule.setContent {
            AmroTheme(darkTheme = true) {
                DetailContent(movieDetails = testMovieDetails().copy(imdbId = null))
            }
        }

        composeTestRule.onNodeWithText("VIEW ON IMDB").assertDoesNotExist()
    }

    @Test
    fun taglineHiddenWhenBlank() {
        composeTestRule.setContent {
            AmroTheme(darkTheme = true) {
                DetailContent(movieDetails = testMovieDetails().copy(tagline = ""))
            }
        }

        composeTestRule.onNodeWithText("A TAGLINE").assertDoesNotExist()
    }
}
