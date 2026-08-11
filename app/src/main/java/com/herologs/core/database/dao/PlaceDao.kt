package com.herologs.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.herologs.core.database.entity.PlaceEntity

@Dao
interface PlaceDao {
    @Query("SELECT COUNT(*) FROM places")
    suspend fun countAll(): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(place: PlaceEntity): Long

    @Query("DELETE FROM places")
    suspend fun deleteAll(): Int

    @Query(
        """
        UPDATE places
        SET name = :name,
            updatedAtEpochMillis = :updatedAtEpochMillis
        WHERE id = :placeId
        """,
    )
    suspend fun rename(
        placeId: Long,
        name: String,
        updatedAtEpochMillis: Long,
    ): Int
}
