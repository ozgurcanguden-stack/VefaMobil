package com.vefamobil.repository

import com.vefamobil.model.Announcement

interface AnnouncementRepository {
    fun getAnnouncements(): List<Announcement>

    fun addAnnouncement(announcement: Announcement)

    fun markAsRead(id: String)
}
