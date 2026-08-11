package com.herologs.data.local

import androidx.room.withTransaction
import com.herologs.core.database.HeroLogsDatabase

class RoomLocalDataCleaner(
    private val database: HeroLogsDatabase,
) : LocalDataCleaner {
    override suspend fun clear() {
        database.withTransaction {
            database.timelineEntryDao().deleteAll()
            database.placeDao().deleteAll()
        }
    }
}
