package com.herologs.feature.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.herologs.core.model.TimelineEntry
import com.herologs.core.model.TimelineEntryType
import com.herologs.designsystem.Calm
import com.herologs.designsystem.Drive
import com.herologs.designsystem.Ink
import com.herologs.designsystem.Joy
import com.herologs.designsystem.Meaning
import com.herologs.designsystem.MutedInk
import com.herologs.designsystem.Paper
import com.herologs.designsystem.Sand
import com.herologs.designsystem.Surface
import com.herologs.designsystem.Vitality
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TimelineRoute(
    onEntryClick: (Long) -> Unit,
    viewModel: TimelineViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TimelineScreen(
        state = state,
        onDateSelected = viewModel::selectDate,
        onLoadDemoTimeline = viewModel::seedDemoTimeline,
        onDismissError = viewModel::dismissSeedError,
        onEntryClick = onEntryClick,
    )
}

@Composable
fun TimelineScreen(
    state: TimelineUiState,
    onDateSelected: (LocalDate) -> Unit,
    onLoadDemoTimeline: () -> Unit,
    onDismissError: () -> Unit,
    onEntryClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .background(Paper)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Spacer(Modifier.height(8.dp))
            TimelineTitle(state.selectedDate)
        }
        item {
            DayStrip(
                selectedDate = state.selectedDate,
                onDateSelected = onDateSelected,
            )
        }
        item {
            LocalDataNotice()
        }
        state.seedError?.let { error ->
            item {
                ErrorCard(message = error, onDismiss = onDismissError)
            }
        }
        when {
            state.isLoading -> item { TimelineLoading() }
            state.isEmpty -> item {
                EmptyTimelineCard(
                    isLoadingDemo = state.isSeedingDemo,
                    onLoadDemoTimeline = onLoadDemoTimeline,
                )
            }
            else -> {
                item {
                    Text("TU DÍA", style = MaterialTheme.typography.titleMedium)
                }
                items(
                    items = state.entries,
                    key = TimelineEntry::id,
                ) { entry ->
                    TimelineEntryCard(
                        entry = entry,
                        onClick = { onEntryClick(entry.id) },
                    )
                }
            }
        }
        item { Spacer(Modifier.height(12.dp)) }
    }
}

@Composable
private fun TimelineTitle(date: LocalDate) {
    Text(
        text = date.format(FULL_DATE_FORMATTER),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
private fun DayStrip(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        (-3L..3L).forEach { dayOffset ->
            val date = selectedDate.plusDays(dayOffset)
            val isSelected = date == selectedDate
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isSelected) Ink else Surface)
                    .clickable(onClick = { onDateSelected(date) })
                    .semantics {
                        role = Role.Button
                        contentDescription = date.format(FULL_DATE_FORMATTER)
                    }
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = date.format(SHORT_WEEKDAY_FORMATTER).uppercase(LOCALE),
                    color = if (isSelected) Surface else MutedInk,
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = date.dayOfMonth.toString(),
                    color = if (isSelected) Surface else Ink,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Composable
private fun LocalDataNotice() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Sand.copy(alpha = 0.55f)),
        shape = RoundedCornerShape(18.dp),
    ) {
        Text(
            text = "Todavía no usamos ubicación ni datos de salud. Podés explorar y corregir una Timeline local de ejemplo.",
            modifier = Modifier.padding(16.dp),
            color = Ink,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun TimelineLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 44.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = Ink)
    }
}

@Composable
private fun EmptyTimelineCard(
    isLoadingDemo: Boolean,
    onLoadDemoTimeline: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(22.dp),
    ) {
        Column(Modifier.padding(22.dp)) {
            Text("Aún no hay eventos para este día", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(
                "Cargá una muestra local para probar la Timeline y sus correcciones. No se solicita ningún permiso.",
                color = MutedInk,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(18.dp))
            Button(
                onClick = onLoadDemoTimeline,
                enabled = !isLoadingDemo,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Ink, contentColor = Surface),
            ) {
                Text(if (isLoadingDemo) "Cargando…" else "Cargar día de ejemplo")
            }
        }
    }
}

@Composable
private fun ErrorCard(
    message: String,
    onDismiss: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Vitality.copy(alpha = 0.18f)),
        shape = RoundedCornerShape(18.dp),
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 10.dp, end = 8.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(message, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    }
}

@Composable
private fun TimelineEntryCard(
    entry: TimelineEntry,
    onClick: () -> Unit,
) {
    val visuals = entry.visuals()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .semantics {
                role = Role.Button
                contentDescription = "${entry.displayTitle}, ${entry.duration.formatted()}. Abrir para corregir."
            },
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(20.dp),
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = entry.startedAt.atZone(ZoneId.systemDefault()).format(TIME_FORMATTER),
                color = MutedInk,
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(Modifier.width(14.dp))
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(visuals.color),
            )
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(entry.displayTitle, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(3.dp))
                Text(
                    text = listOfNotNull(
                        entry.subtitle,
                        entry.movementType?.localizedLabel(),
                        entry.duration.formatted(),
                    ).joinToString(" · "),
                    color = MutedInk,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = entry.confidence.localizedLabel(),
                    color = Ink,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Text("Editar", color = Ink, style = MaterialTheme.typography.labelLarge)
        }
    }
}

private data class EntryVisuals(
    val color: Color,
)

private fun TimelineEntry.visuals(): EntryVisuals = when (type) {
    TimelineEntryType.PLACE -> EntryVisuals(Calm)
    TimelineEntryType.TRIP -> EntryVisuals(Drive)
    TimelineEntryType.SLEEP -> EntryVisuals(Meaning)
    TimelineEntryType.UNKNOWN -> EntryVisuals(Joy)
}

private fun Duration.formatted(): String {
    val totalMinutes = toMinutes().coerceAtLeast(0)
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return when {
        hours > 0 && minutes > 0 -> "$hours h $minutes min"
        hours > 0 -> "$hours h"
        else -> "$minutes min"
    }
}

private fun Float.localizedLabel(): String = when {
    this >= 0.8f -> "Confianza alta"
    this >= 0.5f -> "Confianza media"
    else -> "Confianza baja: revisá este evento"
}

private fun com.herologs.core.model.MovementType.localizedLabel(): String = when (this) {
    com.herologs.core.model.MovementType.WALKING -> "Caminando"
    com.herologs.core.model.MovementType.RUNNING -> "Corriendo"
    com.herologs.core.model.MovementType.CYCLING -> "En bici"
    com.herologs.core.model.MovementType.IN_VEHICLE -> "En vehículo"
    com.herologs.core.model.MovementType.TRANSIT -> "Transporte público"
    com.herologs.core.model.MovementType.STILL -> "Sin movimiento"
    com.herologs.core.model.MovementType.UNKNOWN -> "Movimiento sin clasificar"
}

private val LOCALE = Locale.forLanguageTag("es-AR")
private val FULL_DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", LOCALE)
private val SHORT_WEEKDAY_FORMATTER = DateTimeFormatter.ofPattern("EE", LOCALE)
private val TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm", LOCALE)
