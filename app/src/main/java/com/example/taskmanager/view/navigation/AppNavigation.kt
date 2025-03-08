package com.example.taskmanager.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.model.dataClass.Task
import com.example.taskmanager.view.CreateTask
import com.example.taskmanager.view.HomeScreen
import com.example.taskmanager.view.SettingsScreen
import com.example.taskmanager.view.TaskDetails
import com.example.taskmanager.viewModel.SettingsViewModel

@Composable
fun AppNavigation(settingsViewModel: SettingsViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home",) {
        composable("home") { HomeScreen(navController) }
        composable("taskCreation") { CreateTask(navController) }
        composable("taskDetails") {
            val task = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<Task>("task")
            if (task != null) {
                TaskDetails(task,navController) }
            }
        composable("settingsScreen") { SettingsScreen(navController,settingsViewModel)  }
    }
}