package com.herologs.feature.privacy

import androidx.annotation.StringRes
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.herologs.R
import com.herologs.designsystem.Ink
import com.herologs.designsystem.MutedInk
import com.herologs.designsystem.Paper
import com.herologs.designsystem.Surface
import com.herologs.domain.localdata.LocalDataCategory
import com.herologs.domain.localdata.LocalDataDeletionStage

@Composable
fun PrivacyDataRoute(
    onBack: () -> Unit,
    viewModel: PrivacyDataViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PrivacyDataScreen(
        uiState = uiState,
        onBack = onBack,
        onRequestDeleteAll = viewModel::requestDeleteAll,
        onConfirmDeleteAll = viewModel::confirmDeleteAll,
        onDismissDeleteConfirmation = viewModel::dismissDeleteConfirmation,
    )
}

@Composable
fun PrivacyDataScreen(
    uiState: PrivacyDataUiState,
    onBack: () -> Unit,
    onRequestDeleteAll: () -> Unit,
    onConfirmDeleteAll: () -> Unit,
    onDismissDeleteConfirmation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.isDeleteConfirmationVisible) {
        AlertDialog(
            onDismissRequest = onDismissDeleteConfirmation,
            title = { Text(stringResource(R.string.privacy_delete_dialog_title)) },
            text = { Text(stringResource(R.string.privacy_delete_dialog_body)) },
            confirmButton = {
                TextButton(onClick = onConfirmDeleteAll) {
                    Text(stringResource(R.string.privacy_delete_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissDeleteConfirmation) {
                    Text(stringResource(R.string.privacy_delete_cancel))
                }
            },
        )
    }

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
            TextButton(onClick = onBack, enabled = !uiState.isDeleting) {
                Text(stringResource(R.string.privacy_back))
            }
            Text(
                text = stringResource(R.string.privacy_eyebrow),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.labelLarge,
            )
        }
        Text(
            text = stringResource(R.string.privacy_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(R.string.privacy_intro),
            color = MutedInk,
            style = MaterialTheme.typography.bodyLarge,
        )
        uiState.storedCategories.forEach { category ->
            StoredCategoryCard(category)
        }
        InformationCard(
            title = stringResource(R.string.privacy_permissions_title),
            body = stringResource(R.string.privacy_permissions_body),
        )
        uiState.deletionFailure?.let { failure ->
            InformationCard(
                title = stringResource(R.string.privacy_delete_failure_title),
                body = stringResource(
                    if (failure.failedStage == LocalDataDeletionStage.DATABASE) {
                        R.string.privacy_delete_database_failure
                    } else {
                        R.string.privacy_delete_preferences_failure
                    },
                ),
            )
        }
        if (uiState.deletionComplete) {
            InformationCard(
                title = stringResource(R.string.privacy_delete_success_title),
                body = stringResource(R.string.privacy_delete_success_body),
            )
        }
        DeleteAllCard(
            isDeleting = uiState.isDeleting,
            onRequestDeleteAll = onRequestDeleteAll,
        )
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun StoredCategoryCard(category: LocalDataCategory) {
    val copy = category.toCopy()
    InformationCard(
        title = stringResource(copy.titleRes),
        body = stringResource(copy.bodyRes),
    )
}

@Composable
private fun InformationCard(
    title: String,
    body: String,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(22.dp),
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Text(body, color = MutedInk, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun DeleteAllCard(
    isDeleting: Boolean,
    onRequestDeleteAll: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(22.dp),
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                text = stringResource(R.string.privacy_delete_title),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.privacy_delete_body),
                color = MutedInk,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(14.dp))
            Button(
                onClick = onRequestDeleteAll,
                enabled = !isDeleting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
            ) {
                if (isDeleting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Ink,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(stringResource(R.string.privacy_delete_action))
                }
            }
        }
    }
}

private data class StoredCategoryCopy(
    @param:StringRes val titleRes: Int,
    @param:StringRes val bodyRes: Int,
)

private fun LocalDataCategory.toCopy(): StoredCategoryCopy = when (this) {
    LocalDataCategory.TIMELINE_ENTRIES -> StoredCategoryCopy(
        titleRes = R.string.privacy_timeline_title,
        bodyRes = R.string.privacy_timeline_body,
    )

    LocalDataCategory.PLACES -> StoredCategoryCopy(
        titleRes = R.string.privacy_places_title,
        bodyRes = R.string.privacy_places_body,
    )

    LocalDataCategory.APP_PREFERENCES -> StoredCategoryCopy(
        titleRes = R.string.privacy_preferences_title,
        bodyRes = R.string.privacy_preferences_body,
    )
}
