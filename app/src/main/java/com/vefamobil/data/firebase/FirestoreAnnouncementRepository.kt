package com.vefamobil.data.firebase

import com.vefamobil.model.Announcement
import com.vefamobil.repository.AnnouncementRepository

class FirestoreAnnouncementRepository : AnnouncementRepository {
    override fun getAnnouncements(): List<Announcement> {
        // TODO: Firestore announcements collection will be read here.
        return emptyList()
    }

    override fun addAnnouncement(announcement: Announcement) {
        // TODO: Firestore add announcement implementation.
    }

    override fun markAsRead(id: String) {
        // TODO: Firestore read state update implementation.
    }
}
