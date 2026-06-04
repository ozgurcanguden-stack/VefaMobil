package com.zgrcan.vefamobil.model

data class Organization(
    val organizationId: String = "",
    val organizationName: String = "",
    val organizationCode: String = "",
    val city: String = "",
    val district: String = "",
    val isActive: Boolean = true,
)
