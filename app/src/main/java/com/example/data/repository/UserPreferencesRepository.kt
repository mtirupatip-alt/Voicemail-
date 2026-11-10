package com.example.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "receptionist_preferences")

class UserPreferencesRepository(private val context: Context) {
    companion object {
        val KEY_SERVICE_ACTIVE = booleanPreferencesKey("service_active")
        val KEY_NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val KEY_THEME_MODE = stringPreferencesKey("theme_mode") // "SYSTEM", "DARK", "LIGHT"
        val KEY_AUTO_LOG_VIP = booleanPreferencesKey("auto_log_vip")
    }

    val isServiceActive: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_SERVICE_ACTIVE] ?: true
    }

    val areNotificationsEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_NOTIFICATIONS_ENABLED] ?: true
    }

    val themeMode: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_THEME_MODE] ?: "DARK"
    }

    suspend fun setServiceActive(active: Boolean) {
        context.dataStore.edit { it[KEY_SERVICE_ACTIVE] = active }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[KEY_NOTIFICATIONS_ENABLED] = enabled }
    }

    suspend fun setThemeMode(theme: String) {
        context.dataStore.edit { it[KEY_THEME_MODE] = theme }
    }
}
