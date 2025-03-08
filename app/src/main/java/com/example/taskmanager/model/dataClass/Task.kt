package com.example.taskmanager.model.dataClass

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskmanager.view.Priority
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Priority,
    val dueDate: String,
    val isCompleted: Boolean = false,
    val position: Int = 0
) : Parcelable
