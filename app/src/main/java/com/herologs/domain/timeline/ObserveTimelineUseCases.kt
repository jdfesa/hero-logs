package com.herologs.domain.timeline

import com.herologs.core.model.TimelineEntry
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

class ObserveTimelineDayUseCase(
    private val repository: TimelineRepository,
) {
    operator fun invoke(date: LocalDate): Flow<List<TimelineEntry>> = repository.observeDay(date)
}

class ObserveTimelineEntryUseCase(
    private val repository: TimelineRepository,
) {
    operator fun invoke(entryId: Long): Flow<TimelineEntry?> = repository.observeEntry(entryId)
}
