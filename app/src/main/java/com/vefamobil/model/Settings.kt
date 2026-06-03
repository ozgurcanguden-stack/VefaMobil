package com.vefamobil.model

data class Settings(
    val dailyTargetCount: Int = 6,
    val workStartTime: String = "08:30",
    val workEndTime: String = "17:30",
    val biometricEnabled: Boolean = false,
    val salaryReminderTime: String = "08:20",
    val notificationSoundSalary: String = "maas_imza_default",
    val neighborhoodOrder: List<String> = listOf("Hürriyet", "Cumhuriyet", "Acarlar"),
    val currentNeighborhoodIndex: Int = 0,
)
