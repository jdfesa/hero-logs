package com.herologs.data.timeline

import com.herologs.core.database.entity.PlaceEntity
import com.herologs.core.database.entity.TimelineEntryEntity
import com.herologs.core.database.entity.TimelineEntryWithPlace
import com.herologs.core.model.MovementType
import com.herologs.core.model.PlaceCategory
import com.herologs.core.model.TimelineEntryType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class TimelineMappersTest {
    @Test
    fun `unknown persisted enum values map to safe domain fallbacks`() {
        val mapped = TimelineEntryWithPlace(
            entry = TimelineEntryEntity(
                id = 4,
                dayEpochDay = 20_280,
                type = "FUTURE_TYPE",
                startedAtEpochMillis = 1_784_160_000_000,
                endedAtEpochMillis = 1_784_163_600_000,
                placeId = 9,
                movementType = "FUTURE_MOVEMENT",
                title = "Evento pendiente",
                subtitle = null,
                confidence = 1.4f,
                userEdited = true,
            ),
            place = PlaceEntity(
                id = 9,
                name = "Lugar personal",
                category = "FUTURE_CATEGORY",
                createdAtEpochMillis = 1,
                updatedAtEpochMillis = 2,
            ),
        ).toDomain()

        assertEquals(TimelineEntryType.UNKNOWN, mapped.type)
        assertEquals(MovementType.UNKNOWN, mapped.movementType)
        assertEquals(PlaceCategory.OTHER, mapped.place?.category)
        assertEquals(1f, mapped.confidence)
        assertEquals("Lugar personal", mapped.displayTitle)
        assertFalse(mapped.hasLowConfidence)
    }
}
