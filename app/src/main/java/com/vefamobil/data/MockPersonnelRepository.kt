package com.vefamobil.data

import com.vefamobil.model.Personnel
import com.vefamobil.repository.PersonnelRepository

class MockPersonnelRepository : PersonnelRepository {
    private val personnelList = mutableListOf(
        Personnel(
            id = "1",
            fullName = "Ali Kaya",
            username = "ali.kaya",
            temporaryPassword = "123456",
            isActive = true,
            mustChangePassword = true,
            createdAt = System.currentTimeMillis(),
        ),
        Personnel(
            id = "2",
            fullName = "Veli Demir",
            username = "veli.demir",
            temporaryPassword = "123456",
            isActive = true,
            mustChangePassword = true,
            createdAt = System.currentTimeMillis(),
        ),
        Personnel(
            id = "3",
            fullName = "Ayşe Yılmaz",
            username = "ayse.yilmaz",
            temporaryPassword = "123456",
            isActive = true,
            mustChangePassword = true,
            createdAt = System.currentTimeMillis(),
        ),
    )

    override fun getPersonnel(): List<Personnel> = personnelList.toList()

    override fun addPersonnel(personnel: Personnel) {
        personnelList.add(personnel)
    }

    override fun updatePersonnel(personnel: Personnel) {
        val index = personnelList.indexOfFirst { it.id == personnel.id }
        if (index >= 0) {
            personnelList[index] = personnel
        }
    }

    override fun deletePersonnel(id: String) {
        personnelList.removeAll { it.id == id }
    }

    override fun setActive(id: String, isActive: Boolean) {
        val index = personnelList.indexOfFirst { it.id == id }
        if (index >= 0) {
            personnelList[index] = personnelList[index].copy(isActive = isActive)
        }
    }
}
