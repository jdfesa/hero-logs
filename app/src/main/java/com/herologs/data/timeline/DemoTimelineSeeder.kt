package com.herologs.data.timeline

import androidx.room.withTransaction
import com.herologs.core.database.HeroLogsDatabase
import com.herologs.core.database.entity.PlaceEntity
import com.herologs.core.database.entity.TimelineEntryEntity
import com.herologs.core.model.DemoTimelineSeedResult
import com.herologs.core.model.MovementType
import com.herologs.core.model.PlaceCategory
import com.herologs.core.model.TimelineEntryType
import com.herologs.domain.timeline.TimelineDemoData
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

/**
 * Inserts one representative local day only when a caller explicitly invokes [seedFor].
 * It never runs as part of database creation or application startup.
 */
class DemoTimelineSeeder(
    private val database: HeroLogsDatabase,
    private val clock: Clock = Clock.systemDefaultZone(),
    private val zoneId: ZoneId = clock.zone,
) : TimelineDemoData {
    override suspend fun seedFor(date: LocalDate): DemoTimelineSeedResult =
        database.withTransaction {
            val timelineEntryDao = database.timelineEntryDao()
            if (timelineEntryDao.countForDay(date.toEpochDay()) > 0) {
                return@withTransaction DemoTimelineSeedResult.AlreadyHasEntries
            }

            val placeDao = database.placeDao()
            val nowEpochMillis = Instant.now(clock).toEpochMilli()
            val homeId = placeDao.insert(
                PlaceEntity(
                    name = "Casa",
                    category = PlaceCategory.HOME.name,
                    createdAtEpochMillis = nowEpochMillis,
                    updatedAtEpochMillis = nowEpochMillis,
                ),
            )
            val parkId = placeDao.insert(
                PlaceEntity(
                    name = "Parque",
                    category = PlaceCategory.LEISURE.name,
                    createdAtEpochMillis = nowEpochMillis,
                    updatedAtEpochMillis = nowEpochMillis,
                ),
            )
            val marketId = placeDao.insert(
                PlaceEntity(
                    name = "Mercado",
                    category = PlaceCategory.ERRAND.name,
                    createdAtEpochMillis = nowEpochMillis,
                    updatedAtEpochMillis = nowEpochMillis,
                ),
            )

            val entries = listOf(
                entry(
                    date = date,
                    start = LocalTime.of(0, 3),
                    end = LocalTime.of(8, 1),
                    type = TimelineEntryType.SLEEP,
                    title = "Dormiste",
                    subtitle = "7 h 58 min",
                    confidence = 0.92f,
                ),
                entry(
                    date = date,
                    start = LocalTime.of(8, 1),
                    end = LocalTime.of(12, 18),
                    type = TimelineEntryType.PLACE,
                    placeId = homeId,
                    title = "Casa",
                    subtitle = "Hogar",
                    confidence = 0.97f,
                ),
                entry(
                    date = date,
                    start = LocalTime.of(12, 18),
                    end = LocalTime.of(12, 27),
                    type = TimelineEntryType.TRIP,
                    title = "Caminata",
                    subtitle = "Traslado",
                    movementType = MovementType.WALKING,
                    confidence = 0.74f,
                ),
                entry(
                    date = date,
                    start = LocalTime.of(12, 27),
                    end = LocalTime.of(13, 6),
                    type = TimelineEntryType.PLACE,
                    placeId = parkId,
                    title = "Parque",
                    subtitle = "Tiempo personal",
                    confidence = 0.81f,
                ),
                entry(
                    date = date,
                    start = LocalTime.of(13, 6),
                    end = LocalTime.of(13, 49),
                    type = TimelineEntryType.PLACE,
                    placeId = marketId,
                    title = "Mercado",
                    subtitle = "Recado",
                    confidence = 0.69f,
                ),
                entry(
                    date = date,
                    start = LocalTime.of(13, 49),
                    end = LocalTime.of(18, 0),
                    type = TimelineEntryType.PLACE,
                    placeId = homeId,
                    title = "Casa",
                    subtitle = "Hogar",
                    confidence = 0.96f,
                ),
            )

            timelineEntryDao.insertAll(entries)
            DemoTimelineSeedResult.Seeded(entryCount = entries.size)
        }

    suspend fun seedToday(): DemoTimelineSeedResult = seedFor(LocalDate.now(clock))

    private fun entry(
        date: LocalDate,
        start: LocalTime,
        end: LocalTime,
        type: TimelineEntryType,
        title: String,
        subtitle: String?,
        confidence: Float,
        placeId: Long? = null,
        movementType: MovementType? = null,
    ): TimelineEntryEntity =
        TimelineEntryEntity(
            dayEpochDay = date.toEpochDay(),
            type = type.name,
            startedAtEpochMillis = date.atTime(start).atZone(zoneId).toInstant().toEpochMilli(),
            endedAtEpochMillis = date.atTime(end).atZone(zoneId).toInstant().toEpochMilli(),
            placeId = placeId,
            movementType = movementType?.name,
            title = title,
            subtitle = subtitle,
            confidence = confidence,
        )
}
