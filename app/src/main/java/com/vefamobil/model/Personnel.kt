package com.vefamobil.model

data class Personnel(
    val id: String = "",
    val fullName: String = "",
    val username: String = "",
    val temporaryPassword: String = "",
    val isActive: Boolean = true,
    val mustChangePassword: Boolean = true,
    val createdAt: Long = 0L,
)
