package com.example.composebasic.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composebasic.model.Section
import com.example.composebasic.ui.theme.ComposeBasicTheme
import com.example.composebasic.ui.preview.PreviewData
import kotlin.collections.forEach

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArticleList(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    sections: List<Section> = emptyList()
) {

    val listState = rememberLazyListState()
    val isHeaderVisible = listState.firstVisibleItemIndex == 0

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = innerPadding.calculateBottomPadding()),
        state = listState) {
        sections.forEach { section ->
            // Header
            stickyHeader {
                AnimatedStickyHeader(
                    title = section.title,
                    isHeaderVisible
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
fun ArticleListPreview() {
    ComposeBasicTheme(dynamicColor = false) {
        val isHeaderVisible = true
        val sections = PreviewData.sections

        LazyColumn(modifier = Modifier
            .fillMaxSize()) {
            sections.forEach { section ->
                // Header
                stickyHeader {
                    Text(
                        text = section.title,

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
