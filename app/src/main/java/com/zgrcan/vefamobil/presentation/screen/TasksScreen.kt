package com.zgrcan.vefamobil.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.zgrcan.vefamobil.model.Task
import com.zgrcan.vefamobil.model.TaskPublishMode
import com.zgrcan.vefamobil.presentation.TaskUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    state: TaskUiState,
    onBackClick: () -> Unit,
    onCreateTaskClick: () -> Unit,
    onAutoCreateTaskClick: (TaskPublishMode) -> Unit,
    onTaskClick: (String) -> Unit,
) {
    var showPublishDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Görevler") },
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
                        onClick = onCreateTaskClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null,
                        )
                        Text(text = "Görev Oluştur")
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
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { showPublishDialog = true },
            ) {
                Text(text = "Otomatik Görev Oluştur")
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(
                    items = state.tasks,
                    key = { task -> task.id },
                ) { task ->
                    TaskCard(
                        task = task,
                        onClick = { onTaskClick(task.id) },
                    )
                }
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
                        onAutoCreateTaskClick(TaskPublishMode.TODAY)
                    },
                ) {
                    Text(text = "Bugün Yayınla")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPublishDialog = false
                        onAutoCreateTaskClick(TaskPublishMode.TOMORROW)
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

@Composable
private fun TaskCard(
    task: Task,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = task.taskDate,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = task.neighborhood,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
            )

            Text(
                text = "${task.totalHouseholds} Hane",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = "${task.completedCount} Tamamlandı",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = task.status,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}
