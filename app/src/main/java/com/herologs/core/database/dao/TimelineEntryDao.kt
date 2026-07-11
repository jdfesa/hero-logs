package com.herologs.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.herologs.core.database.entity.TimelineEntryEntity
import com.herologs.core.database.entity.TimelineEntryWithPlace
import kotlinx.coroutines.flow.Flow

@Dao
interface TimelineEntryDao {
    @Transaction
    @Query(
        """
        SELECT *
        FROM timeline_entries
        WHERE dayEpochDay = :dayEpochDay
        ORDER BY startedAtEpochMillis ASC, id ASC
        """,
    )
    fun observeForDay(dayEpochDay: Long): Flow<List<TimelineEntryWithPlace>>

    @Transaction
    @Query("SELECT * FROM timeline_entries WHERE id = :entryId LIMIT 1")
    fun observeById(entryId: Long): Flow<TimelineEntryWithPlace?>

    @Query("SELECT COUNT(*) FROM timeline_entries WHERE dayEpochDay = :dayEpochDay")
    suspend fun countForDay(dayEpochDay: Long): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(entries: List<TimelineEntryEntity>): List<Long>

    @Query(
        """
        UPDATE timeline_entries
        SET title = :title,
            userEdited = 1
        WHERE id = :entryId
        """,
    )
    suspend fun renameTitle(entryId: Long, title: String): Int

    @Query(
        """
        UPDATE timeline_entries
        SET movementType = :movementType,
            userEdited = 1
        WHERE id = :entryId
        """,
    )
    suspend fun updateMovementType(entryId: Long, movementType: String?): Int
}
