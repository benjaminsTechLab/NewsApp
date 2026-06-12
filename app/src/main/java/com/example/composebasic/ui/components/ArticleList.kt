package com.example.composebasic.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composebasic.model.Section
import com.example.composebasic.ui.theme.ComposeBasicTheme
import com.example.composebasic.ui.theme.Dimensions.paddingLarge
import com.example.composebasic.ui.theme.Dimensions.paddingMedium
import com.example.composebasic.ui.theme.Dimensions.searchBarHeight
import com.yourapp.ui.preview.PreviewData
import kotlin.collections.forEach

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArticleList(
    innerPadding: PaddingValues? = null,
    sections: List<Section>) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = innerPadding?.calculateBottomPadding() ?: 0.dp)) {
        sections.forEach { section ->
            // Header
            stickyHeader {
                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = paddingLarge, vertical = paddingMedium)
                )
            }
            items(section.items) { article ->
                ArticleItem(
                    article,
                    true,
                    {}
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun ArticleList() {
    ComposeBasicTheme( dynamicColor = false) {
        val sections = PreviewData.sections
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            sections.forEach { section ->
                stickyHeader {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = paddingLarge, vertical = paddingMedium)
                    )
                }
                items(section.items) { article ->
                    ArticleItem(
                        article,
                        true,
                        {}
                    )
                }
            }
        }
    }
}

