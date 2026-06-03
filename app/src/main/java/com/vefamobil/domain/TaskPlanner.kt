package com.vefamobil.domain

import com.vefamobil.model.Household
import com.vefamobil.model.Settings
import com.vefamobil.model.TaskItem
import com.vefamobil.model.TaskItemStatus

class TaskPlanner {
    fun generateDailyTask(
        households: List<Household>,
        previousPendingItems: List<TaskItem>,
        settings: Settings,
    ): List<TaskItem> {
        val selectedItems = mutableListOf<TaskItem>()
        val selectedHouseholdIds = mutableSetOf<String>()

        fun addItem(item: TaskItem) {
            if (selectedHouseholdIds.add(item.householdId)) {
                selectedItems.add(item)
            }
        }

        activeUrgentHouseholds(households).forEach { household ->
            addItem(household.toTaskItem())
        }

        previousPendingItems
            .filter { it.status == TaskItemStatus.PENDING || it.status == TaskItemStatus.NOT_DONE }
            .forEach(::addItem)

        val activeNeighborhood = resolveActiveNeighborhood(households, settings)
        val neighborhoodHouseholds = households
            .filter { household ->
                household.isActive &&
                    household.neighborhood == activeNeighborhood &&
                    !household.isUrgent &&
                    !household.firstVisitCompleted
            }

        val newHouseholds = neighborhoodHouseholds.filter { it.isNewHousehold }
        val normalHouseholds = neighborhoodHouseholds.filterNot { it.isNewHousehold }

        (newHouseholds + normalHouseholds).forEach { household ->
            if (selectedItems.size < settings.dailyTargetCount) {
                addItem(household.toTaskItem())
            }
        }

        return selectedItems
    }

    private fun activeUrgentHouseholds(households: List<Household>): List<Household> {
        return households.filter { household ->
            household.isActive && household.isUrgent
        }
    }

    private fun resolveActiveNeighborhood(
        households: List<Household>,
        settings: Settings,
    ): String {
        val order = settings.neighborhoodOrder
        if (order.isEmpty()) return ""
        val startIndex = Math.floorMod(settings.currentNeighborhoodIndex, order.size)

        return order.indices
            .map { offset -> order[(startIndex + offset) % order.size] }
            .firstOrNull { neighborhood ->
                households.any { household ->
                    household.isActive &&
                        household.neighborhood == neighborhood &&
                        !household.firstVisitCompleted
                }
            }
            ?: order[startIndex]
    }

    private fun Household.toTaskItem(): TaskItem {
        return TaskItem(
            id = "item-$id",
            taskId = "",
            householdId = id,
            neighborhood = neighborhood,
            householdName = fullName,
            refCode = refCode,
            phone1 = phone1,
            phone2 = phone2,
            address = address,
            status = TaskItemStatus.PENDING,
            isUrgent = isUrgent,
            isNewHousehold = isNewHousehold,
            note = "",
        )
    }
}
