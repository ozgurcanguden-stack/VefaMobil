package com.vefamobil.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.vefamobil.data.MockPersonnelRepository
import com.vefamobil.model.Personnel
import com.vefamobil.repository.PersonnelRepository

data class PersonnelUiState(
    val personnelList: List<Personnel> = emptyList(),
    val searchQuery: String = "",
)

class PersonnelViewModel : ViewModel() {
    private val personnelRepository: PersonnelRepository = MockPersonnelRepository()
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
}
