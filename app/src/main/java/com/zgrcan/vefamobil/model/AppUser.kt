package com.zgrcan.vefamobil.model

data class AppUser(
    val uid: String = "",
    val email: String = "",
    val fullName: String = "",
    val role: String = "",
    val organizationId: String = "",
    val username: String = "",
    val isActive: Boolean = true,
    val mustChangePassword: Boolean = false,
)
