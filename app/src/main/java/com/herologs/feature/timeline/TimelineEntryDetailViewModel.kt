package com.herologs.feature.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.herologs.core.model.MovementType
import com.herologs.core.model.TimelineEntry
import com.herologs.domain.timeline.CorrectTimelineMovementUseCase
import com.herologs.domain.timeline.ObserveTimelineEntryUseCase
import com.herologs.domain.timeline.RenamePlaceUseCase
import com.herologs.domain.timeline.RenameTimelineEntryTitleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TimelineEntryDetailUiState(
    val isLoading: Boolean = true,
    val entry: TimelineEntry? = null,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
) {
    val isMissing: Boolean
        get() = !isLoading && entry == null
}

class TimelineEntryDetailViewModel(
    private val entryId: Long,
    observeTimelineEntry: ObserveTimelineEntryUseCase,
    private val renameTimelineEntryTitle: RenameTimelineEntryTitleUseCase,
    private val renamePlace: RenamePlaceUseCase,
    private val correctTimelineMovement: CorrectTimelineMovementUseCase,
) : ViewModel() {
    private val isSaving = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<TimelineEntryDetailUiState> = combine(
        observeTimelineEntry(entryId),
        isSaving,
        errorMessage,
    ) { entry, saving, error ->
        TimelineEntryDetailUiState(
            isLoading = false,
            entry = entry,
            isSaving = saving,
            errorMessage = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = TimelineEntryDetailUiState(),
    )

    fun saveTitle(title: String) {
        val entry = uiState.value.entry ?: return
        if (title.isBlank()) {
            errorMessage.value = "El nombre no puede estar vacío."
            return
        }

        viewModelScope.launch {
            save {
                if (entry.place != null) {
                    renamePlace(entry.place.id, title)
                } else {
                    renameTimelineEntryTitle(entry.id, title)
                }
            }
        }
    }

    fun saveMovementType(movementType: MovementType?) {
        val entry = uiState.value.entry ?: return
        viewModelScope.launch {
            save {
                correctTimelineMovement(entry.id, movementType)
            }
        }
    }

    fun dismissError() {
        errorMessage.value = null
    }

    private suspend fun save(action: suspend () -> Boolean) {
        if (isSaving.value) return

        isSaving.value = true
        errorMessage.value = null
        try {
            if (!action()) {
                errorMessage.value = "No pudimos guardar el cambio."
            }
        } catch (_: IllegalArgumentException) {
            errorMessage.value = "Revisá el valor ingresado e intentá de nuevo."
        } catch (_: Exception) {
            errorMessage.value = "Ocurrió un problema al guardar el cambio."
        } finally {
            isSaving.value = false
        }
    }

    companion object {
        fun factory(
            entryId: Long,
            observeTimelineEntry: ObserveTimelineEntryUseCase,
            renameTimelineEntryTitle: RenameTimelineEntryTitleUseCase,
            renamePlace: RenamePlaceUseCase,
            correctTimelineMovement: CorrectTimelineMovementUseCase,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TimelineEntryDetailViewModel(
                    entryId = entryId,
                    observeTimelineEntry = observeTimelineEntry,
                    renameTimelineEntryTitle = renameTimelineEntryTitle,
                    renamePlace = renamePlace,
                    correctTimelineMovement = correctTimelineMovement,
                )
            }
        }

        private const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
