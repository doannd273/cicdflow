package com.example.cicdflow.domain

import com.example.cicdflow.data.Task

class TaskRepository {

    // Fake data source (memory)
    private val tasks = mutableListOf<Task>()

    /**
     * Add new task
     * Ưu điểm:
     * - Đơn giản
     * Nhược:
     * - Không persist (production sẽ dùng Room/API)
     */
    fun addTask(task: Task) {
        tasks.add(task)
    }

    /**
     * Get all tasks
     */
    fun getTasks(): List<Task> {
        return tasks
    }
}
