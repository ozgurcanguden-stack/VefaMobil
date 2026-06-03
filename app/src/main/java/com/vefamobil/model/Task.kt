package com.vefamobil.model

data class Task(
    val id: String,
    val taskDate: String,
    val neighborhood: String,
    val totalHouseholds: Int,
    val completedCount: Int,
    val status: String,
    val createdMode: TaskCreatedMode,
    val publishMode: TaskPublishMode,
    val createdAt: Long,
)

enum class TaskCreatedMode {
    AUTO,
    MANUAL,
}

enum class TaskPublishMode {
    TODAY,
    TOMORROW,
}
