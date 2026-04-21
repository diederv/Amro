package com.amro.feature.movies.ui.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amro.core.designsystem.theme.AmroTheme
import com.amro.feature.movies.R
import com.amro.feature.movies.domain.model.SortOption
import com.amro.feature.movies.domain.model.SortOrder

@Composable
fun SortControls(
    sortOption: SortOption,
    sortOrder: SortOrder,
    onSortOption: (SortOption) -> Unit,
    onSortOrder: (SortOrder) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        var expanded by remember { mutableStateOf(false) }
        val shape = RoundedCornerShape(999.dp)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(shape)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh, shape)
                .border(1.dp, Color.White.copy(alpha = 0.06f), shape)
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 10.dp),
        ) {
            Text(
                text = stringResource(R.string.sort_label, sortOption.displayName().uppercase()),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp),
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                SortOption.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.displayName()) },
                        onClick = {
                            onSortOption(option)
                            expanded = false
                        },
                    )
                }
            }
        }

        val orderShape = RoundedCornerShape(999.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(orderShape)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh, orderShape)
                .border(1.dp, Color.White.copy(alpha = 0.06f), orderShape)
                .clickable { onSortOrder(sortOrder.toggled()) }
                .padding(horizontal = 14.dp, vertical = 10.dp),
        ) {
            Icon(
                imageVector = if (sortOrder == SortOrder.ASCENDING)
                    Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (sortOrder == SortOrder.ASCENDING)
                    stringResource(R.string.content_description_sort_ascending)
                else
                    stringResource(R.string.content_description_sort_descending),
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@Composable
fun SortOption.displayName(): String = stringResource(
    when (this) {
        SortOption.POPULARITY -> R.string.sort_option_popularity
        SortOption.TITLE -> R.string.sort_option_title
        SortOption.RELEASE_DATE -> R.string.sort_option_release_date
    },
)

fun SortOrder.toggled(): SortOrder =
    if (this == SortOrder.ASCENDING) SortOrder.DESCENDING else SortOrder.ASCENDING

@Preview(name = "SortControls", widthDp = 380, heightDp = 80)
@Composable
private fun SortControlsPreview() {
    AmroTheme(darkTheme = true) {
        SortControls(
            sortOption = SortOption.POPULARITY,
            sortOrder = SortOrder.DESCENDING,
            onSortOption = {},
            onSortOrder = {},
        )
    }
}
