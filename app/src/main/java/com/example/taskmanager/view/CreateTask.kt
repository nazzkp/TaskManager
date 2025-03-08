package com.example.taskmanager.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.taskmanager.R
import com.example.taskmanager.model.dataClass.Task
import com.example.taskmanager.viewModel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTask(navController: NavHostController, viewModel: TaskViewModel = hiltViewModel()) {
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.Low) }
    var dueDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showPriorityDropdown by remember { mutableStateOf(false) }

    var showTaskTitleError by remember { mutableStateOf(false) }

    val tasks by viewModel.allTasks.collectAsState()
    val maxOrderIndex = tasks.maxOfOrNull { it.position } ?: 0

    fun validateTaskTitle(): Boolean {
        val isTitleValid = taskTitle.isNotEmpty()
        showTaskTitleError = !isTitleValid
        return isTitleValid
    }

    val taskTitleRef = remember { FocusRequester() }
    val taskDescriptionRef = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Task") },
                navigationIcon = {
                    IconButton(onClick = {
                        keyboardController?.hide()
                        navController.popBackStack()
                    },
                        modifier = Modifier.semantics { contentDescription = "Back" }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (validateTaskTitle()) {
                                keyboardController?.hide()
                                val newTask = Task(
                                    title = taskTitle,
                                    description = taskDescription,
                                    priority = priority,
                                    dueDate = dueDate,
                                    position = maxOrderIndex + 1
                                )
                                viewModel.insert(newTask)
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.semantics { contentDescription = "Save Task" }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_save_alt_24),
                            contentDescription = "Save Task"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = taskTitle,
                onValueChange = {
                    taskTitle = it
                    if (showTaskTitleError && it.isNotEmpty()) {
                        showTaskTitleError = false
                    }
                },
                label = { Text("Task Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Task Title" }
                    .focusRequester(taskTitleRef),
                isError = showTaskTitleError,
                keyboardActions = KeyboardActions(
                    onNext = {
                        taskDescriptionRef.requestFocus()
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                supportingText = {
                    if (showTaskTitleError) {
                        Text("Task Title is required", color = MaterialTheme.colorScheme.error)
                    }
                },
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .focusRequester(taskDescriptionRef)
                    .semantics { contentDescription = "Task Description" },
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = priority.name,
                    onValueChange = {},
                    label = { Text("Priority") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = "Task Priority" },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                keyboardController?.hide()
                                showPriorityDropdown = true },
                            modifier = Modifier.semantics {
                                contentDescription = "Choose Priorities"
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Priority Dropdown"
                            )
                        }
                    }
                )

                DropdownMenu(
                    expanded = showPriorityDropdown,
                    onDismissRequest = { showPriorityDropdown = false }
                ) {
                    DropdownMenuItem(
                        modifier = Modifier.semantics { contentDescription = Priority.Low.name },
                        text = { Text(Priority.Low.name) },
                        onClick = {
                            priority = Priority.Low
                            showPriorityDropdown = false
                        }
                    )

                    DropdownMenuItem(
                        modifier = Modifier.semantics { contentDescription = Priority.Medium.name },
                        text = { Text(Priority.Medium.name) },
                        onClick = {
                            priority = Priority.Medium
                            showPriorityDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        modifier = Modifier.semantics { contentDescription = Priority.High.name },
                        text = { Text(Priority.High.name) },
                        onClick = {
                            priority = Priority.High
                            showPriorityDropdown = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = dueDate,
                onValueChange = {},
                label = { Text("Due Date") },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Task Due Date" },
                readOnly = true,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            keyboardController?.hide()
                            showDatePicker = true },
                        modifier = Modifier.semantics { contentDescription = "Choose Date" }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Priority Dropdown"
                        )
                    }
                }
            )

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    onDateSelected = { date ->
                        dueDate = date
                        showDatePicker = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                val selectedDateMillis = datePickerState.selectedDateMillis
                if (selectedDateMillis != null) {
                    val date = Date(selectedDateMillis)
                    val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    val formattedDate = dateFormatter.format(date)
                    onDateSelected(formattedDate)
                }
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

enum class Priority {
    High, Medium, Low
}