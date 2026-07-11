package com.herologs.data.timeline

import com.herologs.core.database.dao.PlaceDao
import com.herologs.core.database.dao.TimelineEntryDao
import com.herologs.core.model.MovementType
import com.herologs.core.model.TimelineEntry
import com.herologs.domain.timeline.TimelineRepository
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomTimelineRepository(
    private val timelineEntryDao: TimelineEntryDao,
    private val placeDao: PlaceDao,
    private val clock: Clock = Clock.systemUTC(),
) : TimelineRepository {
    override fun observeDay(date: LocalDate): Flow<List<TimelineEntry>> =
        timelineEntryDao
            .observeForDay(date.toEpochDay())
            .map { entries -> entries.map { it.toDomain() } }

    override fun observeEntry(entryId: Long): Flow<TimelineEntry?> =
        timelineEntryDao
            .observeById(entryId)
            .map { entry -> entry?.toDomain() }

    override suspend fun renameEntryTitle(entryId: Long, title: String): Boolean {
        require(entryId > 0) { "Timeline entry id must be positive." }
        return timelineEntryDao.renameTitle(entryId, title.normalizedName()) > 0
    }

    override suspend fun renamePlace(placeId: Long, name: String): Boolean {
        require(placeId > 0) { "Place id must be positive." }
        return placeDao.rename(
            placeId = placeId,
            name = name.normalizedName(),
            updatedAtEpochMillis = Instant.now(clock).toEpochMilli(),
        ) > 0
    }

    override suspend fun correctMovementType(
        entryId: Long,
        movementType: MovementType?,
    ): Boolean {
        require(entryId > 0) { "Timeline entry id must be positive." }
        return timelineEntryDao.updateMovementType(
            entryId = entryId,
            movementType = movementType?.name,
        ) > 0
    }
}

private fun String.normalizedName(): String =
    trim().also { normalized ->
        require(normalized.isNotEmpty()) { "Names cannot be blank." }
    }
