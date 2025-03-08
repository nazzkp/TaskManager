package com.example.taskmanager.model

import com.example.taskmanager.model.dataClass.Task
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    fun getAllTasksOrdered(): Flow<List<Task>>
    suspend fun insertTask(task : Task)
    suspend fun deleteTask(task: Task)
    suspend fun updateTask(updatedTask: Task)
    suspend fun updatePosition(id: Int, position: Int)
}