package com.herologs.data.timeline

import com.herologs.core.database.entity.PlaceEntity
import com.herologs.core.database.entity.TimelineEntryWithPlace
import com.herologs.core.model.MovementType
import com.herologs.core.model.Place
import com.herologs.core.model.PlaceCategory
import com.herologs.core.model.TimelineEntry
import com.herologs.core.model.TimelineEntryType
import java.time.Instant
import java.time.LocalDate

fun TimelineEntryWithPlace.toDomain(): TimelineEntry =
    TimelineEntry(
        id = entry.id,
        date = LocalDate.ofEpochDay(entry.dayEpochDay),
        type = entry.type.toTimelineEntryType(),
        startedAt = Instant.ofEpochMilli(entry.startedAtEpochMillis),
        endedAt = Instant.ofEpochMilli(entry.endedAtEpochMillis),
        title = entry.title,
        subtitle = entry.subtitle,
        place = place?.toDomain(),
        movementType = entry.movementType?.toMovementType(),
        confidence = entry.confidence.coerceIn(0f, 1f),
        wasUserEdited = entry.userEdited,
    )

fun PlaceEntity.toDomain(): Place =
    Place(
        id = id,
        name = name,
        category = category.toPlaceCategory(),
    )

private fun String.toTimelineEntryType(): TimelineEntryType =
    TimelineEntryType.entries.firstOrNull { it.name == this } ?: TimelineEntryType.UNKNOWN

private fun String.toMovementType(): MovementType =
    MovementType.entries.firstOrNull { it.name == this } ?: MovementType.UNKNOWN

private fun String.toPlaceCategory(): PlaceCategory =
    PlaceCategory.entries.firstOrNull { it.name == this } ?: PlaceCategory.OTHER
