package com.zgrcan.vefamobil.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zgrcan.vefamobil.data.MockAuditLogRepository
import com.zgrcan.vefamobil.data.MockPersonnelRepository
import com.zgrcan.vefamobil.data.MockTrashRepository
import com.zgrcan.vefamobil.model.AuditLog
import com.zgrcan.vefamobil.model.Personnel
import com.zgrcan.vefamobil.model.TrashItem
import com.zgrcan.vefamobil.repository.PersonnelRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PersonnelUiState(
    val personnelList: List<Personnel> = emptyList(),
    val searchQuery: String = "",
)

class PersonnelViewModel : ViewModel() {
    private val personnelRepository: PersonnelRepository = MockPersonnelRepository()
    private val trashRepository = MockTrashRepository()
    private val auditLogRepository = MockAuditLogRepository()
    private var allPersonnel: List<Personnel> = emptyList()

    var state by mutableStateOf(PersonnelUiState())
        private set

    init {
        loadPersonnel()
    }

    fun loadPersonnel() {
        allPersonnel = personnelRepository.getPersonnel()
        applySearchFilter()
    }

    fun onSearchQueryChange(query: String) {
        state = state.copy(searchQuery = query)
        applySearchFilter()
    }

    fun addPersonnel(personnel: Personnel) {
        personnelRepository.addPersonnel(personnel)
        loadPersonnel()
    }

    fun updatePersonnel(personnel: Personnel) {
        personnelRepository.updatePersonnel(personnel)
        loadPersonnel()
    }

    fun deletePersonnel(id: String) {
        val personnel = allPersonnel.firstOrNull { it.id == id }
        if (personnel != null) {
            val now = currentDateTimeText()
            trashRepository.addToTrash(
                TrashItem(
                    id = "trash-personnel-${personnel.id}-${System.currentTimeMillis()}",
                    entityType = "PERSONNEL",
                    entityId = personnel.id,
                    title = personnel.fullName,
                    description = "Kullanıcı adı: ${personnel.username}",
                    deletedBy = "Vefa Müdürü",
                    deletedAt = now,
                    canRestore = true,
                ),
            )
            auditLogRepository.addLog(
                AuditLog(
                    id = "log-personnel-delete-${personnel.id}-${System.currentTimeMillis()}",
                    actionType = "DELETE_PERSONNEL",
                    entityType = "PERSONNEL",
                    entityId = personnel.id,
                    description = "${personnel.fullName} silindi.",
                    performedBy = "Vefa Müdürü",
                    createdAt = now,
                ),
            )
        }
        personnelRepository.deletePersonnel(id)
        loadPersonnel()
    }

    fun toggleActive(id: String) {
        val personnel = allPersonnel.firstOrNull { it.id == id } ?: return
        personnelRepository.setActive(id = id, isActive = !personnel.isActive)
        loadPersonnel()
    }

    fun getPersonnel(id: String): Personnel? {
        val visiblePersonnel = state.personnelList
        return allPersonnel.firstOrNull { it.id == id }
            ?: visiblePersonnel.firstOrNull { it.id == id }
    }

    private fun applySearchFilter() {
        val query = state.searchQuery.trim()
        val filteredPersonnel = if (query.isEmpty()) {
            allPersonnel
        } else {
            allPersonnel.filter { personnel ->
                personnel.fullName.contains(query, ignoreCase = true) ||
                    personnel.username.contains(query, ignoreCase = true)
            }
        }

        state = state.copy(personnelList = filteredPersonnel)
    }

    private fun currentDateTimeText(): String {
        return SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("tr", "TR")).format(Date())
    }
}
