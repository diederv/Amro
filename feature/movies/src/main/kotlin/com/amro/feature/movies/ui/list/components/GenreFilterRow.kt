package com.amro.feature.movies.ui.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amro.core.designsystem.theme.AmroTheme
import com.amro.core.model.Genre
import com.amro.feature.movies.R

@Composable
fun GenreFilterRow(
    genres: List<Genre>,
    selectedGenreId: Int?,
    onGenreSelected: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            NeonChip(
                label = stringResource(R.string.chip_for_you),
                selected = selectedGenreId == null,
                onClick = { onGenreSelected(null) },
            )
        }
        items(genres, key = { it.id }) { genre ->
            NeonChip(
                label = genre.name.uppercase(),
                selected = selectedGenreId == genre.id,
                onClick = { onGenreSelected(genre.id) },
            )
        }
    }
}

@Composable
private fun NeonChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val bg = if (selected)
        MaterialTheme.colorScheme.secondary
    else
        MaterialTheme.colorScheme.surfaceContainerHigh
    val fg = if (selected)
        MaterialTheme.colorScheme.onSecondary
    else
        MaterialTheme.colorScheme.onSurfaceVariant
    val shape = RoundedCornerShape(999.dp)
    Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        color = fg,
        modifier = Modifier
            .clip(shape)
            .background(bg, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 10.dp),
    )
}

@Preview(name = "GenreFilterRow", widthDp = 380, heightDp = 80)
@Composable
private fun GenreFilterRowPreview() {
    AmroTheme(darkTheme = true) {
        GenreFilterRow(
            genres = listOf(
                Genre(28, "Action"),
                Genre(878, "Sci-Fi"),
                Genre(18, "Drama"),
                Genre(80, "Crime"),
            ),
            selectedGenreId = 28,
            onGenreSelected = {},
        )
    }
}
