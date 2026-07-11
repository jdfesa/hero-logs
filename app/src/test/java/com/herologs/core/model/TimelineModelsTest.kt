package com.herologs.core.model

import java.time.Instant
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TimelineModelsTest {
    @Test
    fun `display title uses the linked place name`() {
        val entry = timelineEntry(
            title = "Lugar sin clasificar",
            place = Place(
                id = 7,
                name = "Casa",
                category = PlaceCategory.HOME,
            ),
        )

        assertEquals("Casa", entry.displayTitle)
    }

    @Test
    fun `low confidence is exposed for correction affordances`() {
        assertTrue(timelineEntry(confidence = 0.49f).hasLowConfidence)
        assertFalse(timelineEntry(confidence = 0.5f).hasLowConfidence)
    }

    private fun timelineEntry(
        title: String = "Caminata",
        place: Place? = null,
        confidence: Float = 0.8f,
    ) = TimelineEntry(
        id = 1,
        date = LocalDate.of(2026, 7, 11),
        type = TimelineEntryType.TRIP,
        startedAt = Instant.parse("2026-07-11T12:00:00Z"),
        endedAt = Instant.parse("2026-07-11T12:20:00Z"),
        title = title,
        subtitle = null,
        place = place,
        movementType = MovementType.WALKING,
        confidence = confidence,
        wasUserEdited = false,
    )
}
