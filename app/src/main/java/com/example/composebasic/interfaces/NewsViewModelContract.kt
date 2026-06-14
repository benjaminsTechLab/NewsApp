package com.example.composebasic.interfaces

import com.example.composebasic.model.Section
import com.example.composebasic.network.Resource
import kotlinx.coroutines.flow.StateFlow

interface NewsViewModelContract {
    val articlesState: StateFlow<Resource<List<Section>>>
    val searchQuery: StateFlow<String>
    fun onSearchQueryChange(query: String)
    fun fetchNews(defaultQuery: String?, loadingStrings: Array<String>?)
}