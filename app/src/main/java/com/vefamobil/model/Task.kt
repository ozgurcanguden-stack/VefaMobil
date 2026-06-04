package com.vefamobil.model

data class Task(
    val id: String = "",
    val taskDate: String = "",
    val neighborhood: String = "",
    val totalHouseholds: Int = 0,
    val completedCount: Int = 0,
    val status: String = "",
    val createdMode: TaskCreatedMode = TaskCreatedMode.AUTO,
    val publishMode: TaskPublishMode = TaskPublishMode.TODAY,
    val createdAt: Long = 0L,
)

enum class TaskCreatedMode {
    AUTO,
    MANUAL,
}

enum class TaskPublishMode {
    TODAY,
    TOMORROW,
}
