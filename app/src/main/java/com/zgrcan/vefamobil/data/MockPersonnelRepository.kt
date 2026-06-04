package com.zgrcan.vefamobil.data

import com.zgrcan.vefamobil.data.mock.MockDataStore
import com.zgrcan.vefamobil.model.Personnel
import com.zgrcan.vefamobil.repository.PersonnelRepository

class MockPersonnelRepository : PersonnelRepository {
    override fun getPersonnel(): List<Personnel> = MockDataStore.personnel.toList()

    override fun addPersonnel(personnel: Personnel) {
        MockDataStore.personnel.add(personnel)
    }

    override fun updatePersonnel(personnel: Personnel) {
        val index = MockDataStore.personnel.indexOfFirst { it.id == personnel.id }
        if (index >= 0) {
            MockDataStore.personnel[index] = personnel
        }
    }

    override fun deletePersonnel(id: String) {
        MockDataStore.personnel.removeAll { it.id == id }
    }

    override fun setActive(id: String, isActive: Boolean) {
        val index = MockDataStore.personnel.indexOfFirst { it.id == id }
        if (index >= 0) {
            MockDataStore.personnel[index] = MockDataStore.personnel[index].copy(isActive = isActive)
        }
    }
}
