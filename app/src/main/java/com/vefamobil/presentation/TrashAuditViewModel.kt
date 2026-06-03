package com.vefamobil.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.vefamobil.data.MockAuditLogRepository
import com.vefamobil.data.MockTrashRepository
import com.vefamobil.model.AuditLog
import com.vefamobil.model.TrashItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TrashAuditUiState(
    val trashItems: List<TrashItem> = emptyList(),
    val auditLogs: List<AuditLog> = emptyList(),
)

class TrashAuditViewModel : ViewModel() {
    private val trashRepository = MockTrashRepository()
    private val auditLogRepository = MockAuditLogRepository()

    var state by mutableStateOf(TrashAuditUiState())
        private set

    init {
        loadData()
    }

    fun loadData() {
        state = TrashAuditUiState(
            trashItems = trashRepository.getTrashItems(),
            auditLogs = auditLogRepository.getLogs(),
        )
    }

    fun restoreItem(id: String) {
        val item = state.trashItems.firstOrNull { it.id == id }
        trashRepository.restoreItem(id)
        if (item != null) {
            auditLogRepository.addLog(
                AuditLog(
                    id = "log-${System.currentTimeMillis()}",
                    actionType = "RESTORE_${item.entityType}",
                    entityType = item.entityType,
                    entityId = item.entityId,
                    description = "${item.title} geri yüklendi.",
                    performedBy = "Vefa Müdürü",
                    createdAt = currentDateTimeText(),
                ),
            )
        }
        loadData()
    }

    fun permanentlyDelete(id: String) {
        trashRepository.permanentlyDelete(id)
        loadData()
    }

    private fun currentDateTimeText(): String {
        return SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("tr", "TR")).format(Date())
    }
}
