package com.example.composebasic.viewmodel

import com.example.composebasic.onxx.EmotionClassifier
import androidx.lifecycle.ViewModel
import com.example.composebasic.model.Article
import com.example.composebasic.model.Section
import com.example.composebasic.data.Respository.NewsRepository
import com.example.composebasic.data.Respository.SummaryRepository
import com.example.composebasic.data.mapper.toSectionList
import com.example.composebasic.interfaces.NewsViewModelContract
import com.example.composebasic.network.ApiService
import com.example.composebasic.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.flatten


@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val classifier: EmotionClassifier,
    private val summarizer: SummaryRepository,
    private val api: ApiService) : ViewModel(), NewsViewModelContract{


    // 1. Private MutableStateFlow - can be modified inside the ViewModel
    private val _articlesState = MutableStateFlow<Resource<List<Section>>>(Resource.Loading())

    // 2. Public StateFlow - read-only for the UI
    override val articlesState: StateFlow<Resource<List<Section>>> = _articlesState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    override val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())
    override val recentSearches: StateFlow<List<String>> = _recentSearches

    override fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    override fun addRecentSearch(query: String) {
        if (query.isBlank()) return
        _recentSearches.update { current ->
            listOf(query) + current.filterNot { it == query }.take(4) // keep 5 max, no dupes
        }
    }

    override suspend fun fetchNews() {
        val newsStories = api.getAllStories(query)
    }

    fun classifyText(articles: List<Article>): Resource<List<Section>>{
        val map = HashMap<String, MutableList<Article>>()
        for(article in articles) {
            article.description?.let {
                map.getOrPut(classifier.classify(it)) { mutableListOf() }.add(article)
            }
        }
        return Resource.Success(map.toSectionList())
    }
}
