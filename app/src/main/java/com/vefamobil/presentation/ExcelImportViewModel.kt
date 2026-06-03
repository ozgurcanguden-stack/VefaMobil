package com.vefamobil.presentation

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.vefamobil.data.MockAuditLogRepository
import com.vefamobil.data.MockExcelImportRepository
import com.vefamobil.model.AuditLog
import com.vefamobil.model.ExcelImportPreviewItem
import com.vefamobil.model.ExcelImportResult
import com.vefamobil.repository.ExcelImportRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ExcelImportUiState(
    val selectedFileName: String = "",
    val previewItems: List<ExcelImportPreviewItem> = emptyList(),
    val importResult: ExcelImportResult? = null,
)

class ExcelImportViewModel : ViewModel() {
    private val excelImportRepository: ExcelImportRepository = MockExcelImportRepository()
    private val auditLogRepository = MockAuditLogRepository()

    var state by mutableStateOf(ExcelImportUiState())
        private set

    fun onFileSelected(context: Context, uri: Uri) {
        state = state.copy(
            selectedFileName = context.getDisplayName(uri),
            previewItems = excelImportRepository.parseExcelFile(uri),
            importResult = null,
        )
    }

    fun importPreviewItems() {
        val result = excelImportRepository.importHouseholds(state.previewItems)
        auditLogRepository.addLog(
            AuditLog(
                id = "log-excel-import-${System.currentTimeMillis()}",
                actionType = "CREATE_HOUSEHOLD",
                entityType = "HOUSEHOLD",
                entityId = "EXCEL_IMPORT",
                description = "Excel ile ${result.successCount} hane içe aktarıldı.",
                performedBy = "Vefa Müdürü",
                createdAt = currentDateTimeText(),
            ),
        )
        state = state.copy(importResult = result)
    }

    private fun Context.getDisplayName(uri: Uri): String {
        return contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex >= 0 && cursor.moveToFirst()) {
                cursor.getString(nameIndex)
            } else {
                uri.lastPathSegment.orEmpty()
            }
        }.orEmpty().ifBlank { uri.lastPathSegment.orEmpty() }
    }

    private fun currentDateTimeText(): String {
        return SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("tr", "TR")).format(Date())
    }
}
