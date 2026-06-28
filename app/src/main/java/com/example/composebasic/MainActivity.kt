package com.example.composebasic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.composebasic.destinations.FavoriteScreen
import com.example.composebasic.destinations.HomeScreen
import com.example.composebasic.destinations.Screen
import com.example.composebasic.destinations.SearchScreen
import com.example.composebasic.destinations.bottomNavItems
import com.example.composebasic.interfaces.NewsViewModelContract
import com.example.composebasic.ui.components.CustomSearchBar
import com.example.composebasic.ui.preview.FakeNewsViewModel
import com.example.composebasic.ui.theme.ComposeBasicTheme
import com.example.composebasic.ui.theme.Dimensions.paddingLarge
import com.example.composebasic.viewmodel.NewsViewModel
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
fun NewsScreen(viewModel: NewsViewModelContract, modifier: Modifier = Modifier) {
    val state by viewModel.articlesState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val loadingStrings = arrayOf(
        stringResource(R.string.loading_fetching),
        stringResource(R.string.loading_classifying),
    )
    val defaultQuery = stringResource(R.string.default_query)
    val recentSearches by viewModel.recentSearches.collectAsStateWithLifecycle()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(modifier = Modifier
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (currentRoute == Screen.Search.route) {
                TopAppBar(
                    title = {
                        CustomSearchBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = paddingLarge,
                                    bottom = paddingLarge,
                                    end = paddingLarge
                                )
                                .clip(RoundedCornerShape(50.dp)),
                            query = searchQuery,
                            onQueryChange = { viewModel.onSearchQueryChange(it) },
                            onSearchText = { viewModel.fetchNews() },
                            recentSearches = recentSearches,
                            onSuggestionClick = {},
                            onRemoveSuggestion = {}
                        )
                    },
                    scrollBehavior = scrollBehavior,
                )
            }
        },
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Search.route) { SearchScreen(state = state) }
            composable(Screen.Favorite.route) { FavoriteScreen() }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun NewsScreenPreview() {
    ComposeBasicTheme(dynamicColor = false) {
        NewsScreen(FakeNewsViewModel())
    }
}
