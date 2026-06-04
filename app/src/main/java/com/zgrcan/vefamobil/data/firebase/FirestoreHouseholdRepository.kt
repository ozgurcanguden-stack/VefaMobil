package com.zgrcan.vefamobil.data.firebase

import com.zgrcan.vefamobil.model.Household
import com.zgrcan.vefamobil.repository.HouseholdRepository

class FirestoreHouseholdRepository : HouseholdRepository {
    override fun getHouseholds(): List<Household> {
        // TODO: Firestore households collection will be read here.
        return emptyList()
    }

    override fun addHousehold(household: Household) {
        // TODO: Firestore add household implementation.
    }

    override fun updateHousehold(household: Household) {
        // TODO: Firestore update household implementation.
    }

    override fun deleteHousehold(id: String) {
        // TODO: Firestore soft delete or delete household implementation.
    }

    override fun setHouseholdActive(id: String, isActive: Boolean) {
        // TODO: Firestore active state update implementation.
    }
}
