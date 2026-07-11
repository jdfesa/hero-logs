package com.herologs.domain.timeline

import com.herologs.core.model.DemoTimelineSeedResult
import java.time.LocalDate

/** Explicit, opt-in source of representative local data for the empty timeline state. */
interface TimelineDemoData {
    suspend fun seedFor(date: LocalDate): DemoTimelineSeedResult
}
