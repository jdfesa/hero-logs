package com.herologs.core.model

import java.time.Duration
import java.time.Instant
import java.time.LocalDate

/** A user-visible place, kept deliberately free of raw location coordinates. */
data class Place(
    val id: Long,
    val name: String,
    val category: PlaceCategory,
) {
    init {
        require(id > 0) { "Place id must be positive." }
        require(name.isNotBlank()) { "Place name cannot be blank." }
    }
}

enum class PlaceCategory {
    HOME,
    WORK,
    EXERCISE,
    LEISURE,
    SOCIAL,
    ERRAND,
    OTHER,
}

enum class TimelineEntryType {
    PLACE,
    TRIP,
    SLEEP,
    UNKNOWN,
}

enum class MovementType {
    WALKING,
    RUNNING,
    CYCLING,
    IN_VEHICLE,
    TRANSIT,
    STILL,
    UNKNOWN,
}

sealed interface DemoTimelineSeedResult {
    data class Seeded(val entryCount: Int) : DemoTimelineSeedResult

    data object AlreadyHasEntries : DemoTimelineSeedResult
}

/**
 * A reconstructed (or manually seeded) portion of a day.
 *
 * [title] is a fallback for entries without a linked [place]. The UI should use
 * [displayTitle] so renaming a place is reflected in every linked entry.
 */
data class TimelineEntry(
    val id: Long,
    val date: LocalDate,
    val type: TimelineEntryType,
    val startedAt: Instant,
    val endedAt: Instant,
    val title: String,
    val subtitle: String?,
    val place: Place?,
    val movementType: MovementType?,
    val confidence: Float,
    val wasUserEdited: Boolean,
) {
    init {
        require(id > 0) { "Timeline entry id must be positive." }
        require(title.isNotBlank()) { "Timeline entry title cannot be blank." }
        require(!endedAt.isBefore(startedAt)) { "An entry cannot end before it starts." }
        require(confidence in 0f..1f) { "Confidence must be between 0 and 1." }
    }

    val duration: Duration
        get() = Duration.between(startedAt, endedAt)

    val displayTitle: String
        get() = place?.name ?: title

    val hasLowConfidence: Boolean
        get() = confidence < LOW_CONFIDENCE_THRESHOLD

    private companion object {
        const val LOW_CONFIDENCE_THRESHOLD = 0.5f
    }
}
