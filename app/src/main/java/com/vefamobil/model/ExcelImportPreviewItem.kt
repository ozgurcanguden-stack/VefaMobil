package com.vefamobil.model

data class ExcelImportPreviewItem(
    val rowNumber: Int = 0,
    val refCode: String = "",
    val neighborhood: String = "",
    val fullName: String = "",
    val tcNo: String = "",
    val phone1: String = "",
    val phone2: String? = null,
    val address: String = "",
    val status: String = "",
    val errorMessage: String? = null,
)
