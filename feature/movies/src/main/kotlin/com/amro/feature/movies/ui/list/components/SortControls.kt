package com.amro.feature.movies.ui.list.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amro.feature.movies.domain.model.SortOption
import com.amro.feature.movies.domain.model.SortOrder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortControls(
    sortOption: SortOption,
    sortOrder: SortOrder,
    onSortOption: (SortOption) -> Unit,
    onSortOrder: (SortOrder) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.weight(1f),
        ) {
            OutlinedTextField(
                value = sortOption.displayName,
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                label = { Text("Sort by") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                SortOption.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.displayName) },
                        onClick = {
                            onSortOption(option)
                            expanded = false
                        },
                    )
                }
            }
        }

        IconButton(
            onClick = { onSortOrder(sortOrder.toggled()) },
        ) {
            Icon(
                imageVector = if (sortOrder == SortOrder.ASCENDING)
                    Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (sortOrder == SortOrder.ASCENDING)
                    "Sort ascending" else "Sort descending",
            )
        }
    }
}

val SortOption.displayName: String
    get() = when (this) {
        SortOption.POPULARITY -> "Popularity"
        SortOption.TITLE -> "Title"
        SortOption.RELEASE_DATE -> "Release Date"
    }

fun SortOrder.toggled(): SortOrder =
    if (this == SortOrder.ASCENDING) SortOrder.DESCENDING else SortOrder.ASCENDING
