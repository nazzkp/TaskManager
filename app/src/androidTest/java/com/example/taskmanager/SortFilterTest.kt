package com.example.taskmanager

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.model.dataClass.Task
import com.example.taskmanager.view.HomeScreen
import com.example.taskmanager.view.Priority
import com.example.taskmanager.viewModel.TaskViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SortFilterTest {
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
    fun testSortingAndFiltering() {
        composeTestRule.waitForIdle()
        val filterNode = composeTestRule.onNodeWithContentDescription("Filter Tasks")
        val sortNode = composeTestRule.onNodeWithContentDescription("Sort Tasks")
        
        filterNode.performClick()
        
        composeTestRule.waitForIdle()
        
        composeTestRule.onNodeWithContentDescription("Filter All").performClick()
        
        filterNode.performClick()
        
        composeTestRule.onNodeWithContentDescription("Filter Completed").performClick()
        
        filterNode.performClick()
        
        composeTestRule.onNodeWithContentDescription("Filter Pending").performClick()
        
        sortNode.performClick()
        
        composeTestRule.onNodeWithContentDescription("Sort By Priority").performClick()
        
        sortNode.performClick()
        
        composeTestRule.onNodeWithContentDescription("Sort By Due Date").performClick()

        sortNode.performClick()
        
        composeTestRule.onNodeWithContentDescription("Sort By Title").performClick()


    }
}