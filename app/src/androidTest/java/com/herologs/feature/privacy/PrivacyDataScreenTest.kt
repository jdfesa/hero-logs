package com.herologs.feature.privacy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.herologs.designsystem.HeroLogsTheme
import com.herologs.domain.localdata.LocalDataCategory
import com.herologs.domain.localdata.LocalDataDeletionResult
import com.herologs.domain.localdata.LocalDataDeletionStage
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PrivacyDataScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun deletionRequiresConfirmationAndCancellationIsSafe() {
        var confirmedDeletes = 0
        composeRule.setContent {
            var confirmationVisible by remember { mutableStateOf(false) }
            HeroLogsTheme {
                PrivacyDataScreen(
                    uiState = state(
                        isDeleteConfirmationVisible = confirmationVisible,
                    ),
                    onBack = {},
                    onRequestDeleteAll = { confirmationVisible = true },
                    onConfirmDeleteAll = {
                        confirmedDeletes += 1
                        confirmationVisible = false
                    },
                    onDismissDeleteConfirmation = { confirmationVisible = false },
                )
            }
        }

        assertEquals(0, confirmedDeletes)
        deleteAction().performScrollTo().performClick()
        composeRule.onNodeWithText("Delete all local HeroLogs data?").assertIsDisplayed()
        assertEquals(0, confirmedDeletes)

        composeRule.onNodeWithText("Cancel").performClick()
        composeRule.onNodeWithText("Delete all local HeroLogs data?").assertDoesNotExist()
        assertEquals(0, confirmedDeletes)

        deleteAction().performScrollTo().performClick()
        composeRule.onNodeWithText("Delete all data").performClick()

        assertEquals(1, confirmedDeletes)
    }

    @Test
    fun inventoryAndPermissionBoundaryAreVisible() {
        composeRule.setContent {
            HeroLogsTheme {
                PrivacyDataScreen(
                    uiState = state(),
                    onBack = {},
                    onRequestDeleteAll = {},
                    onConfirmDeleteAll = {},
                    onDismissDeleteConfirmation = {},
                )
            }
        }

        composeRule.onNodeWithText("Timeline entries").assertIsDisplayed()
        composeRule.onNodeWithText("Places").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("App preferences").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("System permissions stay under Android control")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun partialFailureStatesWhatWasAlreadyDeleted() {
        composeRule.setContent {
            HeroLogsTheme {
                PrivacyDataScreen(
                    uiState = state(
                        deletionFailure = LocalDataDeletionResult.Failure(
                            failedStage = LocalDataDeletionStage.PREFERENCES,
                            clearedCategories = setOf(
                                LocalDataCategory.TIMELINE_ENTRIES,
                                LocalDataCategory.PLACES,
                            ),
                        ),
                    ),
                    onBack = {},
                    onRequestDeleteAll = {},
                    onConfirmDeleteAll = {},
                    onDismissDeleteConfirmation = {},
                )
            }
        }

        composeRule.onNodeWithText(
            "Timeline entries and places were deleted, but app preferences could not be cleared. " +
                "Try again to finish.",
        ).performScrollTo().assertIsDisplayed()
    }

    private fun deleteAction() = composeRule.onNode(
        hasText("Delete all local data") and hasClickAction(),
    )

    private fun state(
        isDeleteConfirmationVisible: Boolean = false,
        deletionFailure: LocalDataDeletionResult.Failure? = null,
    ) = PrivacyDataUiState(
        storedCategories = listOf(
            LocalDataCategory.TIMELINE_ENTRIES,
            LocalDataCategory.PLACES,
            LocalDataCategory.APP_PREFERENCES,
        ),
        isDeleteConfirmationVisible = isDeleteConfirmationVisible,
        deletionFailure = deletionFailure,
    )
}
