package com.herologs.feature.timeline

import com.herologs.core.model.DemoTimelineSeedResult
import com.herologs.core.model.MovementType
import com.herologs.core.model.TimelineEntry
import com.herologs.core.model.TimelineEntryType
import com.herologs.domain.timeline.ObserveTimelineDayUseCase
import com.herologs.domain.timeline.SeedDemoTimelineUseCase
import com.herologs.domain.timeline.TimelineDemoData
import com.herologs.domain.timeline.TimelineRepository
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TimelineViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `selecting a day exposes the entries for that day`() = runTest {
        val firstDay = LocalDate.of(2026, 7, 11)
        val secondDay = firstDay.minusDays(1)
        val repository = FakeTimelineRepository(
            entriesByDay = mapOf(
                firstDay to listOf(entry(id = 1, date = firstDay)),
                secondDay to listOf(entry(id = 2, date = secondDay)),
            ),
        )
        val viewModel = createViewModel(repository, FakeTimelineDemoData(), firstDay)

        assertEquals(
            1,
            viewModel.uiState.first { !it.isLoading }.entries.single().id,
        )

        viewModel.selectDate(secondDay)

        val state = viewModel.uiState.first {
            !it.isLoading && it.selectedDate == secondDay
        }
        assertEquals(2, state.entries.single().id)
    }

    @Test
    fun `loading demo data targets the selected day`() = runTest {
        val date = LocalDate.of(2026, 7, 11)
        val demoData = FakeTimelineDemoData()
        val viewModel = createViewModel(FakeTimelineRepository(), demoData, date)

        viewModel.seedDemoTimeline()
        advanceUntilIdle()

        assertEquals(listOf(date), demoData.seededDates)
    }

    private fun createViewModel(
        repository: TimelineRepository,
        demoData: TimelineDemoData,
        date: LocalDate,
    ): TimelineViewModel = TimelineViewModel(
        observeTimelineDay = ObserveTimelineDayUseCase(repository),
        seedDemoTimeline = SeedDemoTimelineUseCase(demoData),
        clock = Clock.fixed(date.atStartOfDay().toInstant(ZoneOffset.UTC), ZoneOffset.UTC),
    )

    private fun entry(id: Long, date: LocalDate): TimelineEntry = TimelineEntry(
        id = id,
        date = date,
        type = TimelineEntryType.PLACE,
        startedAt = date.atStartOfDay().toInstant(ZoneOffset.UTC),
        endedAt = date.atStartOfDay().plusHours(1).toInstant(ZoneOffset.UTC),
        title = "Casa",
        subtitle = null,
        place = null,
        movementType = MovementType.STILL,
        confidence = 0.9f,
        wasUserEdited = false,
    )

    private class FakeTimelineRepository(
        entriesByDay: Map<LocalDate, List<TimelineEntry>> = emptyMap(),
    ) : TimelineRepository {
        private val entries = entriesByDay.toMutableMap()

        override fun observeDay(date: LocalDate): Flow<List<TimelineEntry>> =
            flowOf(entries[date].orEmpty())

        override fun observeEntry(entryId: Long): Flow<TimelineEntry?> = flowOf(null)

        override suspend fun renameEntryTitle(entryId: Long, title: String): Boolean = false

        override suspend fun renamePlace(placeId: Long, name: String): Boolean = false

        override suspend fun correctMovementType(
            entryId: Long,
            movementType: MovementType?,
        ): Boolean = false
    }

    private class FakeTimelineDemoData : TimelineDemoData {
        val seededDates = mutableListOf<LocalDate>()

        override suspend fun seedFor(date: LocalDate): DemoTimelineSeedResult {
            seededDates += date
            return DemoTimelineSeedResult.Seeded(entryCount = 6)
        }
    }
}
