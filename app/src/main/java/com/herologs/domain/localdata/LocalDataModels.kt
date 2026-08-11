package com.herologs.domain.localdata

/** Categories of user-controlled data currently persisted by HeroLogs. */
enum class LocalDataCategory {
    TIMELINE_ENTRIES,
    PLACES,
    APP_PREFERENCES,
}

/** Persistence boundary at which a delete-all operation can fail. */
enum class LocalDataDeletionStage {
    DATABASE,
    PREFERENCES,
}

sealed interface LocalDataDeletionResult {
    data object Success : LocalDataDeletionResult

    /**
     * [clearedCategories] keeps partial completion visible when independent
     * persistence systems cannot participate in one atomic transaction.
     */
    data class Failure(
        val failedStage: LocalDataDeletionStage,
        val clearedCategories: Set<LocalDataCategory> = emptySet(),
    ) : LocalDataDeletionResult
}
