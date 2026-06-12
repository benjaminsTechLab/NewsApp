package com.example.composebasic.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import com.example.composebasic.model.Section
import com.example.composebasic.ui.theme.Dimensions.searchBarHeight
import kotlin.math.roundToInt


@Composable
fun SearchBarWithScroll(
    modifier: Modifier = Modifier,
    sectionList: List<Section>,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchText: () -> Unit,
) {
    val listState = rememberLazyListState()
    val searchBarHeightPx = with(LocalDensity.current) { searchBarHeight.toPx() }

    // Track scroll direction
    var searchBarOffset by remember { mutableFloatStateOf(0f) }
    var lastFirstVisibleIndex by remember { mutableIntStateOf(0) }
    var lastFirstVisibleOffset by remember { mutableIntStateOf(0) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                val scrollingDown = index > lastFirstVisibleIndex ||
                        (index == lastFirstVisibleIndex && offset > lastFirstVisibleOffset)

                searchBarOffset = if (scrollingDown) {
                    (searchBarOffset + searchBarHeightPx).coerceAtMost(searchBarHeightPx)
                } else {
                    (searchBarOffset - searchBarHeightPx).coerceAtLeast(0f)
                }

                lastFirstVisibleIndex = index
                lastFirstVisibleOffset = offset
            }
    }

    val animatedOffset by animateFloatAsState(
        targetValue = searchBarOffset,
        animationSpec = tween(200),
        label = "searchBarOffset"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        ArticleList(
            sections = sectionList)
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(0, -animatedOffset.roundToInt()) },
            query,
            onQueryChange,
            onSearchText
        )
    }
}
