package com.herologs.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.herologs.core.datastore.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating.asStateFlow()

    fun showOnboardingAgain() {
        if (_isUpdating.value) return
        viewModelScope.launch {
            _isUpdating.value = true
            try {
                userPreferencesRepository.resetOnboarding()
            } finally {
                _isUpdating.value = false
            }
        }
    }

    companion object {
        fun factory(
            userPreferencesRepository: UserPreferencesRepository,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(userPreferencesRepository)
            }
        }
    }
}
