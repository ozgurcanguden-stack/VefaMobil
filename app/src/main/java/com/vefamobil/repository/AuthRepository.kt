package com.vefamobil.repository

import com.vefamobil.model.MockUser
import com.vefamobil.model.UserRole

interface AuthRepository {
    fun login(role: UserRole, username: String, password: String): MockUser
}
