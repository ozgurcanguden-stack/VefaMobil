package com.vefamobil.presentation

import android.content.Context
import android.app.Application
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vefamobil.data.MockAuditLogRepository
import com.vefamobil.data.MockExcelImportRepository
import com.vefamobil.model.AuditLog
import com.vefamobil.model.ExcelImportPreviewItem
import com.vefamobil.model.ExcelImportResult
import com.vefamobil.repository.ExcelImportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ExcelImportUiState(
    val selectedFileName: String = "",
    val previewItems: List<ExcelImportPreviewItem> = emptyList(),
    val importResult: ExcelImportResult? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

class ExcelImportViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val excelImportRepository: ExcelImportRepository = MockExcelImportRepository(application.applicationContext)
    private val auditLogRepository = MockAuditLogRepository()

    var state by mutableStateOf(ExcelImportUiState())
        private set

    fun onFileSelected(context: Context, uri: Uri) {
        state = state.copy(
            selectedFileName = context.getDisplayName(uri),
            previewItems = emptyList(),
            importResult = null,
            isLoading = true,
            errorMessage = null,
        )

        viewModelScope.launch {
            val previewItems = withContext(Dispatchers.IO) {
                excelImportRepository.parseExcelFile(uri)
            }
            state = state.copy(
                previewItems = previewItems,
                isLoading = false,
                errorMessage = null,
            )
        }
    }

    fun importPreviewItems() {
        if (state.previewItems.isEmpty()) return

        state = state.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                excelImportRepository.importHouseholds(state.previewItems)
            }
            auditLogRepository.addLog(
                AuditLog(
                    id = "log-excel-import-${System.currentTimeMillis()}",
                    actionType = "IMPORT_EXCEL_HOUSEHOLDS",
                    entityType = "HOUSEHOLD",
                    entityId = "EXCEL_IMPORT",
                    description = "Excel ile ${result.successCount} hane içe aktarıldı.",
                    performedBy = "Vefa Müdürü",
                    createdAt = currentDateTimeText(),
                ),
            )
            state = state.copy(
                importResult = result,
                isLoading = false,
                errorMessage = null,
            )
        }
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
