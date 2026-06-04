package com.zgrcan.vefamobil.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zgrcan.vefamobil.data.mock.MockDataStore
import com.zgrcan.vefamobil.data.MockHouseholdRepository
import com.zgrcan.vefamobil.model.TaskItemStatus

data class ReportsUiState(
    val summary: ReportsSummary = ReportsSummary(),
    val dashboardCards: List<ReportCardItem> = emptyList(),
    val neighborhoodReports: List<NeighborhoodReport> = emptyList(),
    val staleHouseholds: List<StaleHouseholdReport> = emptyList(),
    val pendingNotes: List<PendingNoteReport> = emptyList(),
)

data class ReportsSummary(
    val totalHouseholds: Int = 0,
    val activeHouseholds: Int = 0,
    val passiveHouseholds: Int = 0,
    val newHouseholds: Int = 0,
    val urgentHouseholds: Int = 0,
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val notDoneTasks: Int = 0,
    val pendingTasks: Int = 0,
    val tasksWithNotes: Int = 0,
)

data class ReportCardItem(
    val title: String,
    val value: String,
)

data class NeighborhoodReport(
    val neighborhood: String,
    val totalHouseholds: Int,
    val activeHouseholds: Int,
    val passiveHouseholds: Int,
    val completedTasks: Int,
    val notDoneTasks: Int,
)

data class StaleHouseholdReport(
    val fullName: String,
    val neighborhood: String,
    val lastVisitDate: String,
    val daysPassed: Int,
)

data class PendingNoteReport(
    val householdName: String,
    val neighborhood: String,
    val noteSummary: String,
    val date: String,
)

class ReportsViewModel : ViewModel() {
    private val householdRepository = MockHouseholdRepository()

    var state by mutableStateOf(ReportsUiState())
        private set

    init {
        loadReports()
    }

    fun loadReports() {
        val households = householdRepository.getHouseholds()
        val taskItems = MockDataStore.taskItems
        val completedTasks = taskItems.count { it.status == TaskItemStatus.DONE }
        val totalTaskItems = taskItems.size
        val notDoneTasks = taskItems.count { it.status == TaskItemStatus.NOT_DONE }
        val tasksWithNotes = MockDataStore.notes.size
        val pendingTasks = (totalTaskItems - completedTasks - notDoneTasks).coerceAtLeast(0)
        val summary = ReportsSummary(
            totalHouseholds = households.size,
            activeHouseholds = households.count { it.isActive },
            passiveHouseholds = households.count { !it.isActive },
            newHouseholds = households.count { it.isNewHousehold },
            urgentHouseholds = households.count { it.isUrgent },
            totalTasks = totalTaskItems,
            completedTasks = completedTasks,
            notDoneTasks = notDoneTasks,
            pendingTasks = pendingTasks,
            tasksWithNotes = tasksWithNotes,
        )

        state = ReportsUiState(
            summary = summary,
            dashboardCards = listOf(
                ReportCardItem("Toplam Hane", summary.totalHouseholds.toString()),
                ReportCardItem("Aktif Hane", summary.activeHouseholds.toString()),
                ReportCardItem("Pasif Hane", summary.passiveHouseholds.toString()),
                ReportCardItem("Bu Ay Gidilen", summary.completedTasks.toString()),
                ReportCardItem("Bu Ay Yapılmayan", summary.notDoneTasks.toString()),
                ReportCardItem("Not Bekleyen", summary.tasksWithNotes.toString()),
            ),
            neighborhoodReports = households
                .groupBy { it.neighborhood }
                .map { (neighborhood, neighborhoodHouseholds) ->
                    NeighborhoodReport(
                        neighborhood = neighborhood,
                        totalHouseholds = neighborhoodHouseholds.size,
                        activeHouseholds = neighborhoodHouseholds.count { it.isActive },
                        passiveHouseholds = neighborhoodHouseholds.count { !it.isActive },
                        completedTasks = taskItems.count {
                            it.neighborhood == neighborhood && it.status == TaskItemStatus.DONE
                        },
                        notDoneTasks = taskItems.count {
                            it.neighborhood == neighborhood && it.status == TaskItemStatus.NOT_DONE
                        },
                    )
                },
            staleHouseholds = listOf(
                StaleHouseholdReport("Ahmet Yılmaz", "Hürriyet", "12.05.2026", 23),
                StaleHouseholdReport("Mehmet Demir", "Acarlar", "03.05.2026", 32),
                StaleHouseholdReport("Ayşe Kaya", "Cumhuriyet", "28.04.2026", 37),
            ),
            pendingNotes = MockDataStore.notes.map { note ->
                PendingNoteReport(
                    householdName = note.householdName,
                    neighborhood = note.neighborhood,
                    noteSummary = note.summary,
                    date = note.createdAt,
                )
            },
        )
    }
}
