package com.vefamobil.repository

import com.vefamobil.model.Task

interface TaskRepository {
    fun getTasks(): List<Task>

    fun addTask(task: Task)

    fun updateTask(task: Task)

    fun deleteTask(id: String)
}
