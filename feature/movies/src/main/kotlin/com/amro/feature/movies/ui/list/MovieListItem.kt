package com.amro.feature.movies.ui.list

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.amro.core.designsystem.theme.AmroTheme
import com.amro.core.designsystem.theme.glassSurface
import com.amro.core.model.Genre
import com.amro.core.model.Movie
import com.amro.feature.movies.R
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
    val ratingText = "%.1f".format(movie.voteAverage)
    val semanticLabel = if (genreNames.isNotEmpty()) {
        stringResource(
            R.string.content_description_movie_card_with_genres,
            movie.title,
            genreNames.joinToString(),
            ratingText,
        )
    } else {
        stringResource(R.string.content_description_movie_card, movie.title, ratingText)
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

    val tileShape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(2f / 3f)
            .clip(tileShape)
            .clickable(onClick = onClick)
            .clearAndSetSemantics { contentDescription = semanticLabel },
    ) {
        AsyncImage(
            model = posterUrl(movie.posterPath),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = sharedImageModifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to Color.Transparent,
                            0.55f to Color.Transparent,
                            1f to Color.Black.copy(alpha = 0.85f),
                        ),
                    ),
                ),
        )

        RatingBadge(
            rating = movie.voteAverage,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp),
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(10.dp)
                .glassSurface(shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp),
        ) {
            Text(
                text = movie.title.uppercase(),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Black,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (genreNames.isNotEmpty()) {
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    text = genreNames.take(2).joinToString(" • "),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun RatingBadge(rating: Double, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black.copy(alpha = 0.55f))
            .padding(horizontal = 6.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = MaterialTheme.colorScheme.secondary,
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = "%.1f".format(rating),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
        )
    }
}

@Preview(name = "MovieListItem", widthDp = 180, heightDp = 270)
@Composable
private fun MovieListItemPreview() {
    AmroTheme(darkTheme = true) {
        Box(modifier = Modifier.width(180.dp)) {
            MovieListItem(
                movie = Movie(
                    id = 1,
                    title = "Inception",
                    overview = "",
                    posterPath = null,
                    backdropPath = null,
                    voteAverage = 8.3,
                    releaseDate = "2010-07-16",
                    genreIds = listOf(28, 878),
                    popularity = 100.0,
                ),
                genres = listOf(Genre(28, "Action"), Genre(878, "Sci-Fi")),
                onClick = {},
            )
        }
    }
}
