package com.zgrcan.vefamobil.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zgrcan.vefamobil.data.firebase.FirestoreHouseholdRepository
import com.zgrcan.vefamobil.model.Household
import kotlinx.coroutines.launch

data class HouseholdUiState(
    val households: List<Household> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
)

class HouseholdViewModel : ViewModel() {
    private val householdRepository = FirestoreHouseholdRepository()
    private var allHouseholds: List<Household> = emptyList()
    private var organizationId: String = ""
    private var currentUserId: String = ""

    var state by mutableStateOf(HouseholdUiState())
        private set

    fun setOrganizationContext(
        organizationId: String,
        currentUserId: String,
    ) {
        val trimmedOrganizationId = organizationId.trim()
        val trimmedUserId = currentUserId.trim()

        if (trimmedOrganizationId.isBlank()) {
            this.organizationId = ""
            this.currentUserId = trimmedUserId
            showError("Kurum bilgisi bulunamadı.")
            return
        }

        val shouldReload = this.organizationId != trimmedOrganizationId
        this.organizationId = trimmedOrganizationId
        this.currentUserId = trimmedUserId

        if (shouldReload || allHouseholds.isEmpty()) {
            loadHouseholds()
        }
    }

    fun loadHouseholds() {
        val currentOrganizationId = organizationId
        if (currentOrganizationId.isBlank()) {
            showError("Kurum bilgisi bulunamadı.")
            return
        }

        state = state.copy(isLoading = true, errorMessage = null, successMessage = null)
        viewModelScope.launch {
            runCatching {
                householdRepository.getHouseholds(currentOrganizationId)
            }.onSuccess { households ->
                allHouseholds = households
                applySearchFilter(isLoading = false)
            }.onFailure {
                showOperationError()
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        state = state.copy(searchQuery = query)
        applySearchFilter()
    }

    fun addHousehold(household: Household) {
        val currentOrganizationId = organizationId
        if (currentOrganizationId.isBlank()) {
            showError("Kurum bilgisi bulunamadı.")
            return
        }

        state = state.copy(isLoading = true, errorMessage = null, successMessage = null)
        viewModelScope.launch {
            runCatching {
                householdRepository.addHousehold(
                    organizationId = currentOrganizationId,
                    household = household.copy(
                        organizationId = currentOrganizationId,
                        isActive = true,
                        isNewHousehold = true,
                        firstVisitCompleted = false,
                        isDeleted = false,
                        createdBy = currentUserId,
                    ),
                )
                householdRepository.getHouseholds(currentOrganizationId)
            }.onSuccess { households ->
                allHouseholds = households
                applySearchFilter(
                    isLoading = false,
                    successMessage = "Hane eklendi.",
                )
            }.onFailure {
                showOperationError()
            }
        }
    }

    fun updateHousehold(household: Household) {
        val currentOrganizationId = organizationId
        if (currentOrganizationId.isBlank()) {
            showError("Kurum bilgisi bulunamadı.")
            return
        }

        state = state.copy(isLoading = true, errorMessage = null, successMessage = null)
        viewModelScope.launch {
            runCatching {
                householdRepository.updateHousehold(
                    organizationId = currentOrganizationId,
                    household = household.copy(
                        organizationId = currentOrganizationId,
                        isDeleted = false,
                    ),
                )
                householdRepository.getHouseholds(currentOrganizationId)
            }.onSuccess { households ->
                allHouseholds = households
                applySearchFilter(
                    isLoading = false,
                    successMessage = "Hane güncellendi.",
                )
            }.onFailure {
                showOperationError()
            }
        }
    }

    fun deleteHousehold(id: String) {
        val currentOrganizationId = organizationId
        if (currentOrganizationId.isBlank()) {
            showError("Kurum bilgisi bulunamadı.")
            return
        }

        state = state.copy(isLoading = true, errorMessage = null, successMessage = null)
        viewModelScope.launch {
            runCatching {
                householdRepository.deleteHousehold(
                    organizationId = currentOrganizationId,
                    householdId = id,
                )
                householdRepository.getHouseholds(currentOrganizationId)
            }.onSuccess { households ->
                allHouseholds = households
                applySearchFilter(
                    isLoading = false,
                    successMessage = "Hane silindi.",
                )
            }.onFailure {
                showOperationError()
            }
        }
    }

    fun toggleActive(id: String) {
        val currentOrganizationId = organizationId
        if (currentOrganizationId.isBlank()) {
            showError("Kurum bilgisi bulunamadı.")
            return
        }

        val household = allHouseholds.firstOrNull { it.id == id } ?: return
        val newActiveState = !household.isActive

        state = state.copy(isLoading = true, errorMessage = null, successMessage = null)
        viewModelScope.launch {
            runCatching {
                householdRepository.setHouseholdActive(
                    organizationId = currentOrganizationId,
                    householdId = id,
                    isActive = newActiveState,
                )
                householdRepository.getHouseholds(currentOrganizationId)
            }.onSuccess { households ->
                allHouseholds = households
                applySearchFilter(
                    isLoading = false,
                    successMessage = if (newActiveState) {
                        "Hane aktife alındı."
                    } else {
                        "Hane pasife alındı."
                    },
                )
            }.onFailure {
                showOperationError()
            }
        }
    }

    fun getHousehold(id: String): Household? {
        val visibleHouseholds = state.households
        return allHouseholds.firstOrNull { it.id == id }
            ?: visibleHouseholds.firstOrNull { it.id == id }
    }

    fun clearMessages() {
        state = state.copy(errorMessage = null, successMessage = null)
    }

    private fun applySearchFilter(
        isLoading: Boolean = state.isLoading,
        successMessage: String? = state.successMessage,
    ) {
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
            successMessage = successMessage,
        )
    }

    private fun showOperationError() {
        showError("Hane işlemi gerçekleştirilemedi.")
    }

    private fun showError(message: String) {
        state = state.copy(
            isLoading = false,
            errorMessage = message,
            successMessage = null,
        )
    }
}
