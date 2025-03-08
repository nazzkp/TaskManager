package com.example.taskmanager.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.ui.theme.TaskManagerTheme
import com.example.taskmanager.view.navigation.AppNavigation
import com.example.taskmanager.viewModel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val primaryColor by settingsViewModel.primaryColor.collectAsState()
            val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
            val isDynamicThemingEnabled by settingsViewModel.isDynamicThemingEnabled.collectAsState()

            TaskManagerTheme(
                darkTheme = isDarkMode,
                dynamicColor = isDynamicThemingEnabled,
                primaryColor = primaryColor
            ) {
                AppNavigation(settingsViewModel)
            }
        }
    }
}