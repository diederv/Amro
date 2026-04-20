package com.amro.feature.movies.data.repository

import app.cash.turbine.test
import com.amro.core.model.Result
import com.amro.core.network.model.GenreDto
import com.amro.feature.movies.data.source.local.MovieLocalDataSource
import com.amro.feature.movies.data.source.remote.MovieRemoteDataSource
import com.amro.feature.movies.util.FakeGenreDao
import com.amro.feature.movies.util.FakeMovieDao
import com.amro.feature.movies.util.FakeTmdbApiService
import com.amro.feature.movies.util.testMovieDetailsDto
import com.amro.feature.movies.util.testMovieDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class MovieRepositoryImplTest {

    private lateinit var movieDao: FakeMovieDao
    private lateinit var genreDao: FakeGenreDao
    private lateinit var apiService: FakeTmdbApiService
    private lateinit var repository: MovieRepositoryImpl

    @Before
    fun setup() {
        movieDao = FakeMovieDao()
        genreDao = FakeGenreDao()
        apiService = FakeTmdbApiService()
        val local = MovieLocalDataSource(movieDao, genreDao)
        val remote = MovieRemoteDataSource(apiService)
        repository = MovieRepositoryImpl(remote, local)
    }

    // --- refreshTrendingMovies ---

    @Test
    fun `refreshTrendingMovies stores movies and genres in DB`() = runTest {
        apiService.movieDtos = listOf(testMovieDto(id = 1, title = "Inception"))
        apiService.genreDtos = listOf(GenreDto(28, "Action"))

        val result = repository.refreshTrendingMovies()

        assertTrue(result is Result.Success)
        assertEquals(1, movieDao.store.size)
        assertEquals("Inception", movieDao.store[1]?.title)
        assertEquals(1, genreDao.store.size)
    }

    @Test
    fun `refreshTrendingMovies upserts — second refresh replaces first`() = runTest {
        apiService.movieDtos = listOf(testMovieDto(id = 1, title = "Old Title"))
        repository.refreshTrendingMovies()

        apiService.movieDtos = listOf(testMovieDto(id = 1, title = "New Title"))
        repository.refreshTrendingMovies()

        assertEquals("New Title", movieDao.store[1]?.title)
        assertEquals(1, movieDao.store.size)
    }

    @Test
    fun `refreshTrendingMovies returns Error on network failure`() = runTest {
        apiService.shouldThrow = IOException("Network error")

        val result = repository.refreshTrendingMovies()

        assertTrue(result is Result.Error)
        assertEquals("Network error", (result as Result.Error).exception.message)
    }

    // --- observeTrendingMovies ---

    @Test
    fun `observeTrendingMovies emits empty list initially`() = runTest {
        repository.observeTrendingMovies().test {
            assertEquals(emptyList<Any>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `observeTrendingMovies emits movies after refresh`() = runTest {
        apiService.movieDtos = listOf(testMovieDto(id = 1, title = "Dune"))
        apiService.genreDtos = listOf(GenreDto(878, "Sci-Fi"))

        repository.observeTrendingMovies().test {
            assertEquals(emptyList<Any>(), awaitItem())

            repository.refreshTrendingMovies()

            val movies = awaitItem()
            assertEquals(1, movies.size)
            assertEquals("Dune", movies.first().title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- getMovieDetails ---

    @Test
    fun `getMovieDetails fetches from API, caches to Room, returns domain`() = runTest {
        val detailsDto = testMovieDetailsDto(movieId = 42)
        apiService.movieDetailsDto = detailsDto
        apiService.genreDtos = listOf(GenreDto(28, "Action"))
        repository.refreshTrendingMovies() // populate genres

        val result = repository.getMovieDetails(42)

        assertTrue(result is Result.Success)
        val details = (result as Result.Success).data
        assertEquals(42, details.id)
        assertEquals("Test Movie 42", details.title)
        assertEquals("A tagline", details.tagline)
        assertEquals(120, details.runtime)
        assertEquals(1, details.genres.size)
        assertEquals("Action", details.genres.first().name)
    }

    @Test
    fun `getMovieDetails returns Error on network failure`() = runTest {
        apiService.shouldThrow = IOException("Timeout")

        val result = repository.getMovieDetails(1)

        assertTrue(result is Result.Error)
        assertEquals("Timeout", (result as Result.Error).exception.message)
    }
}
