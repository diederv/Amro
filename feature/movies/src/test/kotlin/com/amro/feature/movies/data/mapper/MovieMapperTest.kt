package com.amro.feature.movies.data.mapper

import com.amro.core.database.entity.GenreEntity
import com.amro.core.network.model.GenreDto
import com.amro.feature.movies.util.testMovieDetailsDto
import com.amro.feature.movies.util.testMovieDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MovieMapperTest {

    // --- MovieDto → MovieEntity ---

    @Test
    fun `MovieDto toEntity maps all fields`() {
        val dto = testMovieDto(id = 7, title = "Interstellar", popularity = 88.0, releaseDate = "2014-11-05", genreIds = listOf(878, 18))
        val entity = dto.toEntity()

        assertEquals(7, entity.id)
        assertEquals("Interstellar", entity.title)
        assertEquals("Overview for Interstellar", entity.overview)
        assertEquals("/poster7.jpg", entity.posterPath)
        assertNull(entity.backdropPath)
        assertEquals(7.5, entity.voteAverage, 0.001)
        assertEquals("2014-11-05", entity.releaseDate)
        assertEquals(listOf(878, 18), entity.genreIds)
        assertEquals(88.0, entity.popularity, 0.001)
    }

    @Test
    fun `MovieDto toEntity has empty detail fields by default`() {
        val entity = testMovieDto().toEntity()

        assertEquals("", entity.tagline)
        assertNull(entity.runtime)
        assertEquals("", entity.status)
        assertEquals(0L, entity.budget)
        assertEquals(0L, entity.revenue)
        assertNull(entity.imdbId)
    }

    // --- MovieDetailsDto → MovieEntity ---

    @Test
    fun `MovieDetailsDto toEntity maps detail fields`() {
        val dto = testMovieDetailsDto(movieId = 5)
        val entity = dto.toEntity()

        assertEquals(5, entity.id)
        assertEquals("Test Movie 5", entity.title)
        assertEquals("A tagline", entity.tagline)
        assertEquals(120, entity.runtime)
        assertEquals("Released", entity.status)
        assertEquals(10_000_000L, entity.budget)
        assertEquals(50_000_000L, entity.revenue)
        assertEquals("tt1234567", entity.imdbId)
        assertEquals(listOf(28), entity.genreIds)
    }

    // --- MovieEntity → Movie domain ---

    @Test
    fun `MovieEntity toDomain maps all fields`() {
        val entity = testMovieDto(id = 3, title = "Tenet", genreIds = listOf(28)).toEntity()
        val movie = entity.toDomain()

        assertEquals(3, movie.id)
        assertEquals("Tenet", movie.title)
        assertEquals(listOf(28), movie.genreIds)
    }

    // --- MovieEntity → MovieDetails domain ---

    @Test
    fun `MovieEntity toDetailsDomain resolves genre names from entities`() {
        val entity = testMovieDetailsDto(movieId = 9).toEntity()
        val genreEntities = listOf(GenreEntity(28, "Action"), GenreEntity(12, "Adventure"))

        val details = entity.toDetailsDomain(genreEntities)

        assertEquals(9, details.id)
        assertEquals(1, details.genres.size)
        assertEquals("Action", details.genres.first().name)
    }

    @Test
    fun `MovieEntity toDetailsDomain ignores unmapped genre ids`() {
        val entity = testMovieDetailsDto().toEntity()
        val details = entity.toDetailsDomain(emptyList())

        assertEquals(emptyList<Any>(), details.genres)
    }

    // --- GenreDto → GenreEntity ---

    @Test
    fun `GenreDto toEntity maps id and name`() {
        val entity = GenreDto(id = 35, name = "Comedy").toEntity()

        assertEquals(35, entity.id)
        assertEquals("Comedy", entity.name)
    }
}
