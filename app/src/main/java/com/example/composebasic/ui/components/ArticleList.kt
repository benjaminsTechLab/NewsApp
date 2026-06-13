package com.example.composebasic.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composebasic.R
import com.example.composebasic.model.Section
import com.example.composebasic.ui.preview.PreviewData
import com.example.composebasic.ui.theme.ComposeBasicTheme
import com.example.composebasic.ui.theme.Dimensions.paddingLarge
import kotlin.collections.forEach

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArticleList(
    innerPadding: PaddingValues? = null,
    sections: List<Section>) {

    val headerType = stringResource(R.string.header_type)
    val itemType = stringResource(R.string.item_type)
    val listState = rememberLazyListState()
    val pinnedHeader = remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo
                .lastOrNull { it.contentType == headerType && it.offset <= 0 }
                ?.key as? String
        }
    }


    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = innerPadding?.calculateBottomPadding() ?: 0.dp),
        state = listState) {
        sections.forEach { section ->
            // Header
            stickyHeader(
                contentType = headerType,
                key = section.title
            ) {
                AnimatedStickyHeader(
                    title = section.title,
                    isHeaderVisible = pinnedHeader.value?.lowercase().equals(section.title.lowercase())
                )
            }
            items(
                section.items,
                contentType = { itemType }
            ) { article ->
                ArticleItem(
                    article,
                    true,
                    {}
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ArticleListPreview() {
    ComposeBasicTheme( dynamicColor = false) {
        ArticleList(
            sections = PreviewData.sections, 
            innerPadding = PaddingValues(paddingLarge)
        )
    }
}

