package com.example.composebasic.model

data class Users(
    val about: String? = null,
    val created: Long,
    val id: String,
    val karma: Int,
    val submitted: List<Int> = emptyList()
)
