package com.amro.feature.movies.ui.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.amro.core.designsystem.theme.AmroTheme
import com.amro.core.designsystem.theme.glassSurface
import com.amro.core.designsystem.theme.heroScrim
import com.amro.core.designsystem.theme.primaryGradient
import com.amro.core.model.Movie
import com.amro.feature.movies.R
import com.amro.feature.movies.utils.backdropUrl
import com.amro.feature.movies.utils.posterUrl

@Composable
fun FeaturedHero(
    movie: Movie,
    onPlayClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(440.dp),
    ) {
        AsyncImage(
            model = backdropUrl(movie.backdropPath) ?: posterUrl(movie.posterPath),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(heroScrim()),
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 24.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TagPill(
                    text = stringResource(R.string.hero_trending_now),
                    background = MaterialTheme.colorScheme.tertiary,
                    content = MaterialTheme.colorScheme.onTertiary,
                )
                Text(
                    text = stringResource(R.string.hero_genz_choice),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = movie.title.uppercase(),
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Black,
            )
            if (movie.overview.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                WatchNowButton(onClick = { onPlayClick(movie.id) })
                MyListButton(onClick = { onPlayClick(movie.id) })
            }
        }
    }
}

@Composable
private fun TagPill(text: String, background: Color, content: Color) {
    val shape = RoundedCornerShape(999.dp)
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = content,
        fontWeight = FontWeight.Black,
        modifier = Modifier
            .clip(shape)
            .background(background, shape)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    )
}

@Composable
private fun WatchNowButton(onClick: () -> Unit) {
    val shape = RoundedCornerShape(999.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(shape)
            .background(primaryGradient(), shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
    ) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.action_watch_now),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Black,
        )
    }
}

@Composable
private fun MyListButton(onClick: () -> Unit) {
    val shape = RoundedCornerShape(999.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(shape)
            .glassSurface(shape = shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 14.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.action_my_list),
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            fontWeight = FontWeight.Black,
        )
    }
}

@Suppress("unused")
@Composable
private fun HeroOutlineBorder() {
    Box(
        Modifier
            .fillMaxSize()
            .border(1.dp, Color.White.copy(alpha = 0.04f), RoundedCornerShape(0.dp)),
    )
}

@Preview(name = "FeaturedHero", widthDp = 412, heightDp = 440)
@Composable
private fun FeaturedHeroPreview() {
    AmroTheme(darkTheme = true) {
        FeaturedHero(
            movie = Movie(
                id = 1,
                title = "Interstellar",
                overview = "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
                posterPath = null,
                backdropPath = null,
                voteAverage = 8.6,
                releaseDate = "2014-11-07",
                genreIds = listOf(878, 18),
                popularity = 200.0,
            ),
            onPlayClick = {},
        )
    }
}
