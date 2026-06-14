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
import com.example.composebasic.interfaces.NewsViewModelContract
import com.example.composebasic.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val classifier: EmotionClassifier,
    private val summarizer: SummaryRepository) : ViewModel(), NewsViewModelContract{


    // 1. Private MutableStateFlow - can be modified inside the ViewModel
    private val _articlesState = MutableStateFlow<Resource<List<Section>>>(Resource.Loading())

    // 2. Public StateFlow - read-only for the UI
    override val articlesState: StateFlow<Resource<List<Section>>> = _articlesState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    override val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    override fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    override fun fetchNews(defaultQuery: String?, loadingStrings: Array<String>?) {
        val searchQuery = _searchQuery.value.ifEmpty { defaultQuery ?: "" }
        viewModelScope.launch {
            _articlesState.value = Resource.Loading(message = loadingStrings?.get(0) ?: "")
            val result = repository.getNewsArticles(searchQuery)
            result.takeIf { it is Resource.Success }?.let { articleResource ->
                articleResource.data?.let { articles ->
                    _articlesState.value = Resource.Loading(message = loadingStrings?.get(1) ?: "") // keep Loading state while classifying
                    val classified = withContext(Dispatchers.Default) {
                        classifyText(articles)
                    }
                    _articlesState.value = classified         // then set result
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
