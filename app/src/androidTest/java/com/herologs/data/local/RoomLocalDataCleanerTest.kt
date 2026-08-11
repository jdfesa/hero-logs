package com.herologs.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.herologs.core.database.HeroLogsDatabase
import com.herologs.core.database.entity.PlaceEntity
import com.herologs.core.database.entity.TimelineEntryEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomLocalDataCleanerTest {
    private lateinit var database: HeroLogsDatabase
    private lateinit var cleaner: RoomLocalDataCleaner

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, HeroLogsDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        cleaner = RoomLocalDataCleaner(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun clearRemovesTimelineEntriesAndPlaces() = runTest {
        insertLinkedTimelineEntry()

        cleaner.clear()

        assertEquals(0, database.timelineEntryDao().countAll())
        assertEquals(0, database.placeDao().countAll())
    }

    @Test
    fun clearRollsBackWhenADeleteFails() = runTest {
        insertLinkedTimelineEntry()
        database.openHelper.writableDatabase.execSQL(
            """
            CREATE TRIGGER prevent_place_delete
            BEFORE DELETE ON places
            BEGIN
                SELECT RAISE(ABORT, 'place delete blocked');
            END
            """.trimIndent(),
        )

        val failure = try {
            cleaner.clear()
            null
        } catch (exception: Exception) {
            exception
        }

        assertNotNull(failure)
        assertEquals(1, database.timelineEntryDao().countAll())
        assertEquals(1, database.placeDao().countAll())
    }

    private suspend fun insertLinkedTimelineEntry() {
        val placeId = database.placeDao().insert(
            PlaceEntity(
                name = "Home",
                category = "HOME",
                createdAtEpochMillis = 1_000L,
                updatedAtEpochMillis = 1_000L,
            ),
        )
        database.timelineEntryDao().insertAll(
            listOf(
                TimelineEntryEntity(
                    dayEpochDay = 1L,
                    type = "STAY",
                    startedAtEpochMillis = 1_000L,
                    endedAtEpochMillis = 2_000L,
                    placeId = placeId,
                    movementType = null,
                    title = "Home",
                    subtitle = null,
                    confidence = 1f,
                ),
            ),
        )
    }
}
