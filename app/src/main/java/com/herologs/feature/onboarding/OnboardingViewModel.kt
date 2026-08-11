package com.herologs.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.herologs.core.datastore.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class OnboardingStep {
    VALUE_PROP,
    PRIVACY_PROMISE,
    FUTURE_SIGNALS,
    READY,
}

data class OnboardingUiState(
    val isLoading: Boolean = true,
    val hasCompletedOnboarding: Boolean = false,
    val currentStep: OnboardingStep = OnboardingStep.VALUE_PROP,
    val isSavingCompletion: Boolean = false,
) {
    val currentStepIndex: Int get() = currentStep.ordinal
    val totalSteps: Int get() = OnboardingStep.entries.size
    val canGoBack: Boolean get() = currentStepIndex > 0
    val canGoNext: Boolean get() = currentStepIndex < totalSteps - 1
}

class OnboardingViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val currentStepState = MutableStateFlow(OnboardingStep.VALUE_PROP)
    private val isSavingCompletionState = MutableStateFlow(false)

    val uiState: StateFlow<OnboardingUiState> = combine(
        userPreferencesRepository.preferences,
        currentStepState,
        isSavingCompletionState,
    ) { preferences, step, isSaving ->
        OnboardingUiState(
            isLoading = false,
            hasCompletedOnboarding = preferences.hasCompletedOnboarding,
            currentStep = step,
            isSavingCompletion = isSaving,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = OnboardingUiState(),
    )

    fun nextStep() {
        val currentIndex = currentStepState.value.ordinal
        val steps = OnboardingStep.entries
        if (currentIndex < steps.size - 1) {
            currentStepState.value = steps[currentIndex + 1]
        }
    }

    fun previousStep() {
        val currentIndex = currentStepState.value.ordinal
        if (currentIndex > 0) {
            val steps = OnboardingStep.entries
            currentStepState.value = steps[currentIndex - 1]
        }
    }

    fun completeOnboarding() {
        if (currentStepState.value != OnboardingStep.READY || isSavingCompletionState.value) {
            return
        }
        isSavingCompletionState.value = true
        viewModelScope.launch {
            try {
                userPreferencesRepository.completeOnboarding()
            } finally {
                isSavingCompletionState.value = false
            }
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
