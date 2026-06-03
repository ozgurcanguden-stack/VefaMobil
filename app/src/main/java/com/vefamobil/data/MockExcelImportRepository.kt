package com.vefamobil.data

import android.net.Uri
import com.vefamobil.model.ExcelImportPreviewItem
import com.vefamobil.model.ExcelImportResult
import com.vefamobil.repository.ExcelImportRepository

class MockExcelImportRepository : ExcelImportRepository {
    override fun parseExcelFile(uri: Uri): List<ExcelImportPreviewItem> {
        return listOf(
            ExcelImportPreviewItem(
                rowNumber = 2,
                refCode = "REF001",
                neighborhood = "Hürriyet",
                fullName = "Ahmet Yılmaz",
                tcNo = "11111111111",
                phone1 = "05550000001",
                phone2 = null,
                address = "Hürriyet Mahallesi",
                status = "Hazır",
                errorMessage = null,
            ),
            ExcelImportPreviewItem(
                rowNumber = 3,
                refCode = "REF002",
                neighborhood = "Cumhuriyet",
                fullName = "Ayşe Kaya",
                tcNo = "22222222222",
                phone1 = "05550000002",
                phone2 = "05550000012",
                address = "Cumhuriyet Mahallesi",
                status = "Hazır",
                errorMessage = null,
            ),
            ExcelImportPreviewItem(
                rowNumber = 4,
                refCode = "REF003",
                neighborhood = "Acarlar",
                fullName = "Mehmet Demir",
                tcNo = "33333333333",
                phone1 = "05550000003",
                phone2 = null,
                address = "Acarlar Mahallesi",
                status = "Hazır",
                errorMessage = null,
            ),
        )
    }

    override fun importHouseholds(items: List<ExcelImportPreviewItem>): ExcelImportResult {
        return ExcelImportResult(
            totalRows = items.size,
            successCount = items.count { it.status == "Hazır" },
            updatedCount = 0,
            skippedCount = 0,
            errorCount = items.count { it.status == "Hatalı" },
            errors = emptyList(),
        )
    }
}
