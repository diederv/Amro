package com.amro.feature.movies.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import com.amro.core.designsystem.theme.AmroTheme
import com.amro.core.designsystem.theme.glassSurface
import com.amro.core.designsystem.theme.primaryGradient
import com.amro.core.model.Genre
import com.amro.core.model.Movie
import com.amro.feature.movies.R
import com.amro.feature.movies.domain.model.SortOption
import com.amro.feature.movies.domain.model.SortOrder
import com.amro.feature.movies.ui.list.components.FeaturedHero
import com.amro.feature.movies.ui.list.components.GenreFilterRow
import com.amro.feature.movies.ui.list.components.SectionHeader
import com.amro.feature.movies.ui.list.components.SortControls
import org.koin.compose.viewmodel.koinViewModel

private val GridEdgePadding = 20.dp
private val GridGutter = 14.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingMoviesScreen(
    onMovieClick: (Int) -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: TrendingMoviesViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { LucidTopBar(onSettingsClick = onSettingsClick) },
    ) { paddingValues ->
        when (val state = uiState) {
            TrendingMoviesUiState.Loading -> LoadingContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )

            is TrendingMoviesUiState.Content -> MovieListContent(
                state = state,
                onMovieClick = onMovieClick,
                onGenreFilter = viewModel::setGenreFilter,
                onSortOption = viewModel::setSortOption,
                onSortOrder = viewModel::setSortOrder,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )

            TrendingMoviesUiState.Empty -> EmptyContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )

            is TrendingMoviesUiState.Error -> ErrorContent(
                message = state.message,
                onRetry = viewModel::refresh,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LucidTopBar(onSettingsClick: () -> Unit) {
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
                        .height(64.dp)
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
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.content_description_settings),
                    tint = Color.Black,
                )
            }
        },
    )
}

@Composable
internal fun MovieListContent(
    state: TrendingMoviesUiState.Content,
    onMovieClick: (Int) -> Unit,
    onGenreFilter: (Int?) -> Unit,
    onSortOption: (com.amro.feature.movies.domain.model.SortOption) -> Unit,
    onSortOrder: (com.amro.feature.movies.domain.model.SortOrder) -> Unit,
    modifier: Modifier = Modifier,
) {
    val featured = state.movies.firstOrNull()
    val gridItems = if (featured != null) state.movies.drop(1) else state.movies

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(GridGutter),
        verticalArrangement = Arrangement.spacedBy(GridGutter),
    ) {
        if (featured != null) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                FeaturedHero(movie = featured, onPlayClick = onMovieClick)
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            GenreFilterRow(
                genres = state.allGenres,
                selectedGenreId = state.selectedGenreId,
                onGenreSelected = onGenreFilter,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            SortControls(
                sortOption = state.sortOption,
                sortOrder = state.sortOrder,
                onSortOption = onSortOption,
                onSortOrder = onSortOrder,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column {
                Spacer(Modifier.height(4.dp))
                SectionHeader(title = stringResource(R.string.section_hot_picks))
                Spacer(Modifier.height(12.dp))
            }
        }
        itemsIndexed(gridItems, key = { _, m -> m.id }) { index, movie ->
            val isStartColumn = index % 2 == 0
            MovieListItem(
                movie = movie,
                genres = state.allGenres,
                onClick = { onMovieClick(movie.id) },
                modifier = Modifier.padding(
                    start = if (isStartColumn) GridEdgePadding else 0.dp,
                    end = if (isStartColumn) 0.dp else GridEdgePadding,
                ),
            )
        }
    }
}

@Composable
internal fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
internal fun EmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.empty_movies_message),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
internal fun ErrorContent(
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
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp),
        )
        Spacer(modifier = Modifier.height(20.dp))
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

private fun previewMovie(id: Int, title: String, voteAverage: Double, genreIds: List<Int>) = Movie(
    id = id,
    title = title,
    overview = "Overview for $title",
    posterPath = null,
    backdropPath = null,
    voteAverage = voteAverage,
    releaseDate = "2024-01-01",
    genreIds = genreIds,
    popularity = 100.0,
)

@Preview(name = "MovieListContent", widthDp = 412, heightDp = 900)
@Composable
private fun MovieListContentPreview() {
    AmroTheme(darkTheme = true) {
        MovieListContent(
            state = TrendingMoviesUiState.Content(
                movies = listOf(
                    previewMovie(1, "Inception", 8.3, listOf(28, 878)),
                    previewMovie(2, "Interstellar", 8.6, listOf(878, 18)),
                    previewMovie(3, "The Dark Knight", 9.0, listOf(28, 80)),
                    previewMovie(4, "Tenet", 7.4, listOf(28, 878)),
                    previewMovie(5, "Dune", 8.0, listOf(878, 12)),
                ),
                allGenres = listOf(
                    Genre(28, "Action"),
                    Genre(878, "Sci-Fi"),
                    Genre(18, "Drama"),
                    Genre(80, "Crime"),
                    Genre(12, "Adventure"),
                ),
                selectedGenreId = null,
                sortOption = SortOption.POPULARITY,
                sortOrder = SortOrder.DESCENDING,
            ),
            onMovieClick = {},
            onGenreFilter = {},
            onSortOption = {},
            onSortOrder = {},
        )
    }
}

@Preview(name = "LoadingContent", widthDp = 412, heightDp = 400)
@Composable
private fun LoadingContentPreview() {
    AmroTheme(darkTheme = true) { LoadingContent() }
}

@Preview(name = "EmptyContent", widthDp = 412, heightDp = 400)
@Composable
private fun EmptyContentPreview() {
    AmroTheme(darkTheme = true) { EmptyContent() }
}

@Preview(name = "ErrorContent", widthDp = 412, heightDp = 400)
@Composable
private fun ErrorContentPreview() {
    AmroTheme(darkTheme = true) {
        ErrorContent(
            message = "Failed to load movies. Check your connection.",
            onRetry = {},
        )
    }
}
