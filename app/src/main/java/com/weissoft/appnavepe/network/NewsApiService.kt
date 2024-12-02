package com.weissoft.appnavepe.network

import retrofit2.http.GET
import retrofit2.http.Query

// Interfaz que define las llamadas a la API de noticias de Currents
interface NewsApiService {
    @GET("v1/latest-news") // Endpoint para obtener las últimas noticias
    suspend fun getNews(
        @Query("apiKey") apiKey: String = "sL0pVymZLD-DaX-CtA-u4wdEzlALN7MDNEVNXMD4wEU3gDqW", // Tu clave de Currents API
    ): NewsResponse
}


