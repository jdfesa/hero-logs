package com.herologs.data.local

import com.herologs.domain.localdata.LocalDataCategory
import com.herologs.domain.localdata.LocalDataDeletionResult
import com.herologs.domain.localdata.LocalDataDeletionStage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class DefaultLocalDataRepositoryTest {
    @Test
    fun `inventory lists every app-controlled category`() {
        val repository = repository()

        assertEquals(
            listOf(
                LocalDataCategory.TIMELINE_ENTRIES,
                LocalDataCategory.PLACES,
                LocalDataCategory.APP_PREFERENCES,
            ),
            repository.getStoredCategories(),
        )
    }

    @Test
    fun `delete all clears database before preferences`() = runTest {
        val calls = mutableListOf<String>()
        val repository = repository(
            databaseCleaner = recordingCleaner("database", calls),
            preferencesCleaner = recordingCleaner("preferences", calls),
        )

        val result = repository.deleteAll()

        assertEquals(LocalDataDeletionResult.Success, result)
        assertEquals(listOf("database", "preferences"), calls)
    }

    @Test
    fun `database failure stops before preferences`() = runTest {
        val calls = mutableListOf<String>()
        val repository = repository(
            databaseCleaner = recordingCleaner(
                name = "database",
                calls = calls,
                failure = IllegalStateException("database unavailable"),
            ),
            preferencesCleaner = recordingCleaner("preferences", calls),
        )

        val result = repository.deleteAll()

        assertEquals(
            LocalDataDeletionResult.Failure(
                failedStage = LocalDataDeletionStage.DATABASE,
            ),
            result,
        )
        assertEquals(listOf("database"), calls)
    }

    @Test
    fun `preferences failure reports cleared database categories`() = runTest {
        val calls = mutableListOf<String>()
        val repository = repository(
            databaseCleaner = recordingCleaner("database", calls),
            preferencesCleaner = recordingCleaner(
                name = "preferences",
                calls = calls,
                failure = IllegalStateException("preferences unavailable"),
            ),
        )

        val result = repository.deleteAll()

        assertEquals(
            LocalDataDeletionResult.Failure(
                failedStage = LocalDataDeletionStage.PREFERENCES,
                clearedCategories = setOf(
                    LocalDataCategory.TIMELINE_ENTRIES,
                    LocalDataCategory.PLACES,
                ),
            ),
            result,
        )
        assertEquals(listOf("database", "preferences"), calls)
    }

    @Test
    fun `retry after preferences failure clears every category`() = runTest {
        val calls = mutableListOf<String>()
        var preferenceAttempts = 0
        val repository = repository(
            databaseCleaner = recordingCleaner("database", calls),
            preferencesCleaner = LocalDataCleaner {
                calls += "preferences"
                preferenceAttempts += 1
                if (preferenceAttempts == 1) {
                    throw IllegalStateException("preferences unavailable")
                }
            },
        )

        val firstResult = repository.deleteAll()
        val retryResult = repository.deleteAll()

        assertEquals(
            LocalDataDeletionResult.Failure(
                failedStage = LocalDataDeletionStage.PREFERENCES,
                clearedCategories = setOf(
                    LocalDataCategory.TIMELINE_ENTRIES,
                    LocalDataCategory.PLACES,
                ),
            ),
            firstResult,
        )
        assertEquals(LocalDataDeletionResult.Success, retryResult)
        assertEquals(
            listOf("database", "preferences", "database", "preferences"),
            calls,
        )
    }

    @Test
    fun `database cancellation is propagated`() = runTest {
        val repository = repository(
            databaseCleaner = recordingCleaner(
                failure = CancellationException("cancel database"),
            ),
        )

        assertCancellation { repository.deleteAll() }
    }

    @Test
    fun `preferences cancellation is propagated after database clearing`() = runTest {
        val calls = mutableListOf<String>()
        val repository = repository(
            databaseCleaner = recordingCleaner("database", calls),
            preferencesCleaner = recordingCleaner(
                name = "preferences",
                calls = calls,
                failure = CancellationException("cancel preferences"),
            ),
        )

        assertCancellation { repository.deleteAll() }
        assertEquals(listOf("database", "preferences"), calls)
    }

    private fun repository(
        databaseCleaner: LocalDataCleaner = recordingCleaner(),
        preferencesCleaner: LocalDataCleaner = recordingCleaner(),
    ) = DefaultLocalDataRepository(
        databaseCleaner = databaseCleaner,
        preferencesCleaner = preferencesCleaner,
    )

    private fun recordingCleaner(
        name: String = "cleaner",
        calls: MutableList<String> = mutableListOf(),
        failure: Exception? = null,
    ) = LocalDataCleaner {
        calls += name
        failure?.let { throw it }
    }

    private suspend fun assertCancellation(block: suspend () -> Unit) {
        try {
            block()
            fail("Expected CancellationException")
        } catch (exception: CancellationException) {
            assertTrue(exception.message?.startsWith("cancel") == true)
        }
    }
}
