package com.amro.feature.movies.ui.list

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.amro.core.model.Genre
import com.amro.core.model.Movie
import com.amro.feature.movies.ui.LocalAnimatedVisibilityScope
import com.amro.feature.movies.ui.LocalSharedTransitionScope
import com.amro.feature.movies.utils.posterUrl

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MovieListItem(
    movie: Movie,
    genres: List<Genre>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val genreNames = genres.filter { it.id in movie.genreIds }.map { it.name }
    val semanticLabel = buildString {
        append(movie.title)
        if (genreNames.isNotEmpty()) append(", ${genreNames.joinToString()}")
        append(", rated %.1f".format(movie.voteAverage))
    }

    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedVisibilityScope = LocalAnimatedVisibilityScope.current

    val sharedImageModifier = if (sharedTransitionScope != null && animatedVisibilityScope != null) {
        with(sharedTransitionScope) {
            Modifier.sharedElement(
                state = rememberSharedContentState(key = "movie-image-${movie.id}"),
                animatedVisibilityScope = animatedVisibilityScope,
            )
        }
    } else Modifier

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .clearAndSetSemantics { contentDescription = semanticLabel },
    ) {
        Row(modifier = Modifier.height(120.dp)) {
            AsyncImage(
                model = posterUrl(movie.posterPath),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = sharedImageModifier
                    .width(80.dp)
                    .fillMaxHeight(),
            )

            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .weight(1f),
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "%.1f".format(movie.voteAverage),
                        style = MaterialTheme.typography.bodySmall,
                    )
                    if (movie.releaseDate.length >= 4) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = movie.releaseDate.take(4),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    genreNames.take(2).forEach { name ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text(name, maxLines = 1) },
                            modifier = Modifier.height(24.dp),
                        )
                    }
                }
            }
        }
    }
}
