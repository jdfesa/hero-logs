package com.herologs.domain.timeline

import com.herologs.core.model.DemoTimelineSeedResult
import java.time.LocalDate

class SeedDemoTimelineUseCase(
    private val demoData: TimelineDemoData,
) {
    suspend operator fun invoke(date: LocalDate): DemoTimelineSeedResult = demoData.seedFor(date)
}
