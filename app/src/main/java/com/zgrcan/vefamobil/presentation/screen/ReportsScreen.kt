package com.zgrcan.vefamobil.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zgrcan.vefamobil.presentation.NeighborhoodReport
import com.zgrcan.vefamobil.presentation.PendingNoteReport
import com.zgrcan.vefamobil.presentation.ReportCardItem
import com.zgrcan.vefamobil.presentation.ReportsSummary
import com.zgrcan.vefamobil.presentation.ReportsUiState
import com.zgrcan.vefamobil.presentation.StaleHouseholdReport

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    state: ReportsUiState,
    onBackClick: () -> Unit,
    onLoad: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        onLoad()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Raporlar") },
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
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    Toast
                        .makeText(context, "Excel dışa aktarma sonraki aşamada eklenecek.", Toast.LENGTH_SHORT)
                        .show()
                },
            ) {
                Icon(
                    imageVector = Icons.Outlined.Download,
                    contentDescription = null,
                )
                Text(text = "Excel Dışa Aktar")
            }

            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                ResponsiveReportGrid(
                    items = state.dashboardCards,
                    columns = if (maxWidth < 620.dp) 2 else 3,
                ) { item ->
                    ReportDashboardCard(item = item)
                }
            }

            ReportSection(title = "Genel İstatistikler") {
                SummaryReportCard(summary = state.summary)
            }

            ReportSection(title = "Mahalle Bazlı Rapor") {
                state.neighborhoodReports.forEach { report ->
                    NeighborhoodReportCard(report = report)
                }
            }

            ReportSection(title = "Uzun Süredir Gidilmeyen Haneler") {
                state.staleHouseholds.forEach { report ->
                    StaleHouseholdCard(report = report)
                }
            }

            ReportSection(title = "Not Bekleyenler") {
                state.pendingNotes.forEach { report ->
                    PendingNoteCard(report = report)
                }
            }
        }
    }
}

@Composable
private fun SummaryReportCard(
    summary: ReportsSummary,
) {
    val items = listOf(
        "Toplam Hane" to summary.totalHouseholds,
        "Aktif Hane" to summary.activeHouseholds,
        "Pasif Hane" to summary.passiveHouseholds,
        "Yeni Hane" to summary.newHouseholds,
        "Acil Hane" to summary.urgentHouseholds,
        "Toplam Görev" to summary.totalTasks,
        "Tamamlanan Görev" to summary.completedTasks,
        "Yapılmayan Görev" to summary.notDoneTasks,
        "Bekleyen Görev" to summary.pendingTasks,
        "Not Girilen Görev" to summary.tasksWithNotes,
    )

    ReportInfoCard {
        items.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                rowItems.forEach { (label, value) ->
                    SummaryItem(
                        modifier = Modifier.weight(1f),
                        label = label,
                        value = value,
                    )
                }

                repeat(2 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun SummaryItem(
    modifier: Modifier = Modifier,
    label: String,
    value: Int,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = value.toString(),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun ReportDashboardCard(
    item: ReportCardItem,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(104.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
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
private fun ReportSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )

        content()
    }
}

@Composable
private fun NeighborhoodReportCard(
    report: NeighborhoodReport,
) {
    ReportInfoCard {
        Text(
            text = report.neighborhood,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Text(text = "Toplam hane: ${report.totalHouseholds}")
        Text(text = "Aktif hane: ${report.activeHouseholds}")
        Text(text = "Pasif hane: ${report.passiveHouseholds}")
        Text(text = "Tamamlanan görev: ${report.completedTasks}")
        Text(text = "Yapılmayan görev: ${report.notDoneTasks}")
    }
}

@Composable
private fun StaleHouseholdCard(
    report: StaleHouseholdReport,
) {
    ReportInfoCard {
        Text(
            text = report.fullName,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Text(text = report.neighborhood)
        Text(text = "Son ziyaret: ${report.lastVisitDate}")
        Text(text = "${report.daysPassed} gün oldu")
    }
}

@Composable
private fun PendingNoteCard(
    report: PendingNoteReport,
) {
    ReportInfoCard {
        Text(
            text = report.householdName,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Text(text = report.neighborhood)
        Text(text = report.noteSummary)
        Text(text = report.date)
    }
}

@Composable
private fun ReportInfoCard(
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
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            content = content,
        )
    }
}

@Composable
private fun <T> ResponsiveReportGrid(
    items: List<T>,
    columns: Int,
    itemContent: @Composable (T) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items.chunked(columns).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top,
            ) {
                rowItems.forEach { item ->
                    Column(modifier = Modifier.weight(1f)) {
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
