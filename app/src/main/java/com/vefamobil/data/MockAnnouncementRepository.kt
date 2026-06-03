package com.vefamobil.data

import com.vefamobil.model.Announcement
import com.vefamobil.repository.AnnouncementRepository

class MockAnnouncementRepository : AnnouncementRepository {
    private val announcements = mutableListOf(
        Announcement(
            id = "1",
            title = "Yeni Hane Eklendi",
            message = "Hürriyet Mahallesi için yeni hane kaydı oluşturuldu.",
            type = "HOUSEHOLD",
            createdAt = System.currentTimeMillis() - 15 * 60 * 1000,
            isRead = false,
        ),
        Announcement(
            id = "2",
            title = "Hane Pasife Alındı",
            message = "Cumhuriyet Mahallesi içindeki bir hane pasife alındı.",
            type = "HOUSEHOLD",
            createdAt = System.currentTimeMillis() - 45 * 60 * 1000,
            isRead = false,
        ),
        Announcement(
            id = "3",
            title = "Yeni Görev Listesi Oluşturuldu",
            message = "Bugün için saha görev listesi yayınlandı.",
            type = "TASK",
            createdAt = System.currentTimeMillis() - 90 * 60 * 1000,
            isRead = false,
        ),
        Announcement(
            id = "4",
            title = "Maaş İmzası Hatırlatması",
            message = "Personel maaş imza listesi gün içinde tamamlanmalıdır.",
            type = "REMINDER",
            createdAt = System.currentTimeMillis() - 3 * 60 * 60 * 1000,
            isRead = true,
        ),
    )

    override fun getAnnouncements(): List<Announcement> {
        return announcements.sortedByDescending { it.createdAt }
    }

    override fun addAnnouncement(announcement: Announcement) {
        announcements.add(announcement)
    }

    override fun markAsRead(id: String) {
        val index = announcements.indexOfFirst { it.id == id }
        if (index >= 0) {
            announcements[index] = announcements[index].copy(isRead = true)
        }
    }
}
