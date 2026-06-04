package com.zgrcan.vefamobil.data

import com.zgrcan.vefamobil.data.mock.MockDataStore
import com.zgrcan.vefamobil.model.AuditLog

class MockAuditLogRepository {
    fun getLogs(): List<AuditLog> = MockDataStore.auditLogs.sortedByDescending { it.createdAt }

    fun addLog(log: AuditLog) {
        MockDataStore.auditLogs.add(log)
    }
}
