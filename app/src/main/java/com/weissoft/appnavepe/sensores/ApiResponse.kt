package com.weissoft.appnavepe.sensores

data class ApiResponse(
    val latitude: Double,
    val longitude: Double,
    val pir_state: Boolean, // Opcional, si lo necesitas
    val timestamp: String // Opcional, si lo necesitas
)
