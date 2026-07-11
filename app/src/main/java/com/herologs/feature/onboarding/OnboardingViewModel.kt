package com.herologs.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.herologs.core.datastore.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val isLoading: Boolean = true,
    val hasCompletedOnboarding: Boolean = false,
)

class OnboardingViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    val uiState: StateFlow<OnboardingUiState> = userPreferencesRepository.preferences
        .map { preferences ->
            OnboardingUiState(
                isLoading = false,
                hasCompletedOnboarding = preferences.hasCompletedOnboarding,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = OnboardingUiState(),
        )

    fun completeOnboarding() {
        viewModelScope.launch {
            userPreferencesRepository.completeOnboarding()
        }
    }

    companion object {
        fun factory(
            userPreferencesRepository: UserPreferencesRepository,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                OnboardingViewModel(userPreferencesRepository)
            }
        }

        private const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
