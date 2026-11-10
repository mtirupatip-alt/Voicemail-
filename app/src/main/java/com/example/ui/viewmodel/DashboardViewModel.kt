package com.example.ui.viewmodel

import android.app.role.RoleManager
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.CallRecordEntity
import com.example.data.repository.CallRepository
import com.example.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.*

data class DashboardUiState(
    val isServiceActive: Boolean = true,
    val isCallScreeningRoleHeld: Boolean = false,
    val totalDetectedCalls: Int = 0,
    val todayDetectedCalls: Int = 0,
    val recentCalls: List<CallRecordEntity> = emptyList()
)

class DashboardViewModel(
    private val callRepo: CallRepository,
    prefRepo: UserPreferencesRepository
) : ViewModel() {

    private val _roleHeld = MutableStateFlow(false)

    val uiState: StateFlow<DashboardUiState> = combine(
        prefRepo.isServiceActive,
        _roleHeld,
        callRepo.getTotalCount(),
        callRepo.getTodayCount(),
        callRepo.getRecentCalls()
    ) { isActive, roleHeld, total, today, recents ->
        DashboardUiState(
            isServiceActive = isActive,
            isCallScreeningRoleHeld = roleHeld,
            totalDetectedCalls = total,
            todayDetectedCalls = today,
            recentCalls = recents
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardUiState())

    fun refreshRoleStatus(context: Context) {
        val roleManager = context.getSystemService(Context.ROLE_SERVICE) as? RoleManager
        _roleHeld.value = roleManager?.isRoleHeld(RoleManager.ROLE_CALL_SCREENING) == true
    }
}
