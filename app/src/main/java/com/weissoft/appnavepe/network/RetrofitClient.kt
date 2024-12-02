package com.weissoft.appnavepe.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Base URL correcta para la API de Currents
    private const val BASE_URL = "https://api.currentsapi.services/"

    val apiService: NewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Para convertir las respuestas JSON a objetos Kotlin
            .build()
            .create(NewsApiService::class.java) // Crea la instancia del servicio de la API
    }
}

