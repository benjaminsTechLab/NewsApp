package com.example.composebasic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.composebasic.network.Resource
import com.example.composebasic.ui.components.ArticleList
import com.example.composebasic.ui.components.SearchBar
import com.example.composebasic.ui.theme.ComposeBasicTheme
import com.example.composebasic.ui.theme.Dimensions.paddingLarge
import com.example.composebasic.ui.theme.Dimensions.paddingMedium
import com.example.composebasic.viewmodel.NewsViewModel
import com.yourapp.ui.preview.PreviewData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: NewsViewModel = hiltViewModel()

            ComposeBasicTheme(dynamicColor = false) {
                NewsScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(viewModel: NewsViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.articlesState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val loadingStrings = arrayOf(
        stringResource(R.string.loading_fetching),
        stringResource(R.string.loading_classifying),
    )
    val defaultQuery = stringResource(R.string.default_query)

    Scaffold(modifier = Modifier
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    SearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = paddingLarge, bottom = paddingLarge, end = paddingLarge)
                            .clip(RoundedCornerShape(50.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = paddingLarge, vertical = paddingLarge),
                        query = searchQuery,
                        onQueryChange = { viewModel.onSearchQueryChange(it) },
                        onSearchText = { viewModel.fetchNews(
                                defaultQuery = defaultQuery,
                                loadingStrings = loadingStrings
                            )
                        }
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
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
                            innerPadding = innerPadding,
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
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun NewsScreen() {
    val state = Resource.Success(PreviewData.sections)
    val searchQuery = "Android"
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(modifier = Modifier
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    SearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = paddingLarge, bottom = paddingLarge, end = paddingLarge)
                            .clip(RoundedCornerShape(50.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = paddingLarge, vertical = paddingLarge),
                        query = searchQuery,
                        onQueryChange = {},
                        onSearchText = {}
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when (state) {
                    is Resource.Loading<*> -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(paddingMedium))
                            Text(text = "Loading")
                        }
                    }

                    is Resource.Success -> {
                        ArticleList(
                            innerPadding = innerPadding,
                            sections = state.data ?: emptyList())
                    }

                    is Resource.Error<*> -> {
                        Text(
                            text = "An error occurred",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(paddingLarge)
                        )
                    }
                }
            }
        }
    }
}
