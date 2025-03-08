package com.example.taskmanager.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taskmanager.viewModel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, settingsViewModel: SettingsViewModel) {
    val availableColors = listOf(
        Color.Blue, Color.Red, Color.Green, Color.Magenta, Color.Cyan
    )
    val primaryColor by settingsViewModel.primaryColor.collectAsState()
    val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
    val isDynamicThemingEnabled by settingsViewModel.isDynamicThemingEnabled.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() },
                        modifier = Modifier.semantics { contentDescription = "Back" }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
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
            Text(
                text = "Select Primary Color",
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                availableColors.forEach { color ->
                    ColorCircle(
                        color = color,
                        isSelected = color == primaryColor,
                        onColorSelected = {
                            settingsViewModel.updatePrimaryColor(color)
                        })
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Dark Mode")
                Switch(
                    modifier = Modifier.semantics { contentDescription = "Dark Mode Switch" },
                    checked = isDarkMode,
                    onCheckedChange = { settingsViewModel.toggleDarkMode(it) }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Dynamic Theming")
                Switch(
                    modifier = Modifier.semantics { contentDescription = "Dynamic Theming Switch" },
                    checked = isDynamicThemingEnabled,
                    onCheckedChange = { settingsViewModel.toggleDynamicTheming(it) }
                )
            }
        }
    }
}

@Composable
fun ColorCircle(color: Color, onColorSelected: (Color) -> Unit, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clickable { onColorSelected(color) },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(MaterialTheme.colorScheme.onSurface, CircleShape)
            )
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color, CircleShape)
        )
    }
}