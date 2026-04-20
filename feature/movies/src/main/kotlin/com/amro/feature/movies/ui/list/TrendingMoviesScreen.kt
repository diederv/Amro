package com.amro.feature.movies.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amro.feature.movies.ui.list.components.GenreFilterRow
import com.amro.feature.movies.ui.list.components.SortControls
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingMoviesScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: TrendingMoviesViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "AMRO",
                        modifier = Modifier.semantics { heading() },
                    )
                },
            )
        },
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

@Composable
internal fun MovieListContent(
    state: TrendingMoviesUiState.Content,
    onMovieClick: (Int) -> Unit,
    onGenreFilter: (Int?) -> Unit,
    onSortOption: (com.amro.feature.movies.domain.model.SortOption) -> Unit,
    onSortOrder: (com.amro.feature.movies.domain.model.SortOrder) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        GenreFilterRow(
            genres = state.allGenres,
            selectedGenreId = state.selectedGenreId,
            onGenreSelected = onGenreFilter,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        )
        SortControls(
            sortOption = state.sortOption,
            sortOrder = state.sortOrder,
            onSortOption = onSortOption,
            onSortOrder = onSortOrder,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(state.movies, key = { it.id }) { movie ->
                MovieListItem(
                    movie = movie,
                    genres = state.allGenres,
                    onClick = { onMovieClick(movie.id) },
                )
            }
        }
    }
}

@Composable
internal fun LoadingContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
internal fun EmptyContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = "No movies match your filter.\nTry a different genre.",
            style = MaterialTheme.typography.bodyLarge,
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
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}
