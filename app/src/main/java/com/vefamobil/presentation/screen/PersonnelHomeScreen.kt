package com.vefamobil.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vefamobil.data.mock.MockDataStore
import com.vefamobil.model.Announcement
import com.vefamobil.presentation.AnnouncementUiState
import com.vefamobil.presentation.CallHelper
import com.vefamobil.model.TaskItem
import com.vefamobil.model.TaskItemStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PersonnelHomeScreen(
    @Suppress("UNUSED_PARAMETER")
    displayName: String,
    announcementState: AnnouncementUiState,
    onAnnouncementRead: (String) -> Unit,
) {
    val context = LocalContext.current
    val unreadAnnouncement = announcementState.announcements.firstOrNull { !it.isRead }
    val taskItems = remember {
        mutableStateListOf<TaskItem>().apply {
            addAll(MockDataStore.taskItems)
        }
    }
    var notDoneTask by remember { mutableStateOf<TaskItem?>(null) }
    var noteTask by remember { mutableStateOf<TaskItem?>(null) }

    val doneCount = taskItems.count { it.status == TaskItemStatus.DONE }
    val notDoneCount = taskItems.count { it.status == TaskItemStatus.NOT_DONE }
    val pendingCount = taskItems.count { it.status == TaskItemStatus.PENDING }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = "Bugünkü Görevler",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = "Toplam: ${taskItems.size}   Gidildi: $doneCount   Yapılmadı: $notDoneCount   Bekleyen: $pendingCount",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f),
                style = MaterialTheme.typography.bodyMedium,
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(
                    items = taskItems,
                    key = { taskItem -> taskItem.id },
                ) { taskItem ->
                    PersonnelTaskRow(
                        taskItem = taskItem,
                        onCallClick = {
                            CallHelper.openCallScreen(
                                context = context,
                                phone1 = taskItem.phone1,
                                phone2 = taskItem.phone2,
                            )
                        },
                        onDoneClick = {
                            taskItems.updateTaskItem(taskItem.id) {
                                it.copy(status = TaskItemStatus.DONE)
                            }
                            taskItems.firstOrNull { it.id == taskItem.id }?.let { updatedTaskItem ->
                                MockDataStore.updateTaskItem(updatedTaskItem)
                            }
                        },
                        onNotDoneClick = { notDoneTask = taskItem },
                        onNoteClick = { noteTask = taskItem },
                    )
                }
            }
        }
    }

    notDoneTask?.let { taskItem ->
        NotDoneDialog(
            onDismiss = { notDoneTask = null },
            onSave = { reason, description ->
                taskItems.updateTaskItem(taskItem.id) {
                    it.copy(
                        status = TaskItemStatus.NOT_DONE,
                        note = listOf(reason, description)
                            .filter { value -> value.isNotBlank() }
                        .joinToString(" - "),
                    )
                }
                taskItems.firstOrNull { it.id == taskItem.id }?.let { updatedTaskItem ->
                    MockDataStore.updateTaskItem(updatedTaskItem)
                    MockDataStore.addOrUpdateNote(updatedTaskItem)
                }
                notDoneTask = null
            },
        )
    }

    noteTask?.let { taskItem ->
        NoteDialog(
            initialNote = taskItem.note,
            onDismiss = { noteTask = null },
            onSave = { note ->
                taskItems.updateTaskItem(taskItem.id) {
                    it.copy(note = note)
                }
                taskItems.firstOrNull { it.id == taskItem.id }?.let { updatedTaskItem ->
                    MockDataStore.updateTaskItem(updatedTaskItem)
                    MockDataStore.addOrUpdateNote(updatedTaskItem)
                }
                noteTask = null
            },
        )
    }

    unreadAnnouncement?.let { announcement ->
        UnreadAnnouncementDialog(
            announcement = announcement,
            onReadClick = { onAnnouncementRead(announcement.id) },
        )
    }
}

