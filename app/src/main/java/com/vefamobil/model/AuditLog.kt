package com.vefamobil.model

data class AuditLog(
    val id: String = "",
    val actionType: String = "",
    val entityType: String = "",
    val entityId: String = "",
    val description: String = "",
    val performedBy: String = "",
    val createdAt: String = "",
)
