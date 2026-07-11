package com.herologs.domain.timeline

import com.herologs.core.model.MovementType

class RenameTimelineEntryTitleUseCase(
    private val repository: TimelineRepository,
) {
    suspend operator fun invoke(entryId: Long, title: String): Boolean =
        repository.renameEntryTitle(entryId, title)
}

class RenamePlaceUseCase(
    private val repository: TimelineRepository,
) {
    suspend operator fun invoke(placeId: Long, name: String): Boolean =
        repository.renamePlace(placeId, name)
}

class CorrectTimelineMovementUseCase(
    private val repository: TimelineRepository,
) {
    suspend operator fun invoke(entryId: Long, movementType: MovementType?): Boolean =
        repository.correctMovementType(entryId, movementType)
}