@Composable
private fun PersonnelTaskRow(
    taskItem: TaskItem,
    onCallClick: () -> Unit,
    onDoneClick: () -> Unit,
    onNotDoneClick: () -> Unit,
    onNoteClick: () -> Unit,
) {
    val containerColor = when (taskItem.status) {
        TaskItemStatus.DONE -> Color(0xFFE8F5E9)
        TaskItemStatus.NOT_DONE -> Color(0xFFFFF3E0)
        TaskItemStatus.PENDING -> MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BadgeSlot(
                isUrgent = taskItem.isUrgent,
                isNewHousehold = taskItem.isNewHousehold,
            )

            Text(
                modifier = Modifier.weight(1f),
                text = "${taskItem.neighborhood} - ${taskItem.householdName}",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            CompactActionButton(text = "Ara", onClick = onCallClick)
            CompactActionButton(text = "Gidildi", onClick = onDoneClick)
            CompactActionButton(text = "Yapılmadı", onClick = onNotDoneClick)
            CompactActionButton(text = "Not", onClick = onNoteClick)
        }
    }
}

@Composable
private fun BadgeSlot(
    isUrgent: Boolean,
    isNewHousehold: Boolean,
) {
    Box(
        modifier = Modifier.width(54.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        val label = when {
            isUrgent -> "Acil"
            isNewHousehold -> "Yeni"
            else -> ""
        }

        if (label.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .background(
                        color = if (isUrgent) Color(0xFFFFE0B2) else Color(0xFFE3F2FD),
                        shape = RoundedCornerShape(8.dp),
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                text = label,
                color = if (isUrgent) Color(0xFF8A4B00) else MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun CompactActionButton(
    text: String,
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = Modifier.heightIn(min = 36.dp),
        onClick = onClick,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
        )
    }
}

@Composable
private fun NotDoneDialog(
    onDismiss: () -> Unit,
    onSave: (reason: String, description: String) -> Unit,
) {
    val reasons = listOf(
        "Evde yoktu",
        "Hastanedeydi",
        "İşteydi",
        "Kabul etmedi",
        "Daha sonra gelin dedi",
        "Diğer",
    )
    var selectedReason by rememberSaveable { mutableStateOf(reasons.first()) }
    var description by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onSave(selectedReason, description.trim()) }) {
                Text(text = "Kaydet")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "İptal")
            }
        },
        title = {
            Text(text = "Yapılmadı")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                reasons.forEach { reason ->
                    if (selectedReason == reason) {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { selectedReason = reason },
                        ) {
                            Text(text = reason)
                        }
                    } else {
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { selectedReason = reason },
                        ) {
                            Text(text = reason)
                        }
                    }
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(text = "Açıklama") },
                    minLines = 3,
                )
            }
        },
    )
}

@Composable
private fun NoteDialog(
    initialNote: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    var note by rememberSaveable(initialNote) { mutableStateOf(initialNote) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onSave(note.trim()) }) {
                Text(text = "Kaydet")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "İptal")
            }
        },
        title = {
            Text(text = "Not")
        },
        text = {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = note,
                onValueChange = { note = it },
                label = { Text(text = "Not") },
                minLines = 4,
            )
        },
    )
}

private fun MutableList<TaskItem>.updateTaskItem(
    id: String,
    transform: (TaskItem) -> TaskItem,
) {
    val index = indexOfFirst { it.id == id }
    if (index >= 0) {
        this[index] = transform(this[index])
    }
}

@Composable
private fun UnreadAnnouncementDialog(
    announcement: Announcement,
    onReadClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            Button(onClick = onReadClick) {
                Text(text = "OKUDUM")
            }
        },
        title = {
            Text(text = announcement.title)
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(text = announcement.message)
                Text(
                    text = "${announcement.createdAt.toAnnouncementDateText()}  ${announcement.createdAt.toAnnouncementTimeText()}",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        },
    )
}

private fun Long.toAnnouncementDateText(): String {
    return SimpleDateFormat("dd.MM.yyyy", Locale("tr", "TR")).format(Date(this))
}

private fun Long.toAnnouncementTimeText(): String {
    return SimpleDateFormat("HH:mm", Locale("tr", "TR")).format(Date(this))
}
