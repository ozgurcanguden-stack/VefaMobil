package com.vefamobil.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.vefamobil.data.MockHouseholdRepository
import com.vefamobil.model.Household
import com.vefamobil.repository.HouseholdRepository

data class HouseholdUiState(
    val households: List<Household> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

class HouseholdViewModel : ViewModel() {
    private val householdRepository: HouseholdRepository = MockHouseholdRepository()
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
        householdRepository.deleteHousehold(id)
        loadHouseholds()
    }

    fun toggleActive(id: String) {
        val household = allHouseholds.firstOrNull { it.id == id } ?: return
        householdRepository.setHouseholdActive(id = id, isActive = !household.isActive)
        loadHouseholds()
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
}
