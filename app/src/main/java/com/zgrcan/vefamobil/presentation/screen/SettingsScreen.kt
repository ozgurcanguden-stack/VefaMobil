package com.zgrcan.vefamobil.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zgrcan.vefamobil.model.Settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: Settings,
    onBackClick: () -> Unit,
    onDailyTargetChange: (String) -> Unit,
    onWorkStartTimeChange: (String) -> Unit,
    onWorkEndTimeChange: (String) -> Unit,
    onSalaryReminderTimeChange: (String) -> Unit,
    onBiometricEnabledChange: (Boolean) -> Unit,
    onAddNeighborhood: (String) -> Unit,
    onRemoveNeighborhood: (String) -> Unit,
    onPasswordChangeClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    val context = LocalContext.current
    var showNeighborhoodDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Ayarlar ve Güvenlik") },
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
            SettingsSection(title = "Günlük Görev Ayarları") {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = settings.dailyTargetCount.toString(),
                    onValueChange = onDailyTargetChange,
                    label = { Text(text = "Günlük hedef hane sayısı") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = settings.workStartTime,
                    onValueChange = onWorkStartTimeChange,
                    label = { Text(text = "Mesai başlangıç saati") },
                    singleLine = true,
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = settings.workEndTime,
                    onValueChange = onWorkEndTimeChange,
                    label = { Text(text = "Mesai bitiş saati") },
                    singleLine = true,
                )
            }

            SettingsSection(title = "Mahalle Sırası") {
                settings.neighborhoodOrder.forEachIndexed { index, neighborhood ->
                    NeighborhoodRow(
                        orderNumber = index + 1,
                        neighborhood = neighborhood,
                        onRemoveClick = { onRemoveNeighborhood(neighborhood) },
                    )
                }

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showNeighborhoodDialog = true },
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Mahalle Ekle")
                }
            }

            SettingsSection(title = "Maaş İmza Bildirimi") {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = settings.salaryReminderTime,
                    onValueChange = onSalaryReminderTimeChange,
                    label = { Text(text = "Bildirim saati") },
                    singleLine = true,
                )

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        Toast
                            .makeText(context, "Ses seçimi sonraki aşamada eklenecek.", Toast.LENGTH_SHORT)
                            .show()
                    },
                ) {
                    Text(text = "Bildirim sesi: ${settings.notificationSoundSalary}")
                }
            }

            SettingsSection(title = "Güvenlik") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "Biyometrik doğrulama",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = "Aktif edildiğinde uygulama ilk açılışta ve arka plandan dönüşte doğrulama ister.",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }

                    Switch(
                        checked = settings.biometricEnabled,
                        onCheckedChange = onBiometricEnabledChange,
                    )
                }
            }

            SettingsSection(title = "Hesap") {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onPasswordChangeClick,
                ) {
                    Text(text = "Şifre Değiştir")
                }

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onLogoutClick,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                ) {
                    Text(text = "Çıkış Yap")
                }
            }
        }
    }

    if (showNeighborhoodDialog) {
        AddNeighborhoodDialog(
            onDismiss = { showNeighborhoodDialog = false },
            onAddClick = { neighborhood ->
                onAddNeighborhood(neighborhood)
                showNeighborhoodDialog = false
            },
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
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
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )

            content()
        }
    }
}

@Composable
private fun NeighborhoodRow(
    orderNumber: Int,
    neighborhood: String,
    onRemoveClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "$orderNumber.",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
        )

        Text(
            modifier = Modifier.weight(1f),
            text = neighborhood,
            style = MaterialTheme.typography.bodyLarge,
        )

        IconButton(onClick = onRemoveClick) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Sil",
                tint = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun AddNeighborhoodDialog(
    onDismiss: () -> Unit,
    onAddClick: (String) -> Unit,
) {
    var neighborhood by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onAddClick(neighborhood) }) {
                Text(text = "Ekle")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "İptal")
            }
        },
        title = {
            Text(text = "Mahalle Ekle")
        },
        text = {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = neighborhood,
                onValueChange = { neighborhood = it },
                label = { Text(text = "Mahalle adı") },
                singleLine = true,
            )
        },
    )
}
