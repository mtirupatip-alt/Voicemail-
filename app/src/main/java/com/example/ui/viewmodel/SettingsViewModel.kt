package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.CallRepository
import com.example.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val prefRepo: UserPreferencesRepository,
    private val callRepo: CallRepository
) : ViewModel() {

    val isServiceActive: StateFlow<Boolean> = prefRepo.isServiceActive
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val areNotificationsEnabled: StateFlow<Boolean> = prefRepo.areNotificationsEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val themeMode: StateFlow<String> = prefRepo.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "DARK")

    fun toggleService(active: Boolean) {
        viewModelScope.launch { prefRepo.setServiceActive(active) }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch { prefRepo.setNotificationsEnabled(enabled) }
    }

    fun setTheme(theme: String) {
        viewModelScope.launch { prefRepo.setThemeMode(theme) }
    }

    fun wipeLocalCallData() {
        viewModelScope.launch { callRepo.clearHistory() }
    }
}
