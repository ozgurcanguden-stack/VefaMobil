package com.vefamobil.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vefamobil.model.Announcement
import com.vefamobil.presentation.AnnouncementUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementsScreen(
    state: AnnouncementUiState,
    onBackClick: () -> Unit,
) {
    val unreadCount = state.announcements.count { !it.isRead }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Duyurular") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Geri",
                        )
                    }
                },
                actions = {
                    NotificationIcon(unreadCount = unreadCount)
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(
                items = state.announcements,
                key = { announcement -> announcement.id },
            ) { announcement ->
                AnnouncementCard(announcement = announcement)
            }
        }
    }
}

@Composable
private fun NotificationIcon(
    unreadCount: Int,
) {
    Box(
        modifier = Modifier.padding(end = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = "Bildirimler",
        )

        if (unreadCount > 0) {
            Text(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 10.dp, y = (-8).dp)
                    .background(
                        color = MaterialTheme.colorScheme.error,
                        shape = CircleShape,
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                text = unreadCount.toString(),
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun AnnouncementCard(
    announcement: Announcement,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (announcement.isRead) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.primaryContainer
            },
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = announcement.title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = announcement.message,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f),
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = "${announcement.createdAt.toDateText()}  ${announcement.createdAt.toTimeText()}",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f),
                style = MaterialTheme.typography.bodySmall,
            )

            Text(
                text = if (announcement.isRead) "Okundu" else "Okunmadı",
                color = if (announcement.isRead) {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f)
                } else {
                    MaterialTheme.colorScheme.secondary
                },
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

private fun Long.toDateText(): String {
    return SimpleDateFormat("dd.MM.yyyy", Locale("tr", "TR")).format(Date(this))
}

private fun Long.toTimeText(): String {
    return SimpleDateFormat("HH:mm", Locale("tr", "TR")).format(Date(this))
}
