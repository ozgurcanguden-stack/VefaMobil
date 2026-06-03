package com.vefamobil.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.vefamobil.data.MockTaskRepository
import com.vefamobil.data.TaskPlannerMockData
import com.vefamobil.data.mock.MockDataStore
import com.vefamobil.domain.TaskPlanner
import com.vefamobil.model.Household
import com.vefamobil.model.Task
import com.vefamobil.model.TaskCreatedMode
import com.vefamobil.model.TaskItem
import com.vefamobil.model.TaskItemStatus
import com.vefamobil.model.TaskPublishMode
import com.vefamobil.repository.TaskRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
)

class TaskViewModel : ViewModel() {
    private val taskRepository: TaskRepository = MockTaskRepository()
    private val taskPlanner = TaskPlanner()
    private val generatedTaskItemsByTaskId = mutableMapOf<String, List<TaskItem>>()
    private var allTasks: List<Task> = emptyList()

    var state by mutableStateOf(TaskUiState())
        private set

    init {
        loadTasks()
    }

    fun loadTasks() {
        allTasks = taskRepository.getTasks()
        state = state.copy(tasks = allTasks)
    }

    fun addTask(
        neighborhood: String,
        totalHouseholds: Int,
        createdMode: TaskCreatedMode,
        publishMode: TaskPublishMode,
    ) {
        val task = Task(
            id = UUID.randomUUID().toString(),
            taskDate = publishMode.toTaskDate(),
            neighborhood = neighborhood,
            totalHouseholds = totalHouseholds,
            completedCount = 0,
            status = "Planlandı",
            createdMode = createdMode,
            publishMode = publishMode,
            createdAt = System.currentTimeMillis(),
        )

        taskRepository.addTask(task)
        loadTasks()
    }

    fun createAutomaticTask(publishMode: TaskPublishMode) {
        val generatedItems = taskPlanner.generateDailyTask(
            households = MockDataStore.households,
            previousPendingItems = MockDataStore.taskItems.filter {
                it.status == TaskItemStatus.PENDING || it.status == TaskItemStatus.NOT_DONE
            },
            settings = TaskPlannerMockData.settings,
        )
        val taskId = UUID.randomUUID().toString()
        val taskItems = generatedItems.map { taskItem ->
            taskItem.copy(
                id = "$taskId-${taskItem.householdId}",
                taskId = taskId,
            )
        }
        val neighborhoods = taskItems.map { it.neighborhood }.distinct()
        val task = Task(
            id = taskId,
            taskDate = publishMode.toTaskDate(),
            neighborhood = if (neighborhoods.size == 1) {
                "${neighborhoods.first()} Mahallesi"
            } else {
                "Mahalle Sıralı Plan"
            },
            totalHouseholds = taskItems.size,
            completedCount = taskItems.count { it.status == TaskItemStatus.DONE },
            status = "Planlandı",
            createdMode = TaskCreatedMode.AUTO,
            publishMode = publishMode,
            createdAt = System.currentTimeMillis(),
        )

        generatedTaskItemsByTaskId[taskId] = taskItems
        MockDataStore.taskItems.addAll(taskItems)
        taskRepository.addTask(task)
        loadTasks()
    }

    fun getTask(id: String): Task? {
        val visibleTasks = state.tasks
        return allTasks.firstOrNull { it.id == id }
            ?: visibleTasks.firstOrNull { it.id == id }
    }

    fun getTaskHouseholds(taskId: String): List<Household> {
        generatedTaskItemsByTaskId[taskId]?.let { taskItems ->
            return taskItems.map { taskItem ->
                Household(
                    id = taskItem.householdId,
                    refCode = taskItem.refCode,
                    neighborhood = taskItem.neighborhood,
                    fullName = taskItem.householdName,
                    tcNo = "",
                    phone1 = taskItem.phone1,
                    phone2 = taskItem.phone2,
                    address = taskItem.address,
                    isActive = true,
                    isNewHousehold = taskItem.isNewHousehold,
                    isUrgent = taskItem.isUrgent,
                    firstVisitCompleted = taskItem.status == TaskItemStatus.DONE,
                )
            }
        }

        val task = getTask(taskId)
        val neighborhood = task?.neighborhood?.removeSuffix(" Mahallesi").orEmpty()

        return listOf(
            Household(
                id = "task-$taskId-1",
                refCode = "REF001",
                neighborhood = neighborhood.ifBlank { "Hürriyet" },
                fullName = "Ahmet Yılmaz",
                tcNo = "11111111111",
                phone1 = "05550000001",
                phone2 = null,
                address = "${neighborhood.ifBlank { "Hürriyet" }} Mahallesi",
                isActive = true,
                isNewHousehold = true,
                isUrgent = false,
                firstVisitCompleted = true,
            ),
            Household(
                id = "task-$taskId-2",
                refCode = "REF002",
                neighborhood = neighborhood.ifBlank { "Hürriyet" },
                fullName = "Ayşe Kaya",
                tcNo = "22222222222",
                phone1 = "05550000002",
                phone2 = null,
                address = "${neighborhood.ifBlank { "Hürriyet" }} Mahallesi",
                isActive = true,
                isNewHousehold = false,
                isUrgent = true,
                firstVisitCompleted = false,
            ),
        )
    }

    private fun TaskPublishMode.toTaskDate(): String {
        val calendar = Calendar.getInstance()
        if (this == TaskPublishMode.TOMORROW) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return SimpleDateFormat("dd.MM.yyyy", Locale("tr", "TR")).format(calendar.time)
    }
}
