package com.example.composebasic.ui.preview

import com.example.composebasic.interfaces.NewsViewModelContract
import com.example.composebasic.model.Section
import com.example.composebasic.network.Resource
import kotlinx.coroutines.flow.MutableStateFlow

class FakeNewsViewModel(
    articles: Resource<List<Section>> = Resource.Success(PreviewData.sections),
    query: String = "",
) : NewsViewModelContract {
    override val articlesState = MutableStateFlow(articles)
    override val searchQuery = MutableStateFlow(query)
    override val recentSearches = MutableStateFlow(listOf("Android", "Kotlin", "Jetpack Compose"))
    override fun onSearchQueryChange(query: String) {}
    override fun fetchNews(defaultQuery: String?, loadingStrings: Array<String>?) {}
    override fun addRecentSearch(query: String) {}
}