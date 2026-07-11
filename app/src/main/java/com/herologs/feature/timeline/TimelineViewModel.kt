package com.herologs.feature.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.herologs.core.model.TimelineEntry
import com.herologs.domain.timeline.ObserveTimelineDayUseCase
import com.herologs.domain.timeline.SeedDemoTimelineUseCase
import java.time.Clock
import java.time.LocalDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

data class TimelineUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val entries: List<TimelineEntry> = emptyList(),
    val isLoading: Boolean = true,
    val isSeedingDemo: Boolean = false,
    val seedError: String? = null,
) {
    val isEmpty: Boolean
        get() = !isLoading && entries.isEmpty()
}

@OptIn(ExperimentalCoroutinesApi::class)
class TimelineViewModel(
    private val observeTimelineDay: ObserveTimelineDayUseCase,
    private val seedDemoTimeline: SeedDemoTimelineUseCase,
    clock: Clock = Clock.systemDefaultZone(),
) : ViewModel() {
    private val selectedDate = MutableStateFlow(LocalDate.now(clock))
    private val isSeedingDemo = MutableStateFlow(false)
    private val seedError = MutableStateFlow<String?>(null)

    private val timelineEntries: Flow<List<TimelineEntry>> = selectedDate.flatMapLatest { date ->
        observeTimelineDay(date)
    }

    val uiState: StateFlow<TimelineUiState> = combine(
        selectedDate,
        timelineEntries,
        isSeedingDemo,
        seedError,
    ) { date, entries, isSeeding, error ->
        TimelineUiState(
            selectedDate = date,
            entries = entries,
            isLoading = false,
            isSeedingDemo = isSeeding,
            seedError = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = TimelineUiState(selectedDate = selectedDate.value),
    )

    fun selectDate(date: LocalDate) {
        selectedDate.value = date
        seedError.value = null
    }

    fun seedDemoTimeline() {
        if (isSeedingDemo.value) return

        viewModelScope.launch {
            isSeedingDemo.value = true
            seedError.value = null
            try {
                seedDemoTimeline(selectedDate.value)
            } catch (_: Exception) {
                seedError.value = "No pudimos cargar el día de ejemplo. Intentá de nuevo."
            } finally {
                isSeedingDemo.value = false
            }
        }
    }

    fun dismissSeedError() {
        seedError.value = null
    }

    companion object {
        fun factory(
            observeTimelineDay: ObserveTimelineDayUseCase,
            seedDemoTimeline: SeedDemoTimelineUseCase,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TimelineViewModel(
                    observeTimelineDay = observeTimelineDay,
                    seedDemoTimeline = seedDemoTimeline,
                )
            }
        }

        private const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
