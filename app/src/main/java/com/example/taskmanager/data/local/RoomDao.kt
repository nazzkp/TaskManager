package com.example.taskmanager.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskmanager.model.dataClass.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    @Insert
    suspend fun insert(task: Task)

    @Query("SELECT * FROM tasks ORDER BY position ASC")
    fun getAllTasksOrdered(): Flow<List<Task>>

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("UPDATE tasks SET position = :position WHERE id = :id")
    suspend fun updatePosition(id: Int, position: Int)

}