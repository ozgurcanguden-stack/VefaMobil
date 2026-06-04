package com.zgrcan.vefamobil.model

data class Household(
    val id: String = "",
    val refCode: String = "",
    val neighborhood: String = "",
    val fullName: String = "",
    val tcNo: String = "",
    val phone1: String = "",
    val phone2: String? = null,
    val address: String = "",
    val isActive: Boolean = true,
    val isNewHousehold: Boolean = false,
    val isUrgent: Boolean = false,
    val firstVisitCompleted: Boolean = false,
)
