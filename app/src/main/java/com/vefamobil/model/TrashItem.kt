package com.vefamobil.model

data class TrashItem(
    val id: String,
    val entityType: String,
    val entityId: String,
    val title: String,
    val description: String,
    val deletedBy: String,
    val deletedAt: String,
    val canRestore: Boolean,
)
