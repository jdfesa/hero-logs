package com.herologs.feature.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.herologs.core.datastore.UserPreferencesRepository
import com.herologs.domain.permissions.PermissionAccessStatus
import com.herologs.domain.permissions.PermissionCapability
import com.herologs.domain.permissions.PermissionCapabilityState
import com.herologs.domain.permissions.PermissionOverview
import com.herologs.domain.permissions.PermissionStatusReader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsViewModelTest {
    private val repository = UserPreferencesRepository(FakeDataStore())

    @Test
    fun `initial state exposes current permission overview`() {
        val reader = FakePermissionStatusReader(
            overview(location = PermissionAccessStatus.LIMITED),
        )

        val viewModel = SettingsViewModel(repository, reader)

        assertEquals(
            PermissionAccessStatus.LIMITED,
            viewModel.uiState.value.permissionOverview[PermissionCapability.FOREGROUND_LOCATION].access,
        )
    }

    @Test
    fun `refresh replaces permission overview with current platform state`() {
        val reader = FakePermissionStatusReader(
            overview(location = PermissionAccessStatus.NOT_GRANTED),
        )
        val viewModel = SettingsViewModel(repository, reader)
        reader.current = overview(location = PermissionAccessStatus.GRANTED)

        viewModel.refreshPermissions()

        assertEquals(
            PermissionAccessStatus.GRANTED,
            viewModel.uiState.value.permissionOverview[PermissionCapability.FOREGROUND_LOCATION].access,
        )
        assertEquals(2, reader.readCount)
    }

    private fun overview(
        location: PermissionAccessStatus,
    ) = PermissionOverview(
        capabilities = listOf(
            PermissionCapabilityState(PermissionCapability.FOREGROUND_LOCATION, location),
            PermissionCapabilityState(
                PermissionCapability.ACTIVITY_RECOGNITION,
                PermissionAccessStatus.NOT_GRANTED,
            ),
            PermissionCapabilityState(
                PermissionCapability.HEALTH_CONNECT,
                PermissionAccessStatus.NOT_CONFIGURED,
            ),
        ),
    )

    private class FakePermissionStatusReader(
        var current: PermissionOverview,
    ) : PermissionStatusReader {
        var readCount = 0
            private set

        override fun read(): PermissionOverview {
            readCount += 1
            return current
        }
    }

    private class FakeDataStore : DataStore<Preferences> {
        private val state = MutableStateFlow<Preferences>(emptyPreferences())

        override val data: Flow<Preferences> = state

        override suspend fun updateData(
            transform: suspend (t: Preferences) -> Preferences,
        ): Preferences = transform(state.value).also { state.value = it }
    }
}
