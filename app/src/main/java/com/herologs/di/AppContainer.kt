package com.herologs.di

import android.content.Context
import androidx.room.Room
import com.herologs.core.database.HeroLogsDatabase
import com.herologs.core.datastore.UserPreferencesRepository
import com.herologs.core.datastore.userPreferencesDataStore
import com.herologs.data.timeline.DemoTimelineSeeder
import com.herologs.data.timeline.RoomTimelineRepository
import com.herologs.domain.timeline.SeedDemoTimelineUseCase
import com.herologs.domain.timeline.TimelineDemoData
import com.herologs.domain.timeline.TimelineRepository

/**
 * Small manual dependency container for the single-module MVP.
 *
 * The contracts are kept at the domain boundary so this can be replaced by a
 * DI framework only when the app has enough dependencies to justify it.
 */
class AppContainer(context: Context) {
    private val database: HeroLogsDatabase = Room.databaseBuilder(
        context,
        HeroLogsDatabase::class.java,
        DATABASE_NAME,
    ).build()

    val timelineRepository: TimelineRepository = RoomTimelineRepository(
        timelineEntryDao = database.timelineEntryDao(),
        placeDao = database.placeDao(),
    )

    private val timelineDemoData: TimelineDemoData = DemoTimelineSeeder(database)

    val seedDemoTimeline = SeedDemoTimelineUseCase(timelineDemoData)

    val userPreferencesRepository = UserPreferencesRepository(
        dataStore = context.userPreferencesDataStore,
    )

    private companion object {
        const val DATABASE_NAME = "hero_logs.db"
    }
}
