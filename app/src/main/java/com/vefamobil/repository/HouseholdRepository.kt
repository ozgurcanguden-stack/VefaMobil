package com.vefamobil.repository

import com.vefamobil.model.Household

interface HouseholdRepository {
    fun getHouseholds(): List<Household>

    fun addHousehold(household: Household)

    fun updateHousehold(household: Household)

    fun deleteHousehold(id: String)

    fun setHouseholdActive(id: String, isActive: Boolean)
}
