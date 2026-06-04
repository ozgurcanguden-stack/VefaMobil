package com.zgrcan.vefamobil.repository

import com.zgrcan.vefamobil.model.Household

interface HouseholdRepository {
    fun getHouseholds(): List<Household>

    fun addHousehold(household: Household)

    fun updateHousehold(household: Household)

    fun deleteHousehold(id: String)

    fun setHouseholdActive(id: String, isActive: Boolean)
}
