package com.vefamobil.model

data class Personnel(
    val id: String,
    val fullName: String,
    val username: String,
    val temporaryPassword: String,
    val isActive: Boolean,
    val mustChangePassword: Boolean,
    val createdAt: Long,
)
