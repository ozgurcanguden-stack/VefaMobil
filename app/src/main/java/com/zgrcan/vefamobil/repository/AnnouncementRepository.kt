package com.zgrcan.vefamobil.repository

import com.zgrcan.vefamobil.model.Announcement

interface AnnouncementRepository {
    fun getAnnouncements(): List<Announcement>

    fun addAnnouncement(announcement: Announcement)

    fun markAsRead(id: String)
}
