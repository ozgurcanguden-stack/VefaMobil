package com.vefamobil.data

import com.vefamobil.model.Task
import com.vefamobil.model.TaskCreatedMode
import com.vefamobil.model.TaskPublishMode
import com.vefamobil.repository.TaskRepository

class MockTaskRepository : TaskRepository {
    private val tasks = mutableListOf(
        Task(
            id = "1",
            taskDate = "03.06.2026",
            neighborhood = "Hürriyet Mahallesi",
            totalHouseholds = 6,
            completedCount = 4,
            status = "Devam Ediyor",
            createdMode = TaskCreatedMode.AUTO,
            publishMode = TaskPublishMode.TODAY,
            createdAt = System.currentTimeMillis(),
        ),
        Task(
            id = "2",
            taskDate = "03.06.2026",
            neighborhood = "Cumhuriyet Mahallesi",
            totalHouseholds = 5,
            completedCount = 5,
            status = "Tamamlandı",
            createdMode = TaskCreatedMode.MANUAL,
            publishMode = TaskPublishMode.TODAY,
            createdAt = System.currentTimeMillis(),
        ),
    )

    override fun getTasks(): List<Task> = tasks.toList()

    override fun addTask(task: Task) {
        tasks.add(task)
    }

    override fun updateTask(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index >= 0) {
            tasks[index] = task
        }
    }

    override fun deleteTask(id: String) {
        tasks.removeAll { it.id == id }
    }
}
