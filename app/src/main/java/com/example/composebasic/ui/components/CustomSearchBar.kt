package com.example.composebasic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composebasic.ui.theme.ComposeBasicTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchText: () -> Unit,
    recentSearches: List<String>,
    onSuggestionClick: (String) -> Unit,
    onRemoveSuggestion: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    val suggestions = if (query.isEmpty()) {
        recentSearches
    } else {
        recentSearches.filter { it.contains(query, ignoreCase = true) }
    }

    SearchBar(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it },
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = { _ ->
                    expanded = false
                    onSearchText()
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = {
                    Text(
                        text = "Search",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
            )
        }
    ) {
        if (suggestions.isNotEmpty()) {
            LazyColumn {
                items(suggestions) { suggestion ->
                    ListItem(
                        headlineContent = { Text(suggestion) },
                        leadingContent = {
                            Icon(Icons.Default.Search, contentDescription = null)
                        },
                        trailingContent = {
                            IconButton(onClick = { onRemoveSuggestion(suggestion) }) {
                                Icon(Icons.Default.Close, contentDescription = "Remove")
                            }
                        },
                        modifier = Modifier.clickable {
                            onQueryChange(suggestion)
                            onSuggestionClick(suggestion)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomSearchBarPreview() {
    ComposeBasicTheme(dynamicColor = false) {
        CustomSearchBar(
            modifier = Modifier,
            query = "Android",
            onSearchText =  {},
            onQueryChange = {},
            recentSearches = listOf("Kotlin", "Compose", "Android"),
            onSuggestionClick = {},
            onRemoveSuggestion = {}
        )
    }
}
