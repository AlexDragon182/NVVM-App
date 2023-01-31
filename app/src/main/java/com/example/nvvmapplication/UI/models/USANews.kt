package com.example.nvvmapplication.UI.models

data class USANews(
    val articles: MutableList<Article?>?,
    val status: String?,
    val totalResults: Int?
)