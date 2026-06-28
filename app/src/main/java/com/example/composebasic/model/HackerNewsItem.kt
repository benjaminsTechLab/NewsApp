package com.example.composebasic.model

data class HackerNewsItem(
    val by: String,
    val id: Long,
    val kids: List<Int> = emptyList(),
    val parent: Int? = null,
    val text: String? = null,
    val time: Long,
    val type: String,
    val descendants: Int? = null,
    val title: String? = null,
    val url: String? = null,
    val score: Int? = null
)
