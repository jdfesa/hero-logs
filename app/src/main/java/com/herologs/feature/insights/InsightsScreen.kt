package com.herologs.feature.insights

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.herologs.designsystem.Ink
import com.herologs.designsystem.MutedInk
import com.herologs.designsystem.Paper
import com.herologs.designsystem.Surface

@Composable
fun InsightsScreen(
    onOpenTimeline: () -> Unit,
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
        Text("Insights", style = MaterialTheme.typography.headlineMedium)
        Card(
            colors = CardDefaults.cardColors(containerColor = Surface),
            shape = RoundedCornerShape(22.dp),
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("PUNTAJES EXPLICABLES", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(10.dp))
                Text("Todavía no hay un Life Score", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Text(
                    "No mostramos una puntuación inventada. Primero necesitamos señales suficientes y, cuando exista, vas a poder ver qué la movió y qué datos faltan.",
                    color = MutedInk,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        Card(
            colors = CardDefaults.cardColors(containerColor = Surface),
            shape = RoundedCornerShape(22.dp),
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("PRÓXIMO PASO", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Explorá una Timeline local y corregí un evento. Esas correcciones serán la base para futuros Insights.",
                    color = MutedInk,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(14.dp))
                Button(
                    onClick = onOpenTimeline,
                    colors = ButtonDefaults.buttonColors(containerColor = Ink, contentColor = Surface),
                ) {
                    Text("Abrir Timeline")
                }
            }
        }
    }
}
