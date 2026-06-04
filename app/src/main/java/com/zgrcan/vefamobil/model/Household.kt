package com.zgrcan.vefamobil.model

import com.google.firebase.Timestamp

data class Household(
    val id: String = "",
    val organizationId: String = "",
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
    val isDeleted: Boolean = false,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,
    val deletedAt: Timestamp? = null,
    val createdBy: String = "",
)
