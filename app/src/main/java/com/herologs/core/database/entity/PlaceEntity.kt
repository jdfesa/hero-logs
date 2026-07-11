package com.herologs.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "places",
    indices = [Index(value = ["name"])],
)
data class PlaceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String,
    val createdAtEpochMillis: Long,
    val updatedAtEpochMillis: Long,
)
