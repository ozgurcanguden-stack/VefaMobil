package com.zgrcan.vefamobil.data.firebase

import com.zgrcan.vefamobil.model.Task
import com.zgrcan.vefamobil.repository.TaskRepository

class FirestoreTaskRepository : TaskRepository {
    override fun getTasks(): List<Task> {
        // TODO: Firestore dailyTasks collection will be read here.
        return emptyList()
    }

    override fun addTask(task: Task) {
        // TODO: Firestore add task implementation.
    }

    override fun updateTask(task: Task) {
        // TODO: Firestore update task implementation.
    }

    override fun deleteTask(id: String) {
        // TODO: Firestore delete task implementation.
    }
}
