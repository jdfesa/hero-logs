package com.herologs.feature.lifebar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.herologs.domain.timeline.ObserveTimelineDayUseCase
import java.time.Clock
import java.time.LocalDate
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class LifeBarUiState(
    val date: LocalDate = LocalDate.now(),
    val timelineEntryCount: Int = 0,
    val isLoading: Boolean = true,
)

class LifeBarViewModel(
    observeTimelineDay: ObserveTimelineDayUseCase,
    clock: Clock = Clock.systemDefaultZone(),
) : ViewModel() {
    private val today = LocalDate.now(clock)

    val uiState: StateFlow<LifeBarUiState> = observeTimelineDay(today)
        .map { entries ->
            LifeBarUiState(
                date = today,
                timelineEntryCount = entries.size,
                isLoading = false,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = LifeBarUiState(date = today),
        )

    companion object {
        fun factory(
            observeTimelineDay: ObserveTimelineDayUseCase,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                LifeBarViewModel(observeTimelineDay)
            }
        }

        private const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
