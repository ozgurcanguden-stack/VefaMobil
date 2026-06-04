package com.vefamobil.data.firebase

import com.vefamobil.model.Personnel
import com.vefamobil.repository.PersonnelRepository

class FirestorePersonnelRepository : PersonnelRepository {
    override fun getPersonnel(): List<Personnel> {
        // TODO: Firestore users/personnel collection will be read here.
        return emptyList()
    }

    override fun addPersonnel(personnel: Personnel) {
        // TODO: Firestore add personnel implementation.
    }

    override fun updatePersonnel(personnel: Personnel) {
        // TODO: Firestore update personnel implementation.
    }

    override fun deletePersonnel(id: String) {
        // TODO: Firestore soft delete or delete personnel implementation.
    }

    override fun setActive(id: String, isActive: Boolean) {
        // TODO: Firestore active state update implementation.
    }
}
