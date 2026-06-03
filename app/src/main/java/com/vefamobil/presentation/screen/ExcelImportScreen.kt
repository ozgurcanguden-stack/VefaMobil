package com.vefamobil.presentation.screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vefamobil.model.ExcelImportPreviewItem
import com.vefamobil.model.ExcelImportResult
import com.vefamobil.presentation.ExcelImportUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExcelImportScreen(
    state: ExcelImportUiState,
    onBackClick: () -> Unit,
    onFileSelected: (Uri) -> Unit,
    onImportClick: () -> Unit,
) {
    val context = LocalContext.current
    val documentPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                onFileSelected(uri)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Excel ile Hane Yükleme") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Geri",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            CardSection {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading,
                    onClick = {
                        documentPicker.launch(createExcelPickerIntent())
                    },
                ) {
                    Icon(
                        imageVector = Icons.Outlined.UploadFile,
                        contentDescription = null,
                    )
                    Text(text = "Dosya Seç")
                }

                Text(
                    text = if (state.selectedFileName.isBlank()) {
                        "Seçilen dosya yok"
                    } else {
                        "Seçilen dosya: ${state.selectedFileName}"
                    },
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            state.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            TemplateInfoCard()

            CardSection(title = "Önizleme") {
                StatusLegend()

                if (state.previewItems.isEmpty()) {
                    Text(
                        text = "Dosya seçildiğinde mock önizleme burada görünecek.",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                } else {
                    state.previewItems.forEach { item ->
                        PreviewItemCard(item = item)
                    }
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = state.previewItems.isNotEmpty() && !state.isLoading,
                onClick = {
                    onImportClick()
                    Toast
                        .makeText(context, "Excel içe aktarma tamamlandı", Toast.LENGTH_SHORT)
                        .show()
                },
            ) {
                Text(text = "Yükle")
            }

            state.importResult?.let { result ->
                ImportResultCard(result = result)
            }

            ErrorExamplesCard()
        }
    }
}

private fun createExcelPickerIntent(): Intent {
    return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "*/*"
        putExtra(
            Intent.EXTRA_MIME_TYPES,
            arrayOf(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "application/vnd.ms-excel",
            ),
        )
    }
}

@Composable
private fun CardSection(
    title: String? = null,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (title != null) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            content()
        }
    }
}

@Composable
private fun TemplateInfoCard() {
    CardSection(title = "Örnek Şablon Bilgisi") {
        Text(
            text = "Beklenen Excel kolon sırası:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
        )

        listOf(
            "REF KODU",
            "MAHALLE ADI",
            "AD SOYAD",
            "TC",
            "CEP 1",
            "CEP 2",
            "ADRES",
        ).forEachIndexed { index, column ->
            Text(text = "${index + 1}. $column")
        }
    }
}

@Composable
private fun StatusLegend() {
    Text(
        text = "Durumlar: Hazır / Hatalı / Tekrar kayıt",
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.Medium,
    )
}

@Composable
private fun PreviewItemCard(
    item: ExcelImportPreviewItem,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Satır ${item.rowNumber}",
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = item.status,
                    color = when (item.status) {
                        "Hatalı" -> MaterialTheme.colorScheme.error
                        "Tekrar kayıt" -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.primary
                    },
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Text(text = "${item.refCode} / ${item.neighborhood} / ${item.fullName}")
            Text(text = "TC: ${item.tcNo}   Cep 1: ${item.phone1}")
            Text(text = "Cep 2: ${item.phone2.orEmpty()}")
            Text(text = item.address)
            item.errorMessage?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun ImportResultCard(
    result: ExcelImportResult,
) {
    CardSection(title = "İçe Aktarma Sonucu") {
        Text(text = "Toplam ${result.totalRows} satır okundu")
        Text(text = "${result.successCount} kayıt başarıyla eklendi")
        Text(text = "${result.updatedCount} kayıt güncellendi")
        Text(text = "${result.skippedCount} kayıt atlandı")
        Text(text = "${result.errorCount} hatalı kayıt")

        result.errors.forEach { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun ErrorExamplesCard() {
    CardSection(title = "Hatalı Kayıt Alanı") {
        Text(
            text = "Örnek hata gösterimleri:",
            fontWeight = FontWeight.SemiBold,
        )
        Text(text = "Satır 5: TC eksik")
        Text(text = "Satır 8: Ad Soyad boş")
    }
}
