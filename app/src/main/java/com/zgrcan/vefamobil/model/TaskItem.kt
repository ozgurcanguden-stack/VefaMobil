package com.zgrcan.vefamobil.model

data class TaskItem(
    val id: String = "",
    val taskId: String = "",
    val householdId: String = "",
    val neighborhood: String = "",
    val householdName: String = "",
    val refCode: String = "",
    val phone1: String = "",
    val phone2: String? = null,
    val address: String = "",
    val status: TaskItemStatus = TaskItemStatus.PENDING,
    val isUrgent: Boolean = false,
    val isNewHousehold: Boolean = false,
    val note: String = "",
)

enum class TaskItemStatus {
    PENDING,
    DONE,
    NOT_DONE,
}
