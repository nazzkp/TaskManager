package com.example.taskmanager.dB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskmanager.data.local.RoomDao
import com.example.taskmanager.model.dataClass.Task


@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskManagerDB : RoomDatabase() {
    abstract fun myDao(): RoomDao
}