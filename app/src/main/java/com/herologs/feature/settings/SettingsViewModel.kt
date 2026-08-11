package com.herologs.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.herologs.core.datastore.UserPreferencesRepository
import com.herologs.domain.permissions.PermissionOverview
import com.herologs.domain.permissions.PermissionStatusReader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val permissionOverview: PermissionOverview,
    val isUpdatingOnboarding: Boolean = false,
)

class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val permissionStatusReader: PermissionStatusReader,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        SettingsUiState(permissionOverview = permissionStatusReader.read()),
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun refreshPermissions() {
        _uiState.update { current ->
            current.copy(permissionOverview = permissionStatusReader.read())
        }
    }

    fun showOnboardingAgain() {
        if (_uiState.value.isUpdatingOnboarding) return
        viewModelScope.launch {
            _uiState.update { it.copy(isUpdatingOnboarding = true) }
            try {
                userPreferencesRepository.resetOnboarding()
            } finally {
                _uiState.update { it.copy(isUpdatingOnboarding = false) }
            }
        }
    }

    companion object {
        fun factory(
            userPreferencesRepository: UserPreferencesRepository,
            permissionStatusReader: PermissionStatusReader,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    userPreferencesRepository = userPreferencesRepository,
                    permissionStatusReader = permissionStatusReader,
                )
            }
        }
    }
}
