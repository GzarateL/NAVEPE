package com.weissoft.appnavepe.network

data class NewsResponse(
    val status: String,
    val news: List<Article>
)

data class Article(
    val title: String,
    val description: String?,
    val image: String?,
    val url: String
)

