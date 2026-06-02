package com.example.composebasic.network

import com.example.composebasic.model.Article
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository class that abstracts access to the news data sources.
 */
@Singleton
class NewsRepository @Inject constructor(private val apiService: ApiService) {

    /**
     * Fetches all news articles and wraps the result in a [Resource].
     */
    suspend fun getNewsArticles(query: String = "android"): Resource<List<Article>> {
        return try {
            val response = apiService.getEverything(query)
            Resource.Success(response.articles ?: emptyList())
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    /**
     * Fetches top headlines and wraps the result in a [Resource].
     */
    suspend fun getTopHeadlines(country: String = "us"): Resource<List<Article>> {
        return try {
            val response = apiService.getHeadlines(country)
            Resource.Success(response.articles ?: emptyList())
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }
}
