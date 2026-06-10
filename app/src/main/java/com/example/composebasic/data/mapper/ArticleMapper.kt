package com.example.composebasic.data.mapper

import com.example.composebasic.model.Article
import com.example.composebasic.model.Section
import com.example.composebasic.network.Resource

fun Resource<List<Article>>.toSectionResource(): Resource<List<Section>> =
    when (this) {
        is Resource.Success -> Resource.Success(data!!.toSections())
        is Resource.Error -> Resource.Error(message!!, data?.toSections())
        is Resource.Loading -> Resource.Loading(data?.toSections())
    }

fun List<Article>.toSections(): List<Section> =
    groupBy { it.source?.name ?: "Unknown" }
        .map { (sourceName, articles) -> Section(title = sourceName, items = articles) }

fun HashMap<String, out List<Article>>.toSectionList(): List<Section> {
    return map { (title, articles) -> Section(title, articles) }
}