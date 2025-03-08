package com.example.taskmanager.viewModel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _primaryColor = MutableStateFlow(Color.Cyan)
    val primaryColor: StateFlow<Color> = _primaryColor

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    private val _isDynamicThemingEnabled = MutableStateFlow(true)
    val isDynamicThemingEnabled: StateFlow<Boolean> = _isDynamicThemingEnabled

    init {
        viewModelScope.launch {
            val savedColor = preferencesManager.getPrimaryColor()
            _primaryColor.value = Color(savedColor)

            _isDarkMode.value = preferencesManager.getDarkMode()
            _isDynamicThemingEnabled.value = preferencesManager.getDynamicThemingEnabled()
        }
    }

    fun updatePrimaryColor(color: Color) {
        viewModelScope.launch {
            _primaryColor.value = color
            preferencesManager.savePrimaryColor(color.toArgb())
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            _isDarkMode.value = enabled
            preferencesManager.saveDarkMode(enabled)
        }
    }

    fun toggleDynamicTheming(enabled: Boolean) {
        viewModelScope.launch {
            _isDynamicThemingEnabled.value = enabled
            preferencesManager.saveDynamicThemingEnabled(enabled)
        }
    }

}