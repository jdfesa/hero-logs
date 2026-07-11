package com.herologs.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private const val USER_PREFERENCES_FILE = "hero_logs_preferences"

val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_FILE,
)

data class UserPreferences(
    val hasCompletedOnboarding: Boolean = false,
)

/**
 * Owns small, non-sensitive application preferences.
 *
 * Timeline and health data belong in Room; this repository intentionally stores
 * only UI and consent-flow state.
 */
class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>,
) {
    val preferences: Flow<UserPreferences> = dataStore.data
        .catch { throwable ->
            if (throwable is IOException) {
                emit(androidx.datastore.preferences.core.emptyPreferences())
            } else {
                throw throwable
            }
        }
        .map { values ->
            UserPreferences(
                hasCompletedOnboarding = values[HAS_COMPLETED_ONBOARDING] ?: false,
            )
        }

    suspend fun completeOnboarding() {
        dataStore.edit { preferences ->
            preferences[HAS_COMPLETED_ONBOARDING] = true
        }
    }

    suspend fun resetOnboarding() {
        dataStore.edit { preferences ->
            preferences[HAS_COMPLETED_ONBOARDING] = false
        }
    }

    private companion object {
        val HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
    }
}
