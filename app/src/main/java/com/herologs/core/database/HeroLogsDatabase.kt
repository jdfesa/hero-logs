package com.herologs.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.herologs.core.database.dao.PlaceDao
import com.herologs.core.database.dao.TimelineEntryDao
import com.herologs.core.database.entity.PlaceEntity
import com.herologs.core.database.entity.TimelineEntryEntity

@Database(
    entities = [
        PlaceEntity::class,
        TimelineEntryEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class HeroLogsDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao

    abstract fun timelineEntryDao(): TimelineEntryDao
}
