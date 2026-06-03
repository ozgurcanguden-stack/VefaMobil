package com.vefamobil.data

import com.vefamobil.data.mock.MockDataStore
import com.vefamobil.model.AuditLog

class MockAuditLogRepository {
    fun getLogs(): List<AuditLog> = MockDataStore.auditLogs.sortedByDescending { it.createdAt }

    fun addLog(log: AuditLog) {
        MockDataStore.auditLogs.add(log)
    }
}
