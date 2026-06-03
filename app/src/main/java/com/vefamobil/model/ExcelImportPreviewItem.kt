package com.vefamobil.model

data class ExcelImportPreviewItem(
    val rowNumber: Int,
    val refCode: String,
    val neighborhood: String,
    val fullName: String,
    val tcNo: String,
    val phone1: String,
    val phone2: String?,
    val address: String,
    val status: String,
    val errorMessage: String?,
)
