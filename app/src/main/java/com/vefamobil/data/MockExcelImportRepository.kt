package com.vefamobil.data

import android.content.Context
import android.net.Uri
import com.vefamobil.model.Household
import com.vefamobil.model.ExcelImportPreviewItem
import com.vefamobil.model.ExcelImportResult
import com.vefamobil.repository.ExcelImportRepository
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.util.Locale

class MockExcelImportRepository(
    private val context: Context,
) : ExcelImportRepository {
    private val householdRepository = MockHouseholdRepository()

    override fun parseExcelFile(uri: Uri): List<ExcelImportPreviewItem> {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                WorkbookFactory.create(inputStream).use { workbook ->
                    val sheet = workbook.getSheetAt(0)
                    val formatter = DataFormatter(Locale("tr", "TR"))
                    val existingRefCodes = householdRepository.getHouseholds()
                        .map { household -> household.refCode.trim() }
                        .toSet()
                    val seenRefCodes = mutableSetOf<String>()

                    (1..sheet.lastRowNum).mapNotNull { rowIndex ->
                        val row = sheet.getRow(rowIndex) ?: return@mapNotNull null
                        val values = (0..6).map { columnIndex ->
                            formatter.formatCellValue(row.getCell(columnIndex)).trim()
                        }

                        if (values.all { it.isBlank() }) {
                            return@mapNotNull null
                        }

                        val refCode = values[0]
                        val neighborhood = values[1]
                        val fullName = values[2]
                        val tcNo = values[3]
                        val phone1 = values[4]
                        val phone2 = values[5].ifBlank { null }
                        val address = values[6]
                        val errors = buildValidationErrors(
                            refCode = refCode,
                            neighborhood = neighborhood,
                            fullName = fullName,
                            tcNo = tcNo,
                            phone1 = phone1,
                            address = address,
                        )
                        val isDuplicate = refCode.isNotBlank() &&
                            (existingRefCodes.contains(refCode) || !seenRefCodes.add(refCode))
                        val status = when {
                            errors.isNotEmpty() -> "Hatalı"
                            isDuplicate -> "Tekrar kayıt"
                            else -> "Hazır"
                        }

                        ExcelImportPreviewItem(
                            rowNumber = row.rowNum + 1,
                            refCode = refCode,
                            neighborhood = neighborhood,
                            fullName = fullName,
                            tcNo = tcNo,
                            phone1 = phone1,
                            phone2 = phone2,
                            address = address,
                            status = status,
                            errorMessage = errors.joinToString(", ").ifBlank { null },
                        )
                    }
                }
            } ?: listOf(
                createFileErrorItem("Dosya okunamadı."),
            )
        } catch (error: Exception) {
            listOf(createFileErrorItem(error.message ?: "Excel dosyası okunamadı."))
        }
    }

    override fun importHouseholds(items: List<ExcelImportPreviewItem>): ExcelImportResult {
        var successCount = 0
        var skippedCount = 0
        val errors = mutableListOf<String>()

        items.forEach { item ->
            when (item.status) {
                "Hazır" -> {
                    if (householdRepository.getHouseholds().any { it.refCode == item.refCode }) {
                        skippedCount++
                    } else {
                        householdRepository.addHousehold(item.toHousehold())
                        successCount++
                    }
                }

                "Tekrar kayıt" -> skippedCount++
                "Hatalı" -> errors.add("Satır ${item.rowNumber}: ${item.errorMessage.orEmpty()}")
                else -> skippedCount++
            }
        }

        return ExcelImportResult(
            totalRows = items.size,
            successCount = successCount,
            updatedCount = 0,
            skippedCount = skippedCount,
            errorCount = errors.size,
            errors = errors,
        )
    }

    private fun buildValidationErrors(
        refCode: String,
        neighborhood: String,
        fullName: String,
        tcNo: String,
        phone1: String,
        address: String,
    ): List<String> {
        return buildList {
            if (refCode.isBlank()) add("REF KODU eksik")
            if (neighborhood.isBlank()) add("MAHALLE ADI eksik")
            if (fullName.isBlank()) add("AD SOYAD boş")
            if (tcNo.isBlank()) add("TC eksik")
            if (tcNo.isNotBlank() && tcNo.length != 11) add("TC 11 karakter olmalı")
            if (phone1.isBlank()) add("CEP 1 eksik")
            if (address.isBlank()) add("ADRES eksik")
        }
    }

    private fun ExcelImportPreviewItem.toHousehold(): Household {
        return Household(
            id = "excel-${System.currentTimeMillis()}-$rowNumber",
            refCode = refCode,
            neighborhood = neighborhood,
            fullName = fullName,
            tcNo = tcNo,
            phone1 = phone1,
            phone2 = phone2,
            address = address,
            isActive = true,
            isNewHousehold = true,
            isUrgent = false,
            firstVisitCompleted = false,
        )
    }

    private fun createFileErrorItem(message: String): ExcelImportPreviewItem {
        return ExcelImportPreviewItem(
            rowNumber = 0,
            refCode = "",
            neighborhood = "",
            fullName = "",
            tcNo = "",
            phone1 = "",
            phone2 = null,
            address = "",
            status = "Hatalı",
            errorMessage = message,
        )
    }
}
