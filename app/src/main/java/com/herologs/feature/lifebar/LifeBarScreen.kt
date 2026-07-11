package com.herologs.feature.lifebar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.herologs.designsystem.Drive
import com.herologs.designsystem.Ink
import com.herologs.designsystem.MutedInk
import com.herologs.designsystem.Paper
import com.herologs.designsystem.Sand
import com.herologs.designsystem.Surface
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun LifeBarRoute(
    onOpenTimeline: () -> Unit,
    viewModel: LifeBarViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LifeBarScreen(
        state = state,
        onOpenTimeline = onOpenTimeline,
    )
}

@Composable
fun LifeBarScreen(
    state: LifeBarUiState,
    onOpenTimeline: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isLoading) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Paper),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CircularProgressIndicator(color = Ink)
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Paper)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Spacer(Modifier.height(12.dp))
        Header(state.date.format(DATE_FORMATTER))
        HeroCard()
        ScoreStatusCard()
        TimelineStatusCard(
            entryCount = state.timelineEntryCount,
            onOpenTimeline = onOpenTimeline,
        )
        PrivacyCard()
    }
}

@Composable
private fun Header(date: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(22.dp),
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("HeroLogs", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(4.dp))
            Text(date, color = MutedInk, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun HeroCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(22.dp),
    ) {
        Column(Modifier.padding(20.dp)) {
            Column(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Sand),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("H", style = MaterialTheme.typography.headlineMedium)
            }
            Spacer(Modifier.height(16.dp))
            Text("Tu registro empieza con contexto.", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(6.dp))
            Text(
                "HeroLogs no asigna un puntaje hasta tener señales suficientes y explicables.",
                color = MutedInk,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun ScoreStatusCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Ink),
        shape = RoundedCornerShape(22.dp),
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("LIFE SCORE", color = Surface, style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(12.dp))
            Text("Aún no disponible", color = Surface, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                "Cuando conectes señales opcionales, cada dimensión incluirá su explicación y nivel de confianza.",
                color = Surface,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun TimelineStatusCard(
    entryCount: Int,
    onOpenTimeline: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(22.dp),
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("TU TIMELINE", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            Text(
                if (entryCount == 0) "No hay eventos para hoy." else "$entryCount eventos locales hoy.",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Podés cargar un día de ejemplo y corregir sus lugares o traslados.",
                color = MutedInk,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(14.dp))
            Button(
                onClick = onOpenTimeline,
                colors = ButtonDefaults.buttonColors(containerColor = Drive, contentColor = Surface),
            ) {
                Text("Abrir Timeline")
            }
        }
    }
}

@Composable
private fun PrivacyCard() {
    Text(
        "Tus datos de ejemplo y tus correcciones se guardan sólo en este dispositivo.",
        modifier = Modifier.fillMaxWidth(),
        color = Ink,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
    )
}

private val DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", Locale.forLanguageTag("es-AR"))
