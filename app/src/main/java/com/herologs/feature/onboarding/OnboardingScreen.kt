package com.herologs.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.herologs.R
import com.herologs.designsystem.Ink
import com.herologs.designsystem.Paper
import com.herologs.designsystem.Sand
import com.herologs.designsystem.Surface

@Composable
fun OnboardingRoute(
    viewModel: OnboardingViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.isLoading) {
        LoadingScreen()
    } else {
        OnboardingScreen(
            uiState = state,
            onNext = viewModel::nextStep,
            onPrevious = viewModel::previousStep,
            onComplete = viewModel::completeOnboarding,
        )
    }
}

@Composable
fun OnboardingScreen(
    uiState: OnboardingUiState,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Paper)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        HeaderSection(
            currentStepIndex = uiState.currentStepIndex,
            totalSteps = uiState.totalSteps,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false),
            verticalArrangement = Arrangement.Center,
        ) {
            when (uiState.currentStep) {
                OnboardingStep.VALUE_PROP -> ValuePropStepContent()
                OnboardingStep.PRIVACY_PROMISE -> PrivacyPromiseStepContent()
                OnboardingStep.FUTURE_SIGNALS -> FutureSignalsStepContent()
                OnboardingStep.READY -> ReadyStepContent()
            }
        }

        NavigationSection(
            uiState = uiState,
            onNext = onNext,
            onPrevious = onPrevious,
            onComplete = onComplete,
        )
    }
}

@Composable
private fun HeaderSection(
    currentStepIndex: Int,
    totalSteps: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Sand),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("H", style = MaterialTheme.typography.headlineMedium)
        }

        Text(
            text = stringResource(
                R.string.onboarding_step_indicator,
                currentStepIndex + 1,
                totalSteps,
            ),
            style = MaterialTheme.typography.labelLarge,
            color = Ink,
        )
    }
}

@Composable
private fun ValuePropStepContent() {
    Column {
        Text(
            text = stringResource(R.string.onboarding_value_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.onboarding_value_subtitle),
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(Modifier.height(24.dp))
        PrivacyPromiseCard()
    }
}

@Composable
private fun PrivacyPromiseStepContent() {
    Column {
        Text(
            text = stringResource(R.string.onboarding_privacy_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(Modifier.height(16.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = Surface),
            shape = RoundedCornerShape(20.dp),
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    text = stringResource(R.string.onboarding_privacy_badge),
                    style = MaterialTheme.typography.labelLarge,
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.onboarding_privacy_body),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun FutureSignalsStepContent() {
    Column {
        Text(
            text = stringResource(R.string.onboarding_signals_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(Modifier.height(16.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = Surface),
            shape = RoundedCornerShape(20.dp),
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    text = stringResource(R.string.onboarding_signals_badge),
                    style = MaterialTheme.typography.labelLarge,
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.onboarding_signals_body),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(14.dp))
                Text(
                    text = stringResource(R.string.onboarding_signals_notice),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Ink,
                )
            }
        }
    }
}

@Composable
private fun ReadyStepContent() {
    Column {
        Text(
            text = stringResource(R.string.onboarding_ready_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.onboarding_ready_body),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun PrivacyPromiseCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                text = stringResource(R.string.onboarding_privacy_badge),
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.onboarding_privacy_body),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun NavigationSection(
    uiState: OnboardingUiState,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onComplete: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (uiState.canGoBack) {
            OutlinedButton(
                onClick = onPrevious,
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(text = stringResource(R.string.onboarding_action_previous))
            }
            Spacer(Modifier.width(12.dp))
        }

        if (uiState.canGoNext) {
            Button(
                onClick = onNext,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Ink,
                    contentColor = Surface,
                ),
            ) {
                Text(
                    text = stringResource(R.string.onboarding_action_next),
                    fontWeight = FontWeight.Bold,
                )
            }
        } else {
            Button(
                onClick = onComplete,
                enabled = !uiState.isSavingCompletion,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Ink,
                    contentColor = Surface,
                ),
            ) {
                Text(
                    text = stringResource(R.string.onboarding_action_start_no_permissions),
                    modifier = Modifier.padding(vertical = 4.dp),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(color = Ink)
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.onboarding_loading),
            textAlign = TextAlign.Center,
        )
    }
}
