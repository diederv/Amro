package com.amro.feature.movies.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import com.amro.core.designsystem.theme.AmroTheme
import com.amro.core.designsystem.theme.glassSurface
import com.amro.core.designsystem.theme.heroScrim
import com.amro.core.designsystem.theme.primaryGradient
import com.amro.core.model.Genre
import com.amro.core.model.MovieDetails
import com.amro.feature.movies.R
import com.amro.feature.movies.ui.LocalAnimatedVisibilityScope
import com.amro.feature.movies.ui.LocalSharedTransitionScope
import com.amro.feature.movies.utils.backdropUrl
import com.amro.feature.movies.utils.posterUrl
import org.koin.compose.viewmodel.koinViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    onBack: () -> Unit,
    viewModel: MovieDetailsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                    scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    val tooltipState = rememberTooltipState(isPersistent = false)
                    val scope = rememberCoroutineScope()
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip {
                                Text(stringResource(R.string.brand_full_name))
                            }
                        },
                        state = tooltipState,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.logo_amro),
                            contentDescription = "AMRO",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .height(38.dp)
                                .semantics { heading() }
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                ) {
                                    scope.launch { tooltipState.show() }
                                },
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.content_description_back),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        when (val state = uiState) {
            MovieDetailsUiState.Loading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }

            is MovieDetailsUiState.Content -> DetailContent(
                movieDetails = state.movieDetails,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )

            is MovieDetailsUiState.Error -> ErrorState(
                message = state.message,
                onRetry = viewModel::retry,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun DetailContent(
    movieDetails: MovieDetails,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 0
    }

    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedVisibilityScope = LocalAnimatedVisibilityScope.current
    val sharedHeroModifier = if (sharedTransitionScope != null && animatedVisibilityScope != null) {
        with(sharedTransitionScope) {
            Modifier.sharedElement(
                state = rememberSharedContentState(key = "movie-image-${movieDetails.id}"),
                animatedVisibilityScope = animatedVisibilityScope,
            )
        }
    } else Modifier

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(320.dp)) {
            AsyncImage(
                model = backdropUrl(movieDetails.backdropPath) ?: posterUrl(movieDetails.posterPath),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = sharedHeroModifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
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
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
            ) {
                if (movieDetails.genres.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        movieDetails.genres.take(3).forEach { genre ->
                            Text(
                                text = genre.name.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .glassSurface(shape = RoundedCornerShape(999.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
                Text(
                    text = movieDetails.title.uppercase(),
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.semantics { heading() },
                )
                if (movieDetails.tagline.isNotBlank()) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = movieDetails.tagline.uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            OverviewCard(
                overview = movieDetails.overview.ifBlank { stringResource(R.string.detail_overview_unavailable) },
            )

            Spacer(Modifier.height(16.dp))

            val unknown = stringResource(R.string.status_unknown)
            val statusLabel = stringResource(R.string.detail_stat_status)
            val runtimeLabel = stringResource(R.string.detail_stat_runtime)
            val runtimeValue = movieDetails.runtime?.let {
                stringResource(R.string.detail_stat_runtime_value, it)
            }
            val releaseLabel = stringResource(R.string.detail_stat_release)
            val budgetLabel = stringResource(R.string.detail_stat_budget)
            val revenueLabel = stringResource(R.string.detail_stat_revenue)
            val votesLabel = stringResource(R.string.detail_stat_votes)
            val stats = buildList {
                add(statusLabel to movieDetails.status.ifBlank { unknown }.uppercase())
                if (runtimeValue != null) add(runtimeLabel to runtimeValue)
                add(releaseLabel to movieDetails.releaseDate.take(4).ifBlank { unknown })
                if (movieDetails.budget > 0) add(budgetLabel to currencyFormat.format(movieDetails.budget))
                if (movieDetails.revenue > 0) add(revenueLabel to currencyFormat.format(movieDetails.revenue))
                add(votesLabel to "%,d".format(movieDetails.voteCount))
            }
            StatsStrip(stats = stats)

            Spacer(Modifier.height(16.dp))

            RatingCard(rating = movieDetails.voteAverage)

            Spacer(Modifier.height(20.dp))

            if (movieDetails.imdbId != null) {
                GradientCta(
                    label = stringResource(R.string.action_view_on_imdb),
                    leadingIcon = Icons.Default.PlayArrow,
                    onClick = {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.imdb.com/title/${movieDetails.imdbId}"),
                            ),
                        )
                    },
                )
                Spacer(Modifier.height(12.dp))
            }
            GlassCta(
                label = stringResource(R.string.action_add_to_watchlist),
                leadingIcon = Icons.Default.Add,
                onClick = {},
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun OverviewCard(overview: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(20.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .width(3.dp)
                    .height(16.dp)
                    .background(MaterialTheme.colorScheme.tertiary),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.detail_overview),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Black,
                modifier = Modifier.semantics { heading() },
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = overview,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun StatsStrip(stats: List<Pair<String, String>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        stats.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                row.forEach { (label, value) ->
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = value,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                repeat(3 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun RatingCard(rating: Double) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(20.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(28.dp),
        )
        Spacer(Modifier.width(14.dp))
        Column {
            Text(
                text = "%.1f".format(rating),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Black,
            )
            Text(
                text = stringResource(R.string.detail_imdb_score),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun GradientCta(
    label: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(999.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(primaryGradient(), shape)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Black,
        )
    }
}

@Composable
private fun GlassCta(
    label: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(999.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .glassSurface(shape = shape)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            fontWeight = FontWeight.Black,
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.error_signal_lost),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.tertiary,
            fontWeight = FontWeight.Black,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp),
        )
        Spacer(Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(primaryGradient(), RoundedCornerShape(999.dp))
                .clickable(onClick = onRetry)
                .padding(horizontal = 28.dp, vertical = 14.dp),
        ) {
            Text(
                text = stringResource(R.string.action_retry),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Black,
            )
        }
    }
}

@Preview(name = "DetailContent", widthDp = 412, heightDp = 900)
@Composable
private fun DetailContentPreview() {
    AmroTheme(darkTheme = true) {
        DetailContent(
            movieDetails = MovieDetails(
                id = 1,
                title = "Interstellar",
                overview = "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
                tagline = "Mankind was born on Earth. It was never meant to die here.",
                posterPath = null,
                backdropPath = null,
                voteAverage = 8.6,
                voteCount = 34_000,
                releaseDate = "2014-11-07",
                runtime = 169,
                status = "Released",
                budget = 165_000_000L,
                revenue = 701_700_000L,
                genres = listOf(Genre(878, "Sci-Fi"), Genre(18, "Drama"), Genre(12, "Adventure")),
                imdbId = "tt0816692",
                popularity = 200.0,
            ),
        )
    }
}

@Preview(name = "DetailError", widthDp = 412, heightDp = 600)
@Composable
private fun DetailErrorPreview() {
    AmroTheme(darkTheme = true) {
        ErrorState(
            message = "Failed to load movie details.",
            onRetry = {},
        )
    }
}
