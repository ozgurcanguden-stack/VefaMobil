package com.vefamobil.data

import com.vefamobil.model.Household
import com.vefamobil.repository.HouseholdRepository

class MockHouseholdRepository : HouseholdRepository {
    override fun getHouseholds(): List<Household> = households.toList()

    override fun addHousehold(household: Household) {
        households.add(household)
    }

    override fun updateHousehold(household: Household) {
        val index = households.indexOfFirst { it.id == household.id }
        if (index >= 0) {
            households[index] = household
        }
    }

    override fun deleteHousehold(id: String) {
        households.removeAll { it.id == id }
    }

    override fun setHouseholdActive(id: String, isActive: Boolean) {
        val index = households.indexOfFirst { it.id == id }
        if (index >= 0) {
            households[index] = households[index].copy(isActive = isActive)
        }
    }

    companion object {
        private val households = mutableListOf(
            Household(
                id = "1",
                refCode = "REF001",
                neighborhood = "Hürriyet",
                fullName = "Ahmet Yılmaz",
                tcNo = "11111111111",
                phone1 = "05550000001",
                phone2 = null,
                address = "Hürriyet Mahallesi",
                isActive = true,
                isNewHousehold = true,
                isUrgent = false,
                firstVisitCompleted = false,
            ),
            Household(
                id = "2",
                refCode = "REF002",
                neighborhood = "Cumhuriyet",
                fullName = "Ayşe Kaya",
                tcNo = "22222222222",
                phone1 = "05550000002",
                phone2 = "05550000012",
                address = "Cumhuriyet Mahallesi",
                isActive = true,
                isNewHousehold = false,
                isUrgent = true,
                firstVisitCompleted = true,
            ),
            Household(
                id = "3",
                refCode = "REF003",
                neighborhood = "Acarlar",
                fullName = "Mehmet Demir",
                tcNo = "33333333333",
                phone1 = "05550000003",
                phone2 = null,
                address = "Acarlar Mahallesi",
                isActive = true,
                isNewHousehold = false,
                isUrgent = false,
                firstVisitCompleted = true,
            ),
        )
    }
}
