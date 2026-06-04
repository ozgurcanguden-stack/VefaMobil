package com.zgrcan.vefamobil.repository

import com.zgrcan.vefamobil.model.MockUser
import com.zgrcan.vefamobil.model.UserRole

interface AuthRepository {
    fun login(role: UserRole, username: String, password: String): MockUser
}
