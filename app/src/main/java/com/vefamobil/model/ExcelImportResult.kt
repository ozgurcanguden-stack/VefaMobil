package com.vefamobil.model

data class ExcelImportResult(
    val totalRows: Int = 0,
    val successCount: Int = 0,
    val updatedCount: Int = 0,
    val skippedCount: Int = 0,
    val errorCount: Int = 0,
    val errors: List<String> = emptyList(),
)
