package com.herologs.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "timeline_entries",
    foreignKeys = [
        ForeignKey(
            entity = PlaceEntity::class,
            parentColumns = ["id"],
            childColumns = ["placeId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [
        Index(value = ["dayEpochDay", "startedAtEpochMillis"]),
        Index(value = ["placeId"]),
    ],
)
data class TimelineEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dayEpochDay: Long,
    val type: String,
    val startedAtEpochMillis: Long,
    val endedAtEpochMillis: Long,
    val placeId: Long?,
    val movementType: String?,
    val title: String,
    val subtitle: String?,
    val confidence: Float,
    val userEdited: Boolean = false,
)

/** Room projection used by the repository to resolve the user-visible place name. */
data class TimelineEntryWithPlace(
    @Embedded val entry: TimelineEntryEntity,
    @Relation(
        parentColumn = "placeId",
        entityColumn = "id",
    )
    val place: PlaceEntity?,
)
