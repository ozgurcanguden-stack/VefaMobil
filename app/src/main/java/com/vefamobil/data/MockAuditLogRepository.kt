package com.vefamobil.data

import com.vefamobil.model.AuditLog

class MockAuditLogRepository {
    fun getLogs(): List<AuditLog> = logs.sortedByDescending { it.createdAt }

    fun addLog(log: AuditLog) {
        logs.add(log)
    }

    companion object {
        private val logs = mutableListOf<AuditLog>()
    }
}
