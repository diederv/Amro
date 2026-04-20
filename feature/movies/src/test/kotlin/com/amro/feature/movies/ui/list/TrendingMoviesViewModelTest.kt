package com.amro.feature.movies.ui.list

import app.cash.turbine.test
import com.amro.core.model.Genre
import com.amro.core.model.Result
import com.amro.feature.movies.domain.model.SortOption
import com.amro.feature.movies.domain.model.SortOrder
import com.amro.feature.movies.domain.usecase.FilterAndSortMoviesUseCase
import com.amro.feature.movies.domain.usecase.GetTrendingMoviesUseCase
import com.amro.feature.movies.domain.usecase.ObserveGenresUseCase
import com.amro.feature.movies.util.FakeMovieRepository
import com.amro.feature.movies.util.testMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrendingMoviesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel(repo: FakeMovieRepository) = TrendingMoviesViewModel(
        getTrendingMovies = GetTrendingMoviesUseCase(repo),
        observeGenres = ObserveGenresUseCase(repo),
        filterAndSort = FilterAndSortMoviesUseCase(),
        repository = repo,
    )

    @Test
    fun `initial state is Loading`() = runTest {
        val repo = FakeMovieRepository()
        val vm = buildViewModel(repo)

        vm.uiState.test {
            assertEquals(TrendingMoviesUiState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits Content when movies become available after refresh`() = runTest {
        val movies = listOf(testMovie(id = 1, title = "Movie A", popularity = 90.0))
        val genres = listOf(Genre(28, "Action"))
        val repo = FakeMovieRepository(genres = genres) // empty movies initially

        val vm = buildViewModel(repo)

        vm.uiState.test {
            assertEquals(TrendingMoviesUiState.Loading, awaitItem())

            repo.setMovies(movies)
            advanceUntilIdle()

            val state = awaitItem()
            assertTrue("Expected Content but got $state", state is TrendingMoviesUiState.Content)
            assertEquals(1, (state as TrendingMoviesUiState.Content).movies.size)
            assertEquals(1, state.allGenres.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits Error when refresh fails and no cached movies`() = runTest {
        val error = RuntimeException("Network error")
        val repo = FakeMovieRepository(refreshResult = Result.Error(error))
        val vm = buildViewModel(repo)

        vm.uiState.test {
            assertEquals(TrendingMoviesUiState.Loading, awaitItem())
            advanceUntilIdle()

            val state = awaitItem()
            assertTrue("Expected Error but got $state", state is TrendingMoviesUiState.Error)
            assertEquals("Network error", (state as TrendingMoviesUiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `filter by genre updates visible movies`() = runTest {
        val actionMovie = testMovie(id = 1, genreIds = listOf(28), popularity = 90.0)
        val comedyMovie = testMovie(id = 2, genreIds = listOf(35), popularity = 60.0)
        val repo = FakeMovieRepository(
            movies = listOf(actionMovie, comedyMovie),
            genres = listOf(Genre(28, "Action"), Genre(35, "Comedy")),
        )
        val vm = buildViewModel(repo)

        vm.uiState.test {
            assertEquals(TrendingMoviesUiState.Loading, awaitItem())
            advanceUntilIdle()

            val initial = awaitItem() as TrendingMoviesUiState.Content
            assertEquals(2, initial.movies.size)

            vm.setGenreFilter(28)
            val filtered = awaitItem() as TrendingMoviesUiState.Content
            assertEquals(1, filtered.movies.size)
            assertEquals(1, filtered.movies.first().id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `clearing genre filter restores all movies`() = runTest {
        val movies = listOf(
            testMovie(id = 1, genreIds = listOf(28)),
            testMovie(id = 2, genreIds = listOf(35)),
        )
        val repo = FakeMovieRepository(movies = movies)
        val vm = buildViewModel(repo)

        vm.uiState.test {
            assertEquals(TrendingMoviesUiState.Loading, awaitItem())
            advanceUntilIdle()

            val initial = awaitItem() as TrendingMoviesUiState.Content
            assertEquals(2, initial.movies.size)

            vm.setGenreFilter(28)
            val filtered = awaitItem() as TrendingMoviesUiState.Content
            assertEquals(1, filtered.movies.size)

            vm.setGenreFilter(null)
            val restored = awaitItem() as TrendingMoviesUiState.Content
            assertEquals(2, restored.movies.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `sort by title ascending orders movies alphabetically`() = runTest {
        val movies = listOf(
            testMovie(id = 1, title = "Zebra Film", popularity = 90.0),
            testMovie(id = 2, title = "Alpha Movie", popularity = 60.0),
        )
        val repo = FakeMovieRepository(movies = movies)
        val vm = buildViewModel(repo)

        vm.uiState.test {
            assertEquals(TrendingMoviesUiState.Loading, awaitItem())
            advanceUntilIdle()
            awaitItem() // Content with default sort (popularity DESC)

            vm.setSortOption(SortOption.TITLE)
            awaitItem() // Content with TITLE DESC

            vm.setSortOrder(SortOrder.ASCENDING)
            val state = awaitItem() as TrendingMoviesUiState.Content
            assertEquals("Alpha Movie", state.movies.first().title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `empty filter result emits Empty state`() = runTest {
        val movies = listOf(testMovie(id = 1, genreIds = listOf(28)))
        val repo = FakeMovieRepository(movies = movies)
        val vm = buildViewModel(repo)

        vm.uiState.test {
            assertEquals(TrendingMoviesUiState.Loading, awaitItem())
            advanceUntilIdle()
            awaitItem() // Content with 1 movie

            vm.setGenreFilter(99) // no movie has genre 99
            val state = awaitItem()
            assertEquals(TrendingMoviesUiState.Empty, state)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
