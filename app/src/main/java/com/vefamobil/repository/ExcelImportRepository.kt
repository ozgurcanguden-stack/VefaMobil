package com.vefamobil.repository

import android.net.Uri
import com.vefamobil.model.ExcelImportPreviewItem
import com.vefamobil.model.ExcelImportResult

interface ExcelImportRepository {
    fun parseExcelFile(uri: Uri): List<ExcelImportPreviewItem>

    fun importHouseholds(items: List<ExcelImportPreviewItem>): ExcelImportResult
}
