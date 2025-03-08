package com.example.taskmanager.utils

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    fun savePrimaryColor(color: Int) {
        sharedPreferences.edit {
            putInt("primary_color", color)
        }
    }

    fun getPrimaryColor(): Int {
        return sharedPreferences.getInt("primary_color", Color.Cyan.toArgb())
    }

    fun saveDarkMode(enabled: Boolean) {
        sharedPreferences.edit { putBoolean("dark_mode", enabled) }
    }

    fun getDarkMode(): Boolean {
        return sharedPreferences.getBoolean("dark_mode", false)
    }

    fun saveDynamicThemingEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean("dynamic_theming", enabled) }
    }

    fun getDynamicThemingEnabled(): Boolean {
        return sharedPreferences.getBoolean("dynamic_theming", true)
    }

}