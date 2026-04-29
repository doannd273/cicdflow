package com.example.cicdflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cicdflow.data.Task
import com.example.cicdflow.domain.TaskRepository

class MainViewModel : ViewModel() {

    private val repository = TaskRepository()

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    /**
     * Load tasks from repository
     * Production:
     * - sẽ dùng Flow / suspend
     */
    fun loadTasks() {
        _tasks.value = repository.getTasks()
    }

    /**
     * Add task
     */
    fun addTask(title: String) {
        val newTask = Task(
            id = System.currentTimeMillis().toInt(),
            title = title
        )
        repository.addTask(newTask)
        loadTasks()
    }
}
