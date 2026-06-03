package com.vefamobil.model

data class ExcelImportResult(
    val totalRows: Int,
    val successCount: Int,
    val updatedCount: Int,
    val skippedCount: Int,
    val errorCount: Int,
    val errors: List<String>,
)
