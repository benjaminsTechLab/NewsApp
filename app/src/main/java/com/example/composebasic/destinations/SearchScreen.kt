package com.example.composebasic.destinations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.composebasic.model.Section
import com.example.composebasic.network.Resource
import com.example.composebasic.ui.components.ArticleList
import com.example.composebasic.ui.preview.PreviewData
import com.example.composebasic.ui.theme.ComposeBasicTheme
import com.example.composebasic.ui.theme.Dimensions.paddingLarge
import com.example.composebasic.ui.theme.Dimensions.paddingMedium


@Composable
fun SearchScreen(modifier: Modifier = Modifier,
                 state: Resource<List<Section>>) {
    Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when (state) {
                    is Resource.Loading -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(paddingMedium))
                            Text(text = state.message)
                        }
                    }

                    is Resource.Success -> {
                        ArticleList(
                            sections = state.data ?: emptyList())
                    }

                    is Resource.Error -> {
                        Text(
                            text = state.message ?: "An error occurred",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(paddingLarge)
                        )
                    }
                }
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