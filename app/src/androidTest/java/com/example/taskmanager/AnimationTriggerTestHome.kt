package com.example.taskmanager

import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.model.dataClass.Task
import com.example.taskmanager.view.HomeScreen
import com.example.taskmanager.view.Priority
import com.example.taskmanager.view.TaskDetails
import com.example.taskmanager.viewModel.TaskViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AnimationTriggerTestHome {
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
            HomeScreen(navController = rememberNavController(), viewModel = taskViewModel)
        }
    }

    @Test
    fun testAnimationTriggeredFAB() {

        val fabNode = composeTestRule.onNodeWithContentDescription("Add Task")

        val beforeClick = fabNode.captureToImage()

        fabNode.performClick()

        composeTestRule.mainClock.advanceTimeBy(100) // First animation phase

        val afterClick = fabNode.captureToImage()

        fabNode.assertExists()

        assertNotEquals(beforeClick, afterClick)

    }

    @Test
    fun testAnimationTriggeredDetails() {

        val mockTask = Task(
            id = 1,
            title = "Test Task",
            description = "This is a test description",
            priority = Priority.High,
            dueDate = "2025-03-10",
            isCompleted = false,
            position = 1
        )

        composeTestRule.setContent {
            TaskDetails(
                task = mockTask,
                navController = rememberNavController(),
                viewModel = taskViewModel
            )
        }

        val animatedBox = composeTestRule.onNodeWithContentDescription("Task Details")

        val initialTime = animatedBox.captureToImage()

        composeTestRule.waitForIdle()

        val lastTime = animatedBox.captureToImage()

        animatedBox.assertExists()

        assertNotEquals(initialTime, lastTime)


    }


}