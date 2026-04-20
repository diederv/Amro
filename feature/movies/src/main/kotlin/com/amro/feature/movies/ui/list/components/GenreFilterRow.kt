package com.amro.feature.movies.ui.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amro.core.model.Genre

@Composable
fun GenreFilterRow(
    genres: List<Genre>,
    selectedGenreId: Int?,
    onGenreSelected: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            FilterChip(
                selected = selectedGenreId == null,
                onClick = { onGenreSelected(null) },
                label = { Text("All") },
            )
        }
        items(genres, key = { it.id }) { genre ->
            FilterChip(
                selected = selectedGenreId == genre.id,
                onClick = { onGenreSelected(genre.id) },
                label = { Text(genre.name) },
            )
        }
    }
}
