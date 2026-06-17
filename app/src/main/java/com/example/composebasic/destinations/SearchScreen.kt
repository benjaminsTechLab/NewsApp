package com.example.composebasic.destinations

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.composebasic.model.Section
import com.example.composebasic.network.Resource
import com.example.composebasic.ui.components.ArticleList
import com.example.composebasic.ui.components.ErrorState
import com.example.composebasic.ui.components.LoadingState
import com.example.composebasic.ui.preview.PreviewData
import com.example.composebasic.ui.theme.ComposeBasicTheme


@Composable
fun SearchScreen(modifier: Modifier = Modifier,
                 state: Resource<List<Section>>) {
        when (state) {
            is Resource.Loading -> {
                LoadingState(modifier, state.message)
            }

            is Resource.Success -> {
                ArticleList(
                    sections = state.data ?: emptyList())
            }

            is Resource.Error -> {
                ErrorState(message = state.message)
            }
        }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    ComposeBasicTheme(dynamicColor = false) {
        SearchScreen(state = Resource.Success(PreviewData.sections))
    }
}