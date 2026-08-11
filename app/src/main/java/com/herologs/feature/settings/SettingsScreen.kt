package com.herologs.feature.settings

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.annotation.StringRes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.herologs.R
import com.herologs.designsystem.Ink
import com.herologs.designsystem.MutedInk
import com.herologs.designsystem.Paper
import com.herologs.designsystem.Surface
import com.herologs.domain.permissions.PermissionAccessStatus
import com.herologs.domain.permissions.PermissionCapability
import com.herologs.domain.permissions.PermissionCapabilityState

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(viewModel) {
        viewModel.refreshPermissions()
    }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) {
        viewModel.refreshPermissions()
    }
    val activityPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) {
        viewModel.refreshPermissions()
    }
    SettingsScreen(
        uiState = uiState,
        onShowOnboardingAgain = viewModel::showOnboardingAgain,
        onRequestPermission = { capability ->
            when (capability) {
                PermissionCapability.FOREGROUND_LOCATION -> locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    ),
                )

                PermissionCapability.ACTIVITY_RECOGNITION -> activityPermissionLauncher.launch(
                    Manifest.permission.ACTIVITY_RECOGNITION,
                )

                PermissionCapability.HEALTH_CONNECT -> Unit
            }
        },
    )
}

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onShowOnboardingAgain: () -> Unit,
    onRequestPermission: (PermissionCapability) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Paper)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Spacer(Modifier.height(12.dp))
        Text(stringResource(R.string.settings_title), style = MaterialTheme.typography.headlineMedium)
        SettingsCard(
            eyebrow = stringResource(R.string.settings_privacy_eyebrow),
            title = stringResource(R.string.settings_privacy_title),
            body = stringResource(R.string.settings_privacy_body),
        )
        Text(
            text = stringResource(R.string.settings_permissions_eyebrow),
            style = MaterialTheme.typography.labelLarge,
        )
        uiState.permissionOverview.capabilities.forEach { permissionState ->
            PermissionStatusCard(
                state = permissionState,
                onRequestPermission = onRequestPermission,
            )
        }
        Card(
            colors = CardDefaults.cardColors(containerColor = Surface),
            shape = RoundedCornerShape(22.dp),
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    stringResource(R.string.settings_experience_eyebrow),
                    style = MaterialTheme.typography.labelLarge,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    stringResource(R.string.settings_onboarding_title),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    stringResource(R.string.settings_onboarding_body),
                    color = MutedInk,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = onShowOnboardingAgain,
                    enabled = !uiState.isUpdatingOnboarding,
                    colors = ButtonDefaults.buttonColors(containerColor = Ink, contentColor = Surface),
                ) {
                    Text(
                        stringResource(
                            if (uiState.isUpdatingOnboarding) {
                                R.string.settings_onboarding_updating
                            } else {
                                R.string.settings_onboarding_action
                            },
                        ),
                    )
                }
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun PermissionStatusCard(
    state: PermissionCapabilityState,
    onRequestPermission: (PermissionCapability) -> Unit,
) {
    val copy = state.toCopy()
    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(22.dp),
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                text = stringResource(copy.titleRes),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(copy.statusRes),
                color = Ink,
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(copy.bodyRes),
                color = MutedInk,
                style = MaterialTheme.typography.bodyMedium,
            )
            if (state.canRequest) {
                val actionRes = checkNotNull(copy.actionRes)
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = { onRequestPermission(state.capability) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Ink,
                        contentColor = Surface,
                    ),
                ) {
                    Text(stringResource(actionRes))
                }
            }
        }
    }
}

private data class PermissionStatusCopy(
    @param:StringRes val titleRes: Int,
    @param:StringRes val statusRes: Int,
    @param:StringRes val bodyRes: Int,
    @param:StringRes val actionRes: Int?,
)

private val PermissionCapabilityState.canRequest: Boolean
    get() = access == PermissionAccessStatus.NOT_GRANTED &&
        capability != PermissionCapability.HEALTH_CONNECT

private fun PermissionCapabilityState.toCopy(): PermissionStatusCopy = when (capability) {
    PermissionCapability.FOREGROUND_LOCATION -> PermissionStatusCopy(
        titleRes = R.string.settings_location_title,
        statusRes = when (access) {
            PermissionAccessStatus.GRANTED -> R.string.settings_status_precise
            PermissionAccessStatus.LIMITED -> R.string.settings_status_approximate
            else -> R.string.settings_status_not_connected
        },
        bodyRes = when (access) {
            PermissionAccessStatus.GRANTED -> R.string.settings_location_precise_body
            PermissionAccessStatus.LIMITED -> R.string.settings_location_approximate_body
            else -> R.string.settings_location_missing_body
        },
        actionRes = R.string.settings_location_action,
    )

    PermissionCapability.ACTIVITY_RECOGNITION -> PermissionStatusCopy(
        titleRes = R.string.settings_activity_title,
        statusRes = when (access) {
            PermissionAccessStatus.GRANTED -> R.string.settings_status_connected
            PermissionAccessStatus.NOT_REQUIRED -> R.string.settings_status_available
            else -> R.string.settings_status_not_connected
        },
        bodyRes = when (access) {
            PermissionAccessStatus.GRANTED -> R.string.settings_activity_granted_body
            PermissionAccessStatus.NOT_REQUIRED -> R.string.settings_activity_not_required_body
            else -> R.string.settings_activity_missing_body
        },
        actionRes = R.string.settings_activity_action,
    )

    PermissionCapability.HEALTH_CONNECT -> PermissionStatusCopy(
        titleRes = R.string.settings_health_connect_title,
        statusRes = R.string.settings_status_not_configured,
        bodyRes = R.string.settings_health_connect_body,
        actionRes = null,
    )
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
