package com.vefamobil.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vefamobil.model.Household

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseholdDetailScreen(
    household: Household?,
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    onToggleActiveClick: (String) -> Unit,
) {
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var showActiveDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Hane Detayı") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
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
        if (household == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Hane bulunamadı.",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                )

                Button(onClick = onBackClick) {
                    Text(text = "Geri Dön")
                }
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
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
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    DetailRow(label = "Ref Kodu", value = household.refCode)
                    DetailRow(label = "Mahalle", value = household.neighborhood)
                    DetailRow(label = "Ad Soyad", value = household.fullName)
                    DetailRow(label = "TC", value = household.tcNo)
                    DetailRow(label = "Cep 1", value = household.phone1)
                    DetailRow(label = "Cep 2", value = household.phone2.orEmpty())
                    DetailRow(label = "Adres", value = household.address)
                    DetailRow(label = "Durum", value = if (household.isActive) "Aktif" else "Pasif")
                    DetailRow(label = "Yeni Hane", value = household.isNewHousehold.toYesNoText())
                    DetailRow(label = "Acil Öncelik", value = household.isUrgent.toYesNoText())
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onEditClick(household.id) },
            ) {
                Text(text = "Düzenle")
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { showActiveDialog = true },
            ) {
                Text(text = if (household.isActive) "Pasife Al" else "Aktife Al")
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { showDeleteDialog = true },
            ) {
                Text(text = "Sil")
            }
        }
    }

    if (showDeleteDialog && household != null) {
        ConfirmActionDialog(
            text = "Bu hane silinecek. Devam etmek istiyor musunuz?",
            confirmText = "Sil",
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                onDeleteClick(household.id)
            },
        )
    }

    if (showActiveDialog && household != null) {
        val isActive = household.isActive
        ConfirmActionDialog(
            text = if (isActive) {
                "Bu hane pasife alınacak. Günlük görev listelerine dahil edilmeyecek."
            } else {
                "Bu hane tekrar aktif hale getirilecek."
            },
            confirmText = if (isActive) "Pasife Al" else "Aktife Al",
            onDismiss = { showActiveDialog = false },
            onConfirm = {
                showActiveDialog = false
                onToggleActiveClick(household.id)
            },
        )
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
        )

        Text(
            text = value.ifBlank { "-" },
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun ConfirmActionDialog(
    text: String,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "İptal")
            }
        },
        text = {
            Text(text = text)
        },
    )
}

private fun Boolean.toYesNoText(): String = if (this) "Evet" else "Hayır"
