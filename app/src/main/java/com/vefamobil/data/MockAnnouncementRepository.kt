package com.vefamobil.data

import com.vefamobil.data.mock.MockDataStore
import com.vefamobil.model.Announcement
import com.vefamobil.repository.AnnouncementRepository

class MockAnnouncementRepository : AnnouncementRepository {
    override fun getAnnouncements(): List<Announcement> {
        return MockDataStore.announcements.sortedByDescending { it.createdAt }
    }

    override fun addAnnouncement(announcement: Announcement) {
        MockDataStore.announcements.add(announcement)
    }

    override fun markAsRead(id: String) {
        val index = MockDataStore.announcements.indexOfFirst { it.id == id }
        if (index >= 0) {
            MockDataStore.announcements[index] = MockDataStore.announcements[index].copy(isRead = true)
        }
    }
}
