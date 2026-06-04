package com.zgrcan.vefamobil.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zgrcan.vefamobil.data.MockAnnouncementRepository
import com.zgrcan.vefamobil.model.Announcement
import com.zgrcan.vefamobil.repository.AnnouncementRepository

data class AnnouncementUiState(
    val announcements: List<Announcement> = emptyList(),
)

class AnnouncementViewModel : ViewModel() {
    private val announcementRepository: AnnouncementRepository = MockAnnouncementRepository()

    var state by mutableStateOf(AnnouncementUiState())
        private set

    init {
        loadAnnouncements()
    }

    fun loadAnnouncements() {
        state = state.copy(
            announcements = announcementRepository.getAnnouncements(),
        )
    }

    fun markAsRead(id: String) {
        announcementRepository.markAsRead(id)
        loadAnnouncements()
    }
}
