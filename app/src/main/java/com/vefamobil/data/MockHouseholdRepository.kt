package com.vefamobil.data

import com.vefamobil.data.mock.MockDataStore
import com.vefamobil.model.Household
import com.vefamobil.repository.HouseholdRepository

class MockHouseholdRepository : HouseholdRepository {
    override fun getHouseholds(): List<Household> = MockDataStore.households.toList()

    override fun addHousehold(household: Household) {
        MockDataStore.households.add(household)
    }

    override fun updateHousehold(household: Household) {
        val index = MockDataStore.households.indexOfFirst { it.id == household.id }
        if (index >= 0) {
            MockDataStore.households[index] = household
        }
    }

    override fun deleteHousehold(id: String) {
        MockDataStore.households.removeAll { it.id == id }
    }

    override fun setHouseholdActive(id: String, isActive: Boolean) {
        val index = MockDataStore.households.indexOfFirst { it.id == id }
        if (index >= 0) {
            MockDataStore.households[index] = MockDataStore.households[index].copy(isActive = isActive)
        }
    }
}
