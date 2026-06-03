package com.vefamobil.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.vefamobil.model.Settings

class SettingsViewModel : ViewModel() {
    var settings by mutableStateOf(Settings())
        private set

    fun updateDailyTargetCount(value: String) {
        val count = value.toIntOrNull() ?: 0
        settings = settings.copy(dailyTargetCount = count.coerceAtLeast(0))
    }

    fun updateWorkStartTime(value: String) {
        settings = settings.copy(workStartTime = value)
    }

    fun updateWorkEndTime(value: String) {
        settings = settings.copy(workEndTime = value)
    }

    fun updateSalaryReminderTime(value: String) {
        settings = settings.copy(salaryReminderTime = value)
    }

    fun setBiometricEnabled(enabled: Boolean) {
        settings = settings.copy(biometricEnabled = enabled)
    }

    fun addNeighborhood(neighborhood: String) {
        val trimmedNeighborhood = neighborhood.trim()
        if (trimmedNeighborhood.isBlank()) return
        if (settings.neighborhoodOrder.any { it.equals(trimmedNeighborhood, ignoreCase = true) }) return

        settings = settings.copy(
            neighborhoodOrder = settings.neighborhoodOrder + trimmedNeighborhood,
        )
    }

    fun removeNeighborhood(neighborhood: String) {
        settings = settings.copy(
            neighborhoodOrder = settings.neighborhoodOrder.filterNot { it == neighborhood },
            currentNeighborhoodIndex = settings.currentNeighborhoodIndex
                .coerceAtMost((settings.neighborhoodOrder.size - 2).coerceAtLeast(0)),
        )
    }
}
