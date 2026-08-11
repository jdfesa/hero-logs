package com.herologs.feature.settings

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.herologs.designsystem.HeroLogsTheme
import com.herologs.domain.permissions.PermissionAccessStatus
import com.herologs.domain.permissions.PermissionCapability
import com.herologs.domain.permissions.PermissionCapabilityState
import com.herologs.domain.permissions.PermissionOverview
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun requestableCapabilitiesEmitEventsOnlyAfterExplicitButtonClicks() {
        val requestedCapabilities = mutableListOf<PermissionCapability>()
        composeRule.setContent {
            HeroLogsTheme {
                SettingsScreen(
                    uiState = settingsState(
                        location = PermissionAccessStatus.NOT_GRANTED,
                        activity = PermissionAccessStatus.NOT_GRANTED,
                    ),
                    onOpenPrivacyData = {},
                    onShowOnboardingAgain = {},
                    onRequestPermission = requestedCapabilities::add,
                )
            }
        }

        assertEquals(emptyList<PermissionCapability>(), requestedCapabilities)

        composeRule.onNodeWithText("Connect foreground location")
            .performScrollTo()
            .performClick()
        composeRule.onNodeWithText("Connect activity recognition")
            .performScrollTo()
            .performClick()

        assertEquals(
            listOf(
                PermissionCapability.FOREGROUND_LOCATION,
                PermissionCapability.ACTIVITY_RECOGNITION,
            ),
            requestedCapabilities,
        )
    }

    @Test
    fun connectedOrUnavailableCapabilitiesDoNotExposeRequestActions() {
        composeRule.setContent {
            HeroLogsTheme {
                SettingsScreen(
                    uiState = settingsState(
                        location = PermissionAccessStatus.GRANTED,
                        activity = PermissionAccessStatus.NOT_REQUIRED,
                    ),
                    onOpenPrivacyData = {},
                    onShowOnboardingAgain = {},
                    onRequestPermission = {},
                )
            }
        }

        assertTrue(
            composeRule.onAllNodesWithText("Connect foreground location")
                .fetchSemanticsNodes()
                .isEmpty(),
        )
        assertTrue(
            composeRule.onAllNodesWithText("Connect activity recognition")
                .fetchSemanticsNodes()
                .isEmpty(),
        )
    }

    @Test
    fun localDataManagementOpensOnlyAfterExplicitClick() {
        var openCount = 0
        composeRule.setContent {
            HeroLogsTheme {
                SettingsScreen(
                    uiState = settingsState(
                        location = PermissionAccessStatus.GRANTED,
                        activity = PermissionAccessStatus.NOT_REQUIRED,
                    ),
                    onOpenPrivacyData = { openCount += 1 },
                    onShowOnboardingAgain = {},
                    onRequestPermission = {},
                )
            }
        }

        assertEquals(0, openCount)

        composeRule.onNodeWithText("Manage local data")
            .performScrollTo()
            .performClick()

        assertEquals(1, openCount)
    }

    private fun settingsState(
        location: PermissionAccessStatus,
        activity: PermissionAccessStatus,
    ) = SettingsUiState(
        permissionOverview = PermissionOverview(
            capabilities = listOf(
                PermissionCapabilityState(PermissionCapability.FOREGROUND_LOCATION, location),
                PermissionCapabilityState(PermissionCapability.ACTIVITY_RECOGNITION, activity),
                PermissionCapabilityState(
                    PermissionCapability.HEALTH_CONNECT,
                    PermissionAccessStatus.NOT_CONFIGURED,
                ),
            ),
        ),
    )
}
