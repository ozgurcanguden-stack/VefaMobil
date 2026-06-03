package com.vefamobil.model

data class TaskItem(
    val id: String,
    val taskId: String,
    val householdId: String,
    val neighborhood: String,
    val householdName: String,
    val refCode: String,
    val phone1: String,
    val phone2: String?,
    val address: String,
    val status: TaskItemStatus,
    val isUrgent: Boolean,
    val isNewHousehold: Boolean,
    val note: String,
)

enum class TaskItemStatus {
    PENDING,
    DONE,
    NOT_DONE,
}
