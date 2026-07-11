package com.herologs.domain.timeline

import com.herologs.core.model.MovementType
import com.herologs.core.model.TimelineEntry
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

/** Boundary between timeline use cases and its local persistence implementation. */
interface TimelineRepository {
    fun observeDay(date: LocalDate): Flow<List<TimelineEntry>>

    fun observeEntry(entryId: Long): Flow<TimelineEntry?>

    /** Renames the fallback title of an entry that is not represented by a place. */
    suspend fun renameEntryTitle(entryId: Long, title: String): Boolean

    /** Renames a place and therefore every timeline entry that references it. */
    suspend fun renamePlace(placeId: Long, name: String): Boolean

    /** Saves a user correction for a trip's movement type; null clears a prior value. */
    suspend fun correctMovementType(entryId: Long, movementType: MovementType?): Boolean
}
