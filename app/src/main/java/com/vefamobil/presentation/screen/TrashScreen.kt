package com.vefamobil.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vefamobil.model.TrashItem
import com.vefamobil.presentation.TrashAuditUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashScreen(
    state: TrashAuditUiState,
    onBackClick: () -> Unit,
    onLoad: () -> Unit,
    onRestoreClick: (String) -> Unit,
    onPermanentDeleteClick: (String) -> Unit,
) {
    val context = LocalContext.current
    var pendingDeleteItem by remember { mutableStateOf<TrashItem?>(null) }

    LaunchedEffect(Unit) {
        onLoad()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Çöp Kutusu") },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (state.trashItems.isEmpty()) {
                item {
                    Text(
                        text = "Çöp kutusunda kayıt yok.",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.68f),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            items(
                items = state.trashItems,
                key = { item -> item.id },
            ) { item ->
                TrashItemCard(
                    item = item,
                    onRestoreClick = {
                        onRestoreClick(item.id)
                        Toast
                            .makeText(context, "Kayıt geri yüklendi", Toast.LENGTH_SHORT)
                            .show()
                    },
                    onPermanentDeleteClick = { pendingDeleteItem = item },
                )
            }
        }
    }

    pendingDeleteItem?.let { item ->
        AlertDialog(
            onDismissRequest = { pendingDeleteItem = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        pendingDeleteItem = null
                        onPermanentDeleteClick(item.id)
                    },
                ) {
                    Text(text = "Kalıcı Sil")
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDeleteItem = null }) {
                    Text(text = "İptal")
                }
            },
            text = {
                Text(text = "Bu kayıt kalıcı olarak silinecek. Devam etmek istiyor musunuz?")
            },
        )
    }
}

@Composable
private fun TrashItemCard(
    item: TrashItem,
    onRestoreClick: () -> Unit,
    onPermanentDeleteClick: () -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = item.title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(text = "Kayıt türü: ${item.entityType}")
            Text(text = "Silinme tarihi: ${item.deletedAt}")
            Text(text = "Silen kullanıcı: ${item.deletedBy}")
            Text(
                text = item.description,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                style = MaterialTheme.typography.bodyMedium,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = item.canRestore,
                    onClick = onRestoreClick,
                ) {
                    Text(text = "Geri Yükle")
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onPermanentDeleteClick,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                ) {
                    Text(text = "Kalıcı Sil")
                }
            }
        }
    }
}
