package com.vefamobil.data

import com.vefamobil.data.mock.MockDataStore
import com.vefamobil.model.Task
import com.vefamobil.repository.TaskRepository

class MockTaskRepository : TaskRepository {
    override fun getTasks(): List<Task> = MockDataStore.tasks.toList()

    override fun addTask(task: Task) {
        MockDataStore.tasks.add(task)
    }

    override fun updateTask(task: Task) {
        val index = MockDataStore.tasks.indexOfFirst { it.id == task.id }
        if (index >= 0) {
            MockDataStore.tasks[index] = task
        }
    }

    override fun deleteTask(id: String) {
        MockDataStore.tasks.removeAll { it.id == id }
    }
}
