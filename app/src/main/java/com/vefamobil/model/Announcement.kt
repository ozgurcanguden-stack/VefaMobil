package com.vefamobil.model

data class Announcement(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val type: String = "",
    val createdAt: Long = 0L,
    val isRead: Boolean = false,
)
