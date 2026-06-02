package com.example.composebasic.viewmodel

import com.example.composebasic.onxx.EmotionClassifier
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composebasic.model.Article
import com.example.composebasic.network.NewsRepository
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
    private val classifier: EmotionClassifier) : ViewModel() {

    // 1. Private MutableStateFlow - can be modified inside the ViewModel
    private val _articlesState = MutableStateFlow<Resource<List<Article>>>(Resource.Loading())
    
    // 2. Public StateFlow - read-only for the UI
    val articlesState: StateFlow<Resource<List<Article>>> = _articlesState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        fetchNews()
        classifyText()
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun fetchNews(query: String = _searchQuery.value.ifEmpty { "Android" }) {
        viewModelScope.launch {
            _articlesState.value = Resource.Loading()
            val result = repository.getNewsArticles(query)
            _articlesState.value = result
        }
    }

    fun classifyText() {
        val emotion = classifier.classify("I just got promoted, this is amazing!")
        Log.i("EMOTION", "Result: $emotion")
    }
}
