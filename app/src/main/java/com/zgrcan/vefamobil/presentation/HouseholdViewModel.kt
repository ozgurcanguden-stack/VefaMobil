package com.zgrcan.vefamobil.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zgrcan.vefamobil.data.MockAuditLogRepository
import com.zgrcan.vefamobil.data.MockHouseholdRepository
import com.zgrcan.vefamobil.data.MockTrashRepository
import com.zgrcan.vefamobil.model.AuditLog
import com.zgrcan.vefamobil.model.Household
import com.zgrcan.vefamobil.model.TrashItem
import com.zgrcan.vefamobil.repository.HouseholdRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class HouseholdUiState(
    val households: List<Household> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

class HouseholdViewModel : ViewModel() {
    private val householdRepository: HouseholdRepository = MockHouseholdRepository()
    private val trashRepository = MockTrashRepository()
    private val auditLogRepository = MockAuditLogRepository()
    private var allHouseholds: List<Household> = emptyList()

    var state by mutableStateOf(HouseholdUiState())
        private set

    init {
        loadHouseholds()
    }

    fun loadHouseholds() {
        state = state.copy(isLoading = true, errorMessage = null)
        allHouseholds = householdRepository.getHouseholds()
        applySearchFilter(isLoading = false)
    }

    fun onSearchQueryChange(query: String) {
        state = state.copy(searchQuery = query)
        applySearchFilter()
    }

    fun addHousehold(household: Household) {
        householdRepository.addHousehold(household)
        loadHouseholds()
    }

    fun updateHousehold(household: Household) {
        householdRepository.updateHousehold(household)
        loadHouseholds()
    }

    fun deleteHousehold(id: String) {
        val household = allHouseholds.firstOrNull { it.id == id }
        if (household != null) {
            val now = currentDateTimeText()
            trashRepository.addToTrash(
                TrashItem(
                    id = "trash-household-${household.id}-${System.currentTimeMillis()}",
                    entityType = "HOUSEHOLD",
                    entityId = household.id,
                    title = "${household.refCode} - ${household.fullName}",
                    description = "${household.neighborhood} Mahallesi - ${household.address}",
                    deletedBy = "Vefa Müdürü",
                    deletedAt = now,
                    canRestore = true,
                ),
            )
            auditLogRepository.addLog(
                AuditLog(
                    id = "log-household-delete-${household.id}-${System.currentTimeMillis()}",
                    actionType = "DELETE_HOUSEHOLD",
                    entityType = "HOUSEHOLD",
                    entityId = household.id,
                    description = "${household.refCode} - ${household.fullName} silindi.",
                    performedBy = "Vefa Müdürü",
                    createdAt = now,
                ),
            )
        }
        householdRepository.deleteHousehold(id)
        loadHouseholds()
    }

    fun toggleActive(id: String) {
        val household = allHouseholds.firstOrNull { it.id == id } ?: return
        householdRepository.setHouseholdActive(id = id, isActive = !household.isActive)
        loadHouseholds()
    }

    fun getHousehold(id: String): Household? {
        val visibleHouseholds = state.households
        return allHouseholds.firstOrNull { it.id == id }
            ?: visibleHouseholds.firstOrNull { it.id == id }
    }

    private fun applySearchFilter(isLoading: Boolean = state.isLoading) {
        val query = state.searchQuery.trim()
        val filteredHouseholds = if (query.isEmpty()) {
            allHouseholds
        } else {
            allHouseholds.filter { household ->
                household.refCode.contains(query, ignoreCase = true) ||
                    household.neighborhood.contains(query, ignoreCase = true) ||
                    household.fullName.contains(query, ignoreCase = true)
            }
        }

        state = state.copy(
            households = filteredHouseholds,
            isLoading = isLoading,
            errorMessage = null,
        )
    }

    private fun currentDateTimeText(): String {
        return SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("tr", "TR")).format(Date())
    }
}
