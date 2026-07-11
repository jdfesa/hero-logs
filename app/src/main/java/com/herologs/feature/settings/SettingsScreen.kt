package com.herologs.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.herologs.designsystem.Ink
import com.herologs.designsystem.MutedInk
import com.herologs.designsystem.Paper
import com.herologs.designsystem.Surface

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = viewModel(),
) {
    val isUpdating by viewModel.isUpdating.collectAsStateWithLifecycle()
    SettingsScreen(
        isUpdating = isUpdating,
        onShowOnboardingAgain = viewModel::showOnboardingAgain,
    )
}

@Composable
fun SettingsScreen(
    isUpdating: Boolean,
    onShowOnboardingAgain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Paper)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Spacer(Modifier.height(12.dp))
        Text("Ajustes", style = MaterialTheme.typography.headlineMedium)
        SettingsCard(
            eyebrow = "PRIVACIDAD",
            title = "Tus datos son locales",
            body = "Este MVP no tiene cuenta ni sincronización en la nube. Todavía no se solicitaron permisos sensibles.",
        )
        SettingsCard(
            eyebrow = "PERMISOS",
            title = "Sin permisos conectados",
            body = "Ubicación, actividad y Health Connect se solicitarán de forma progresiva cuando estén implementados.",
        )
        Card(
            colors = CardDefaults.cardColors(containerColor = Surface),
            shape = RoundedCornerShape(22.dp),
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("EXPERIENCIA", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                Text("Volver a ver la bienvenida", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Repasá cómo funciona el modo local y sin permisos.",
                    color = MutedInk,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = onShowOnboardingAgain,
                    enabled = !isUpdating,
                    colors = ButtonDefaults.buttonColors(containerColor = Ink, contentColor = Surface),
                ) {
                    Text(if (isUpdating) "Actualizando…" else "Mostrar bienvenida")
                }
            }
        }
    }
}

@Composable
private fun SettingsCard(
    eyebrow: String,
    title: String,
    body: String,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(22.dp),
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(eyebrow, style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Text(body, color = MutedInk, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
