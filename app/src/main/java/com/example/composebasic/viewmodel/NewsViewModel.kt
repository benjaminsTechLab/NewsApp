package com.example.composebasic.viewmodel

import com.example.composebasic.onxx.EmotionClassifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composebasic.data.mapper.toSectionResource
import com.example.composebasic.model.Article
import com.example.composebasic.model.Section
import com.example.composebasic.data.Respository.NewsRepository
import com.example.composebasic.data.Respository.SummaryRepository
import com.example.composebasic.data.mapper.toSectionList
import com.example.composebasic.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val classifier: EmotionClassifier,
    private val summarizer: SummaryRepository) : ViewModel() {

    // 1. Private MutableStateFlow - can be modified inside the ViewModel
    private val _articlesState = MutableStateFlow<Resource<List<Section>>>(Resource.Loading())
    
    // 2. Public StateFlow - read-only for the UI
    val articlesState: StateFlow<Resource<List<Section>>> = _articlesState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        fetchNews()
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun fetchNews(query: String = _searchQuery.value.ifEmpty { "Android" }) {
        viewModelScope.launch {
            _articlesState.value = Resource.Loading()
            val result = repository.getNewsArticles(query)
            result.takeIf { it is Resource.Success }?.let { articleResource ->
                articleResource.data?.let { articles ->
                    _articlesState.value = classifyText(articles)
                }
            } ?: run {
                _articlesState.value = result.toSectionResource()
            }
        }
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
