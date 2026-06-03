package com.vefamobil.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ManagerHomeScreen(
    @Suppress("UNUSED_PARAMETER")
    displayName: String,
    onHouseholdsClick: () -> Unit,
    onPersonnelClick: () -> Unit,
    onTasksClick: () -> Unit,
) {
    val context = LocalContext.current
    val toastMessage = "Bu ekran sonraki aşamada eklenecek."
    val dashboardItems = listOf(
        DashboardItem(title = "Toplam Hane", value = "0"),
        DashboardItem(title = "Aktif Hane", value = "0"),
        DashboardItem(title = "Pasif Hane", value = "0"),
        DashboardItem(title = "Bugünkü Görevler", value = "0"),
    )
    val actionItems = listOf(
        "Haneler",
        "Personeller",
        "Görevler",
        "Raporlar",
        "Duyurular",
        "Ayarlar",
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 920.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp),
            ) {
                ManagerHeader()

                WelcomeCard()

                BoxWithConstraints(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ResponsiveGrid(
                        items = dashboardItems,
                        columns = if (maxWidth < 640.dp) 2 else 4,
                    ) { item ->
                        DashboardCard(item = item)
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "İşlemler",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )

                    BoxWithConstraints(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        ResponsiveGrid(
                            items = actionItems,
                            columns = if (maxWidth < 520.dp) 1 else 2,
                        ) { label ->
                            DashboardActionButton(
                                label = label,
                                onClick = {
                                    if (label == "Haneler") {
                                        onHouseholdsClick()
                                    } else if (label == "Personeller") {
                                        onPersonnelClick()
                                    } else if (label == "Görevler") {
                                        onTasksClick()
                                    } else {
                                        Toast
                                            .makeText(context, toastMessage, Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ManagerHeader() {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = "Vefa Mobil",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = "Müdür Ana Sayfa",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f),
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun WelcomeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
    ) {
        Text(
            modifier = Modifier.padding(18.dp),
            text = "Hoş geldiniz, Vefa Müdürü",
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun DashboardCard(
    item: DashboardItem,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = item.title,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )

            Text(
                text = item.value,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun DashboardActionButton(
    label: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = onClick,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun <T> ResponsiveGrid(
    items: List<T>,
    columns: Int,
    itemContent: @Composable (T) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items.chunked(columns).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                rowItems.forEach { item ->
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        itemContent(item)
                    }
                }

                repeat(columns - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

private data class DashboardItem(
    val title: String,
    val value: String,
)
