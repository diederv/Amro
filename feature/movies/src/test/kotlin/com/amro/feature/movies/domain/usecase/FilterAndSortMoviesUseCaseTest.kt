package com.amro.feature.movies.domain.usecase

import com.amro.feature.movies.domain.model.FilterAndSortParams
import com.amro.feature.movies.domain.model.SortOption
import com.amro.feature.movies.domain.model.SortOrder
import com.amro.feature.movies.util.testMovie
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FilterAndSortMoviesUseCaseTest {

    private lateinit var useCase: FilterAndSortMoviesUseCase

    private val actionMovie = testMovie(id = 1, title = "Action Hero", popularity = 90.0, releaseDate = "2024-03-01", genreIds = listOf(28))
    private val comedyMovie = testMovie(id = 2, title = "Comedy Night", popularity = 60.0, releaseDate = "2023-06-15", genreIds = listOf(35))
    private val actionComedy = testMovie(id = 3, title = "Best of Both", popularity = 75.0, releaseDate = "2024-01-20", genreIds = listOf(28, 35))
    private val dramaMovie = testMovie(id = 4, title = "Deep Drama", popularity = 45.0, releaseDate = "2022-11-01", genreIds = listOf(18))

    private val allMovies = listOf(actionMovie, comedyMovie, actionComedy, dramaMovie)

    @Before
    fun setup() {
        useCase = FilterAndSortMoviesUseCase()
    }

    // --- Filter tests ---

    @Test
    fun `no genre filter returns all movies`() {
        val result = useCase(allMovies, FilterAndSortParams())
        assertEquals(allMovies.size, result.size)
    }

    @Test
    fun `filter by action genre returns only action movies`() {
        val result = useCase(allMovies, FilterAndSortParams(genreId = 28))
        assertEquals(listOf(actionMovie, actionComedy), result.sortedBy { it.id })
    }

    @Test
    fun `filter by comedy genre returns only comedy movies`() {
        val result = useCase(allMovies, FilterAndSortParams(genreId = 35))
        assertEquals(listOf(comedyMovie, actionComedy), result.sortedBy { it.id })
    }

    @Test
    fun `filter by genre with no matches returns empty list`() {
        val result = useCase(allMovies, FilterAndSortParams(genreId = 99))
        assertTrue(result.isEmpty())
    }

    @Test
    fun `filter on empty list returns empty list`() {
        val result = useCase(emptyList(), FilterAndSortParams(genreId = 28))
        assertTrue(result.isEmpty())
    }

    // --- Sort by popularity ---

    @Test
    fun `sort by popularity descending returns highest first`() {
        val result = useCase(allMovies, FilterAndSortParams(sortOption = SortOption.POPULARITY, sortOrder = SortOrder.DESCENDING))
        assertEquals(listOf(90.0, 75.0, 60.0, 45.0), result.map { it.popularity })
    }

    @Test
    fun `sort by popularity ascending returns lowest first`() {
        val result = useCase(allMovies, FilterAndSortParams(sortOption = SortOption.POPULARITY, sortOrder = SortOrder.ASCENDING))
        assertEquals(listOf(45.0, 60.0, 75.0, 90.0), result.map { it.popularity })
    }

    // --- Sort by title ---

    @Test
    fun `sort by title ascending returns alphabetical order`() {
        val result = useCase(allMovies, FilterAndSortParams(sortOption = SortOption.TITLE, sortOrder = SortOrder.ASCENDING))
        assertEquals(listOf("Action Hero", "Best of Both", "Comedy Night", "Deep Drama"), result.map { it.title })
    }

    @Test
    fun `sort by title descending returns reverse alphabetical order`() {
        val result = useCase(allMovies, FilterAndSortParams(sortOption = SortOption.TITLE, sortOrder = SortOrder.DESCENDING))
        assertEquals(listOf("Deep Drama", "Comedy Night", "Best of Both", "Action Hero"), result.map { it.title })
    }

    // --- Sort by release date ---

    @Test
    fun `sort by release date descending returns newest first`() {
        val result = useCase(allMovies, FilterAndSortParams(sortOption = SortOption.RELEASE_DATE, sortOrder = SortOrder.DESCENDING))
        assertEquals(listOf("2024-03-01", "2024-01-20", "2023-06-15", "2022-11-01"), result.map { it.releaseDate })
    }

    @Test
    fun `sort by release date ascending returns oldest first`() {
        val result = useCase(allMovies, FilterAndSortParams(sortOption = SortOption.RELEASE_DATE, sortOrder = SortOrder.ASCENDING))
        assertEquals(listOf("2022-11-01", "2023-06-15", "2024-01-20", "2024-03-01"), result.map { it.releaseDate })
    }

    // --- Combined filter + sort ---

    @Test
    fun `filter action then sort by popularity ascending`() {
        val result = useCase(
            allMovies,
            FilterAndSortParams(genreId = 28, sortOption = SortOption.POPULARITY, sortOrder = SortOrder.ASCENDING)
        )
        assertEquals(listOf(75.0, 90.0), result.map { it.popularity })
    }

    @Test
    fun `filter comedy then sort by title descending`() {
        val result = useCase(
            allMovies,
            FilterAndSortParams(genreId = 35, sortOption = SortOption.TITLE, sortOrder = SortOrder.DESCENDING)
        )
        assertEquals(listOf("Comedy Night", "Best of Both"), result.map { it.title })
    }
}
