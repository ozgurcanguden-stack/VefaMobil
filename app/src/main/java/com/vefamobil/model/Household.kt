package com.vefamobil.model

data class Household(
    val id: String,
    val refCode: String,
    val neighborhood: String,
    val fullName: String,
    val tcNo: String,
    val phone1: String,
    val phone2: String?,
    val address: String,
    val isActive: Boolean,
    val isNewHousehold: Boolean,
    val isUrgent: Boolean,
    val firstVisitCompleted: Boolean,
)
