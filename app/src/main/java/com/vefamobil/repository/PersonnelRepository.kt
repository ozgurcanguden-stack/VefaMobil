package com.vefamobil.repository

import com.vefamobil.model.Personnel

interface PersonnelRepository {
    fun getPersonnel(): List<Personnel>

    fun addPersonnel(personnel: Personnel)

    fun updatePersonnel(personnel: Personnel)

    fun deletePersonnel(id: String)

    fun setActive(id: String, isActive: Boolean)
}
