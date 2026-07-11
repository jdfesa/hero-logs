package com.herologs.feature.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.herologs.core.model.MovementType
import com.herologs.core.model.TimelineEntry
import com.herologs.core.model.TimelineEntryType
import com.herologs.designsystem.Drive
import com.herologs.designsystem.Ink
import com.herologs.designsystem.MutedInk
import com.herologs.designsystem.Paper
import com.herologs.designsystem.Surface
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TimelineEntryDetailRoute(
    onBack: () -> Unit,
    viewModel: TimelineEntryDetailViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TimelineEntryDetailScreen(
        state = state,
        onBack = onBack,
        onSaveTitle = viewModel::saveTitle,
        onSaveMovementType = viewModel::saveMovementType,
        onDismissError = viewModel::dismissError,
    )
}

@Composable
fun TimelineEntryDetailScreen(
    state: TimelineEntryDetailUiState,
    onBack: () -> Unit,
    onSaveTitle: (String) -> Unit,
    onSaveMovementType: (MovementType?) -> Unit,
    onDismissError: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        state.isLoading -> DetailLoading(modifier)
        state.isMissing -> MissingEntryScreen(modifier, onBack)
        else -> EntryDetailContent(
            entry = requireNotNull(state.entry),
            isSaving = state.isSaving,
            errorMessage = state.errorMessage,
            onBack = onBack,
            onSaveTitle = onSaveTitle,
            onSaveMovementType = onSaveMovementType,
            onDismissError = onDismissError,
            modifier = modifier,
        )
    }
}

@Composable
private fun DetailLoading(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Paper),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(color = Ink)
    }
}

@Composable
private fun MissingEntryScreen(
    modifier: Modifier,
    onBack: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Paper)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Este evento ya no está disponible", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(10.dp))
        Text(
            "Volvé a la Timeline para elegir otro evento.",
            textAlign = TextAlign.Center,
            color = MutedInk,
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = Ink)) {
            Text("Volver")
        }
    }
}

@Composable
private fun EntryDetailContent(
    entry: TimelineEntry,
    isSaving: Boolean,
    errorMessage: String?,
    onBack: () -> Unit,
    onSaveTitle: (String) -> Unit,
    onSaveMovementType: (MovementType?) -> Unit,
    onDismissError: () -> Unit,
    modifier: Modifier,
) {
    var showRenameDialog by rememberSaveable(entry.id) { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Paper)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = onBack) {
                Text("‹ Volver")
            }
            Text(
                "DETALLE DEL EVENTO",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.labelLarge,
            )
        }
        Card(
            colors = CardDefaults.cardColors(containerColor = Surface),
            shape = RoundedCornerShape(22.dp),
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(entry.displayTitle, style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(8.dp))
                Text(
                    entry.type.localizedLabel(),
                    color = MutedInk,
                    style = MaterialTheme.typography.labelLarge,
                )
                Spacer(Modifier.height(18.dp))
                DetailRow("Horario", entry.timeRange())
                DetailRow("Duración", entry.duration.toReadableDuration())
                DetailRow("Confianza", entry.confidence.toConfidenceLabel())
                entry.subtitle?.let { DetailRow("Contexto", it) }
                if (entry.wasUserEdited) {
                    Spacer(Modifier.height(10.dp))
                    Text("Corregido por vos", color = Drive, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
        Card(
            colors = CardDefaults.cardColors(containerColor = Surface),
            shape = RoundedCornerShape(22.dp),
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("CORREGIR NOMBRE", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                Text(
                    if (entry.place != null) {
                        "Cambiar este lugar actualizará todas las visitas asociadas."
                    } else {
                        "Podés corregir el nombre de este evento local."
                    },
                    color = MutedInk,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = { showRenameDialog = true },
                    enabled = !isSaving,
                    colors = ButtonDefaults.buttonColors(containerColor = Ink),
                ) {
                    Text("Cambiar nombre")
                }
            }
        }
        if (entry.type == TimelineEntryType.TRIP) {
            MovementCorrectionCard(
                currentMovementType = entry.movementType,
                isSaving = isSaving,
                onSaveMovementType = onSaveMovementType,
            )
        }
        errorMessage?.let { message ->
            ErrorMessage(message = message, onDismiss = onDismissError)
        }
        Spacer(Modifier.height(12.dp))
    }

    if (showRenameDialog) {
        RenameDialog(
            initialValue = entry.displayTitle,
            onDismiss = { showRenameDialog = false },
            onConfirm = { value ->
                onSaveTitle(value)
                showRenameDialog = false
            },
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, color = MutedInk, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(1.dp))
        Text(value, textAlign = TextAlign.End, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun MovementCorrectionCard(
    currentMovementType: MovementType?,
    isSaving: Boolean,
    onSaveMovementType: (MovementType?) -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(22.dp),
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("CÓMO TE MOVISTE", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            Text(
                "Elegí la clasificación que mejor describe este traslado.",
                color = MutedInk,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(12.dp))
            MovementType.entries.forEach { movementType ->
                FilterChip(
                    selected = currentMovementType == movementType,
                    onClick = { onSaveMovementType(movementType) },
                    enabled = !isSaving,
                    label = { Text(movementType.toLocalizedLabel()) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                )
            }
            if (currentMovementType != null) {
                TextButton(
                    onClick = { onSaveMovementType(null) },
                    enabled = !isSaving,
                ) {
                    Text("Quitar clasificación")
                }
            }
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String,
    onDismiss: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(18.dp),
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
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
private fun RenameDialog(
    initialValue: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var value by rememberSaveable(initialValue) { mutableStateOf(initialValue) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cambiar nombre") },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = { value = it },
                label = { Text("Nombre") },
                singleLine = true,
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(value) },
                enabled = value.isNotBlank(),
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
    )
}

private fun TimelineEntry.timeRange(): String = "${startedAt.atZone(ZoneId.systemDefault()).format(TIME_FORMATTER)} – ${endedAt.atZone(ZoneId.systemDefault()).format(TIME_FORMATTER)}"

private fun java.time.Duration.toReadableDuration(): String {
    val totalMinutes = toMinutes().coerceAtLeast(0)
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return when {
        hours > 0 && minutes > 0 -> "$hours h $minutes min"
        hours > 0 -> "$hours h"
        else -> "$minutes min"
    }
}

private fun TimelineEntryType.localizedLabel(): String = when (this) {
    TimelineEntryType.PLACE -> "Lugar"
    TimelineEntryType.TRIP -> "Traslado"
    TimelineEntryType.SLEEP -> "Sueño"
    TimelineEntryType.UNKNOWN -> "Sin clasificar"
}

private fun Float.toConfidenceLabel(): String = when {
    this >= 0.8f -> "Alta"
    this >= 0.5f -> "Media"
    else -> "Baja — revisá este evento"
}

private fun MovementType.toLocalizedLabel(): String = when (this) {
    MovementType.WALKING -> "Caminando"
    MovementType.RUNNING -> "Corriendo"
    MovementType.CYCLING -> "En bici"
    MovementType.IN_VEHICLE -> "En vehículo"
    MovementType.TRANSIT -> "Transporte público"
    MovementType.STILL -> "Sin movimiento"
    MovementType.UNKNOWN -> "No estoy seguro"
}

private val TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm", Locale.forLanguageTag("es-AR"))
