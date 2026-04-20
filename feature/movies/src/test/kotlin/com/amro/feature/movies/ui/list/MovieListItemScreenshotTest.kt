package com.amro.feature.movies.ui.list

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.amro.core.designsystem.theme.AmroTheme
import com.amro.core.model.Genre
import com.amro.feature.movies.util.testMovie
import org.junit.Rule
import org.junit.Test

class MovieListItemScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    private val movie = testMovie(
        id = 1,
        title = "Inception",
        releaseDate = "2010-07-16",
        voteAverage = 8.3,
        genreIds = listOf(28, 878),
    )
    private val genres = listOf(Genre(28, "Action"), Genre(878, "Science Fiction"))

    @Test
    fun listItemLight() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = false) {
                MovieListItem(movie = movie, genres = genres, onClick = {})
            }
        }
    }

    @Test
    fun listItemDark() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = true) {
                MovieListItem(movie = movie, genres = genres, onClick = {})
            }
        }
    }

    @Test
    fun listItemNoGenres() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = false) {
                MovieListItem(
                    movie = testMovie(id = 2, title = "Unknown Genre Film", genreIds = emptyList()),
                    genres = emptyList(),
                    onClick = {},
                )
            }
        }
    }

    @Test
    fun listItemLongTitle() {
        paparazzi.snapshot {
            AmroTheme(darkTheme = false) {
                MovieListItem(
                    movie = testMovie(id = 3, title = "A Very Long Movie Title That Should Be Truncated After Two Lines"),
                    genres = genres,
                    onClick = {},
                )
            }
        }
    }
}
