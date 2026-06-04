package com.zgrcan.vefamobil.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zgrcan.vefamobil.model.TaskCreatedMode
import com.zgrcan.vefamobil.model.TaskPublishMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormScreen(
    onBackClick: () -> Unit,
    onSaveTask: (
        neighborhood: String,
        totalHouseholds: Int,
        createdMode: TaskCreatedMode,
        publishMode: TaskPublishMode,
    ) -> Unit,
) {
    val neighborhoods = listOf("Hürriyet Mahallesi", "Cumhuriyet Mahallesi", "Acarlar Mahallesi")
    var selectedNeighborhood by rememberSaveable { mutableStateOf(neighborhoods.first()) }
    var targetHouseholdCount by rememberSaveable { mutableStateOf("") }
    var createdMode by rememberSaveable { mutableStateOf(TaskCreatedMode.AUTO) }
    var showPublishDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Görev Oluştur") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = "Mahalle seçimi",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
            )

            neighborhoods.forEach { neighborhood ->
                val selected = neighborhood == selectedNeighborhood
                if (selected) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { selectedNeighborhood = neighborhood },
                    ) {
                        Text(text = neighborhood)
                    }
                } else {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { selectedNeighborhood = neighborhood },
                    ) {
                        Text(text = neighborhood)
                    }
                }
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = targetHouseholdCount,
                onValueChange = { targetHouseholdCount = it.filter(Char::isDigit) },
                label = { Text(text = "Hedef hane sayısı") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
            )

            Text(
                text = "Oluşturma tipi",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                if (createdMode == TaskCreatedMode.AUTO) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { createdMode = TaskCreatedMode.AUTO },
                    ) {
                        Text(text = "Otomatik")
                    }
                } else {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = { createdMode = TaskCreatedMode.AUTO },
                    ) {
                        Text(text = "Otomatik")
                    }
                }

                if (createdMode == TaskCreatedMode.MANUAL) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { createdMode = TaskCreatedMode.MANUAL },
                    ) {
                        Text(text = "Manuel")
                    }
                } else {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = { createdMode = TaskCreatedMode.MANUAL },
                    ) {
                        Text(text = "Manuel")
                    }
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { showPublishDialog = true },
            ) {
                Text(text = "Kaydet")
            }
        }
    }

    if (showPublishDialog) {
        AlertDialog(
            onDismissRequest = { showPublishDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPublishDialog = false
                        onSaveTask(
                            selectedNeighborhood,
                            targetHouseholdCount.toIntOrNull() ?: 0,
                            createdMode,
                            TaskPublishMode.TODAY,
                        )
                    },
                ) {
                    Text(text = "Bugün Yayınla")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPublishDialog = false
                        onSaveTask(
                            selectedNeighborhood,
                            targetHouseholdCount.toIntOrNull() ?: 0,
                            createdMode,
                            TaskPublishMode.TOMORROW,
                        )
                    },
                ) {
                    Text(text = "Yarın Yayınla")
                }
            },
            text = {
                Text(text = "Bu görevleri bugün mü, yarın mı yayınlamak istiyorsunuz?")
            },
        )
    }
}
