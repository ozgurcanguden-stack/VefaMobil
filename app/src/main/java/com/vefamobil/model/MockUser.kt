package com.vefamobil.model

data class MockUser(
    val id: String,
    val displayName: String,
    val role: UserRole,
    val mustChangePassword: Boolean = false,
)
