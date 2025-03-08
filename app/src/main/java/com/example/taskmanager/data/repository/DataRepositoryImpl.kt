package com.example.taskmanager.data.repository

import android.app.Application
import com.example.taskmanager.data.local.RoomDao
import com.example.taskmanager.model.DataRepository
import com.example.taskmanager.model.dataClass.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val dao: RoomDao,
): DataRepository {

    override fun getAllTasksOrdered(): Flow<List<Task>> {
        return dao.getAllTasksOrdered()
    }

    override suspend fun insertTask(task: Task) {
        dao.insert(task)
    }

    override suspend fun deleteTask(task: Task) {
        dao.deleteTask(task)
    }

    override suspend fun updateTask(updatedTask: Task) {
        dao.updateTask(updatedTask)
    }

    override suspend fun updatePosition(id: Int, position: Int) {
        dao.updatePosition(id, position)
    }


}