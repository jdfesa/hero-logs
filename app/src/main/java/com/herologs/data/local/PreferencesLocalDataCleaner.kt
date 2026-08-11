package com.herologs.data.local

import com.herologs.core.datastore.UserPreferencesRepository

class PreferencesLocalDataCleaner(
    private val repository: UserPreferencesRepository,
) : LocalDataCleaner {
    override suspend fun clear() {
        repository.clearAll()
    }
}
