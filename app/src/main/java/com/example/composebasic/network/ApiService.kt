package com.example.composebasic.network

import com.example.composebasic.model.ArticleResponse
import com.example.composebasic.model.SourceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything")
    suspend fun getEverything(@Query("q") query: String): ArticleResponse

    @GET("top-headlines")
    suspend fun getHeadlines(@Query("country") country: String): ArticleResponse

    @GET("top-headlines/sources")
    suspend fun getTopSources(): SourceResponse
}