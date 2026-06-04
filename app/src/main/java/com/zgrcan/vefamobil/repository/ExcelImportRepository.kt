package com.zgrcan.vefamobil.repository

import android.net.Uri
import com.zgrcan.vefamobil.model.ExcelImportPreviewItem
import com.zgrcan.vefamobil.model.ExcelImportResult

interface ExcelImportRepository {
    fun parseExcelFile(uri: Uri): List<ExcelImportPreviewItem>

    fun importHouseholds(items: List<ExcelImportPreviewItem>): ExcelImportResult
}
