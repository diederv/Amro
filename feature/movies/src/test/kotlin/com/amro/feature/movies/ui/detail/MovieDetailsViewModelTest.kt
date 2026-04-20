package com.amro.feature.movies.ui.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.amro.core.model.Result
import com.amro.feature.movies.domain.usecase.GetMovieDetailsUseCase
import com.amro.feature.movies.util.FakeMovieRepository
import com.amro.feature.movies.util.testMovieDetails
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
class MovieDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel(
        movieId: Int = 1,
        repo: FakeMovieRepository = FakeMovieRepository(
            detailsResult = Result.Success(testMovieDetails(movieId)),
        ),
    ) = MovieDetailsViewModel(
        savedStateHandle = SavedStateHandle(mapOf("movieId" to movieId)),
        getMovieDetails = GetMovieDetailsUseCase(repo),
    )

    @Test
    fun `initial state is Loading`() = runTest {
        val vm = buildViewModel()
        vm.uiState.test {
            assertEquals(MovieDetailsUiState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits Content with correct details on successful load`() = runTest {
        val details = testMovieDetails(movieId = 42)
        val vm = buildViewModel(
            movieId = 42,
            repo = FakeMovieRepository(detailsResult = Result.Success(details)),
        )

        vm.uiState.test {
            assertEquals(MovieDetailsUiState.Loading, awaitItem())
            advanceUntilIdle()

            val state = awaitItem()
            assertTrue("Expected Content but got $state", state is MovieDetailsUiState.Content)
            assertEquals(details, (state as MovieDetailsUiState.Content).movieDetails)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits Error with message on failed load`() = runTest {
        val vm = buildViewModel(
            repo = FakeMovieRepository(detailsResult = Result.Error(RuntimeException("Load failed"))),
        )

        vm.uiState.test {
            assertEquals(MovieDetailsUiState.Loading, awaitItem())
            advanceUntilIdle()

            val state = awaitItem()
            assertTrue("Expected Error but got $state", state is MovieDetailsUiState.Error)
            assertEquals("Load failed", (state as MovieDetailsUiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry resets to Loading then emits Content`() = runTest {
        val vm = buildViewModel()

        vm.uiState.test {
            awaitItem() // Loading (init)
            advanceUntilIdle()
            awaitItem() // Content

            vm.retry()
            assertEquals(MovieDetailsUiState.Loading, awaitItem())
            advanceUntilIdle()
            assertTrue(awaitItem() is MovieDetailsUiState.Content)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uses movieId from SavedStateHandle`() = runTest {
        val details = testMovieDetails(movieId = 99)
        val vm = buildViewModel(
            movieId = 99,
            repo = FakeMovieRepository(detailsResult = Result.Success(details)),
        )

        vm.uiState.test {
            awaitItem() // Loading
            advanceUntilIdle()

            val state = awaitItem() as MovieDetailsUiState.Content
            assertEquals(99, state.movieDetails.id)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
