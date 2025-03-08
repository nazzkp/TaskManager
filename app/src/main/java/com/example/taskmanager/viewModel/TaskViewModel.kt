package com.example.taskmanager.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.model.DataRepository
import com.example.taskmanager.model.dataClass.Task
import com.example.taskmanager.view.FilterOption
import com.example.taskmanager.view.SortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: DataRepository,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    var allTasks = repository.getAllTasksOrdered()
        .onEach { _isLoading.value = false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insert(task: Task) = viewModelScope.launch {
        repository.insertTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
    }

    fun markTaskCompletion(task: Task, completionStatus: Boolean) = viewModelScope.launch {
        val updatedTask = task.copy(isCompleted = completionStatus)
        repository.updateTask(updatedTask)
    }

    private var sortOption by mutableStateOf(SortOption.PRIORITY)

    private var filterOption by mutableStateOf(FilterOption.ALL)

    fun updateSortOption(option: SortOption) {
        sortOption = option
    }

    fun updateFilterOption(option: FilterOption) {
        filterOption = option
    }

    fun getFilteredAndSortedTasks(tasks: List<Task>): List<Task> {
        val filteredTasks = when (filterOption) {
            FilterOption.ALL -> tasks
            FilterOption.COMPLETED -> tasks.filter { it.isCompleted }
            FilterOption.PENDING -> tasks.filter { !it.isCompleted }
        }

        return when (sortOption) {
            SortOption.PRIORITY -> filteredTasks.sortedBy { it.priority }
            SortOption.DUE_DATE -> filteredTasks.sortedBy { it.dueDate }
            SortOption.ALPHABETICAL -> filteredTasks.sortedBy { it.title }
        }
    }

}