package com.example.taskmanager

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.model.dataClass.Task
import com.example.taskmanager.view.CreateTask
import com.example.taskmanager.view.Priority
import com.example.taskmanager.viewModel.TaskViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class TaskCreationTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val taskViewModel = mockk<TaskViewModel>(relaxed = true)


    @Before
    fun setup() {

        val mockTasks: StateFlow<List<Task>> = MutableStateFlow(
            listOf(Task(1, "Description", "Priority.Low", Priority.Low, "1", false, 1))
        )
        every { taskViewModel.allTasks } returns mockTasks

        composeTestRule.setContent {
            CreateTask(navController = rememberNavController(), viewModel = taskViewModel)
        }
    }

    @Test
    fun testTaskCreation() {

        composeTestRule.onNodeWithText("Task Title").performTextInput("Test Task")

        composeTestRule.onNodeWithText("Description").performTextInput("Test description for the task.")

        composeTestRule.onNodeWithContentDescription("Choose Priorities").performClick()

        composeTestRule.onNodeWithText(Priority.Medium.name).performClick()

        composeTestRule.onNodeWithContentDescription("Choose Date").performClick()

        composeTestRule.onNodeWithText("OK").performClick()

        composeTestRule.onNodeWithContentDescription("Save Task").performClick()

    }
}