package com.vefamobil.model

data class Settings(
    val dailyTargetCount: Int = 6,
    val neighborhoodOrder: List<String> = listOf("Hürriyet", "Cumhuriyet", "Acarlar"),
    val currentNeighborhoodIndex: Int = 0,
)
