package com.zgrcan.vefamobil.presentation.screen

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zgrcan.vefamobil.model.Personnel
import com.zgrcan.vefamobil.presentation.PersonnelUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonnelListScreen(
    state: PersonnelUiState,
    onBackClick: () -> Unit,
    onAddPersonnelClick: () -> Unit,
    onPersonnelClick: (String) -> Unit,
    onEditClick: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    onToggleActiveClick: (String) -> Unit,
) {
    var pendingDeletePersonnel by remember { mutableStateOf<Personnel?>(null) }
    var pendingTogglePersonnel by remember { mutableStateOf<Personnel?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Personeller") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Geri",
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = onAddPersonnelClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null,
                        )
                        Text(text = "Personel Ekle")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text(text = "Personel ara") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                    )
                },
                singleLine = true,
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(
                    items = state.personnelList,
                    key = { personnel -> personnel.id },
                ) { personnel ->
                    PersonnelRow(
                        personnel = personnel,
                        onClick = { onPersonnelClick(personnel.id) },
                        onEditClick = { onEditClick(personnel.id) },
                        onToggleActiveClick = { pendingTogglePersonnel = personnel },
                        onDeleteClick = { pendingDeletePersonnel = personnel },
                    )
                }
            }
        }
    }

    pendingDeletePersonnel?.let { personnel ->
        AlertDialog(
            onDismissRequest = { pendingDeletePersonnel = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        pendingDeletePersonnel = null
                        onDeleteClick(personnel.id)
                    },
                ) {
                    Text(text = "Sil")
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDeletePersonnel = null }) {
                    Text(text = "İptal")
                }
            },
            text = {
                Text(text = "Bu personel silinecek. Devam etmek istiyor musunuz?")
            },
        )
    }

    pendingTogglePersonnel?.let { personnel ->
        AlertDialog(
            onDismissRequest = { pendingTogglePersonnel = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        pendingTogglePersonnel = null
                        onToggleActiveClick(personnel.id)
                    },
                ) {
                    Text(text = if (personnel.isActive) "Pasife Al" else "Aktife Al")
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingTogglePersonnel = null }) {
                    Text(text = "İptal")
                }
            },
            text = {
                Text(
                    text = if (personnel.isActive) {
                        "Bu personel pasife alınacak."
                    } else {
                        "Bu personel tekrar aktif hale getirilecek."
                    },
                )
            },
        )
    }
}

@Composable
private fun PersonnelRow(
    personnel: Personnel,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onToggleActiveClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val textAlpha = if (personnel.isActive) 1f else 0.48f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (personnel.isActive) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = personnel.fullName,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = textAlpha),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )

                Text(
                    text = personnel.username,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (personnel.isActive) 0.72f else 0.48f),
                    style = MaterialTheme.typography.bodyMedium,
                )

                Text(
                    text = if (personnel.isActive) "Aktif" else "Pasif",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (personnel.isActive) 0.72f else 0.48f),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                )
            }

            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Düzenle",
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = textAlpha),
                )
            }

            IconButton(onClick = onToggleActiveClick) {
                Icon(
                    imageVector = Icons.Outlined.Block,
                    contentDescription = if (personnel.isActive) "Pasife Al" else "Aktife Al",
                    tint = MaterialTheme.colorScheme.secondary.copy(alpha = textAlpha),
                )
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Sil",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = textAlpha),
                )
            }
        }
    }
}
