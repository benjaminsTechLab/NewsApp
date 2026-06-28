package com.example.composebasic.network

import com.example.composebasic.model.HackerNewsItem
import com.example.composebasic.model.SourceResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/v0/item/{id}.json")
    suspend fun getItem(@Path("id") id: Long): HackerNewsItem

    @GET("/v0/topstories")
    suspend fun getTopStories(): SourceResponse

    @GET("/v0/newstories")
    suspend fun getNewStories(): SourceResponse

    @GET("/v0/beststories")
    suspend fun getBestStories(): SourceResponse

    @GET("/v0/askstories")
    suspend fun getAskStories(): SourceResponse

    @GET("/v0/showstories")
    suspend fun getShowStories(): SourceResponse

    @GET("/v0/jobstories")
    suspend fun getJobStories(): SourceResponse

    suspend fun getAllStories(query: String): List<HackerNewsItem> {
        return coroutineScope {
            val sourceResponseList = listOf(
                async { getTopStories() },
                async { getNewStories() },
                async { getBestStories() }
            ).awaitAll()

            val ids = sourceResponseList.distinct().map { response -> response.source?.map { it.id } ?: 0 }
            ids.map { id -> async { getItem(id as Long) }}
                .awaitAll()
                .filter  { it.title?.contains(query, ignoreCase = true) == true }
        }
    }
}