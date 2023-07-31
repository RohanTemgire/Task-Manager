package com.example.taskmanager.data.repository

import com.example.taskmanager.data.TaskDao
import com.example.taskmanager.data.models.TaskEntity
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// this indicates that our task repository will only be alive until our shared view module
@ViewModelScoped
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    val getAllTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()
    val sortByLowPriority: Flow<List<TaskEntity>> = taskDao.sortByLowPriority()
    val sortByHighPriority: Flow<List<TaskEntity>> = taskDao.sortByHighPriority()

    fun getSelectedTask(taskId: Int): Flow<TaskEntity> {
        return taskDao.getSelectedTask(taskId)
    }

    suspend fun addTask(taskEntity: TaskEntity) {
        taskDao.addTask(taskEntity)
    }

    suspend fun deleteTask(taskEntity: TaskEntity) {
        taskDao.deleteTask(taskEntity)
    }

    suspend fun updateTask(taskEntity: TaskEntity) {
        taskDao.updateTask(taskEntity)
    }

    suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }

    fun searchDatabase(searchQuery: String): Flow<List<TaskEntity>> {
        return taskDao.searchDatabase(searchQuery)
    }

}