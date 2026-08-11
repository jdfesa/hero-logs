package com.herologs.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.herologs.data.local.PreferencesLocalDataCleaner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UserPreferencesRepositoryTest {
    @Test
    fun `clear all removes onboarding completion`() = runTest {
        val dataStore = FakeDataStore()
        val repository = UserPreferencesRepository(dataStore)
        repository.completeOnboarding()
        assertTrue(repository.preferences.first().hasCompletedOnboarding)

        PreferencesLocalDataCleaner(repository).clear()

        assertFalse(repository.preferences.first().hasCompletedOnboarding)
        assertTrue(dataStore.current.asMap().isEmpty())
    }

    @Test
    fun `clear all is safe to retry`() = runTest {
        val dataStore = FakeDataStore()
        val repository = UserPreferencesRepository(dataStore)

        repository.clearAll()
        repository.clearAll()

        assertFalse(repository.preferences.first().hasCompletedOnboarding)
        assertTrue(dataStore.current.asMap().isEmpty())
    }

    private class FakeDataStore : DataStore<Preferences> {
        private val state = MutableStateFlow<Preferences>(emptyPreferences())
        val current: Preferences
            get() = state.value

        override val data: Flow<Preferences> = state

        override suspend fun updateData(
            transform: suspend (t: Preferences) -> Preferences,
        ): Preferences = transform(state.value).also { state.value = it }
    }
}
