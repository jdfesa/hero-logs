package com.herologs.feature.onboarding

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesOf
import com.herologs.core.datastore.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val fakeDataStore = FakeDataStore()
    private val repository = UserPreferencesRepository(fakeDataStore)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state exposes first step and correct step indices`() = runTest {
        val viewModel = OnboardingViewModel(repository)
        val state = viewModel.uiState.first { !it.isLoading }

        assertEquals(OnboardingStep.VALUE_PROP, state.currentStep)
        assertEquals(0, state.currentStepIndex)
        assertEquals(4, state.totalSteps)
        assertFalse(state.canGoBack)
        assertTrue(state.canGoNext)
    }

    @Test
    fun `nextStep advances through all steps up to READY`() = runTest {
        val viewModel = OnboardingViewModel(repository)

        viewModel.nextStep()
        var state = viewModel.uiState.first { !it.isLoading }
        assertEquals(OnboardingStep.PRIVACY_PROMISE, state.currentStep)
        assertTrue(state.canGoBack)
        assertTrue(state.canGoNext)

        viewModel.nextStep()
        state = viewModel.uiState.first { !it.isLoading }
        assertEquals(OnboardingStep.FUTURE_SIGNALS, state.currentStep)

        viewModel.nextStep()
        state = viewModel.uiState.first { !it.isLoading }
        assertEquals(OnboardingStep.READY, state.currentStep)
        assertTrue(state.canGoBack)
        assertFalse(state.canGoNext)
    }

    @Test
    fun `nextStep is bounded at READY`() = runTest {
        val viewModel = OnboardingViewModel(repository)

        repeat(10) { viewModel.nextStep() }

        val state = viewModel.uiState.first { !it.isLoading }
        assertEquals(OnboardingStep.READY, state.currentStep)
        assertEquals(3, state.currentStepIndex)
    }

    @Test
    fun `previousStep navigates backwards and is bounded at first step`() = runTest {
        val viewModel = OnboardingViewModel(repository)

        viewModel.nextStep()
        viewModel.nextStep()
        assertEquals(OnboardingStep.FUTURE_SIGNALS, viewModel.uiState.first { !it.isLoading }.currentStep)

        viewModel.previousStep()
        assertEquals(OnboardingStep.PRIVACY_PROMISE, viewModel.uiState.first { !it.isLoading }.currentStep)

        viewModel.previousStep()
        assertEquals(OnboardingStep.VALUE_PROP, viewModel.uiState.first { !it.isLoading }.currentStep)

        viewModel.previousStep()
        assertEquals(OnboardingStep.VALUE_PROP, viewModel.uiState.first { !it.isLoading }.currentStep)
        assertFalse(viewModel.uiState.first { !it.isLoading }.canGoBack)
    }

    @Test
    fun `completeOnboarding is a no-op before READY step`() = runTest {
        val viewModel = OnboardingViewModel(repository)

        viewModel.completeOnboarding()

        assertFalse(fakeDataStore.hasCompletedOnboarding)
    }

    @Test
    fun `completeOnboarding on READY step persists completion`() = runTest {
        val viewModel = OnboardingViewModel(repository)

        viewModel.nextStep()
        viewModel.nextStep()
        viewModel.nextStep()
        assertEquals(OnboardingStep.READY, viewModel.uiState.first { !it.isLoading }.currentStep)

        viewModel.completeOnboarding()

        assertTrue(fakeDataStore.hasCompletedOnboarding)
    }

    private class FakeDataStore : DataStore<Preferences> {
        private val state = MutableStateFlow<Preferences>(emptyPreferences())
        var hasCompletedOnboarding = false
            private set

        override val data: Flow<Preferences> = state

        override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
            val current = state.value
            val updated = transform(current)
            val completedKey = booleanPreferencesKey("has_completed_onboarding")
            if (updated[completedKey] == true) {
                hasCompletedOnboarding = true
            }
            state.value = updated
            return updated
        }
    }
}
