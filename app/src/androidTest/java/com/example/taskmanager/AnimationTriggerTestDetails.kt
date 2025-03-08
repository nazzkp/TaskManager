package com.example.taskmanager


import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.model.dataClass.Task
import com.example.taskmanager.view.Priority
import com.example.taskmanager.view.TaskDetails
import com.example.taskmanager.viewModel.TaskViewModel
import io.mockk.mockk
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AnimationTriggerTestDetails {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val taskViewModel = mockk<TaskViewModel>(relaxed = true)


    @Before
    fun setup() {

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
    }

    @Test
    fun testAnimationTriggeredDetails() {

        val animatedBox = composeTestRule.onNodeWithContentDescription("Task Details")

        val initialTime = animatedBox.captureToImage()

        composeTestRule.waitForIdle()

        val lastTime = animatedBox.captureToImage()

        animatedBox.assertExists()

        assertNotEquals(initialTime, lastTime)

    }
}