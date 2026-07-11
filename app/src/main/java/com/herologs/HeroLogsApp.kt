package com.herologs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.herologs.designsystem.Ink
import com.herologs.designsystem.Paper
import com.herologs.di.AppContainer
import com.herologs.domain.timeline.CorrectTimelineMovementUseCase
import com.herologs.domain.timeline.ObserveTimelineDayUseCase
import com.herologs.domain.timeline.ObserveTimelineEntryUseCase
import com.herologs.domain.timeline.RenamePlaceUseCase
import com.herologs.domain.timeline.RenameTimelineEntryTitleUseCase
import com.herologs.feature.insights.InsightsScreen
import com.herologs.feature.lifebar.LifeBarRoute
import com.herologs.feature.lifebar.LifeBarViewModel
import com.herologs.feature.onboarding.OnboardingScreen
import com.herologs.feature.onboarding.OnboardingViewModel
import com.herologs.feature.settings.SettingsRoute
import com.herologs.feature.settings.SettingsViewModel
import com.herologs.feature.timeline.TimelineEntryDetailRoute
import com.herologs.feature.timeline.TimelineEntryDetailViewModel
import com.herologs.feature.timeline.TimelineRoute
import com.herologs.feature.timeline.TimelineViewModel

@Composable
fun HeroLogsApp(
    appContainer: AppContainer,
) {
    val onboardingViewModel: OnboardingViewModel = viewModel(
        factory = remember(appContainer) {
            OnboardingViewModel.factory(appContainer.userPreferencesRepository)
        },
    )
    val onboardingState by onboardingViewModel.uiState.collectAsStateWithLifecycle()

    when {
        onboardingState.isLoading -> AppLoading()
        !onboardingState.hasCompletedOnboarding -> OnboardingScreen(
            onContinue = onboardingViewModel::completeOnboarding,
        )
        else -> HeroLogsNavigation(appContainer)
    }
}

@Composable
private fun HeroLogsNavigation(appContainer: AppContainer) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = currentDestination?.route in TopLevelDestination.entries.map { it.route }

    Scaffold(
        containerColor = Paper,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = Paper) {
                    TopLevelDestination.entries.forEach { destination ->
                        val selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Text(destination.symbol) },
                            label = { Text(destination.label) },
                        )
                    }
                }
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = TopLevelDestination.LifeBar.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            composable(TopLevelDestination.LifeBar.route) {
                val factory = remember(appContainer) {
                    LifeBarViewModel.factory(
                        observeTimelineDay = ObserveTimelineDayUseCase(appContainer.timelineRepository),
                    )
                }
                LifeBarRoute(
                    onOpenTimeline = {
                        navController.navigate(TopLevelDestination.Timeline.route) {
                            launchSingleTop = true
                        }
                    },
                    viewModel = viewModel(factory = factory),
                )
            }
            composable(TopLevelDestination.Timeline.route) {
                val factory = remember(appContainer) {
                    TimelineViewModel.factory(
                        observeTimelineDay = ObserveTimelineDayUseCase(appContainer.timelineRepository),
                        seedDemoTimeline = appContainer.seedDemoTimeline,
                    )
                }
                TimelineRoute(
                    onEntryClick = { entryId ->
                        navController.navigate("$TIMELINE_DETAIL_ROUTE/$entryId")
                    },
                    viewModel = viewModel(factory = factory),
                )
            }
            composable(TopLevelDestination.Insights.route) {
                InsightsScreen(
                    onOpenTimeline = {
                        navController.navigate(TopLevelDestination.Timeline.route) {
                            launchSingleTop = true
                        }
                    },
                )
            }
            composable(TopLevelDestination.Settings.route) {
                val factory = remember(appContainer) {
                    SettingsViewModel.factory(appContainer.userPreferencesRepository)
                }
                SettingsRoute(viewModel = viewModel(factory = factory))
            }
            composable("$TIMELINE_DETAIL_ROUTE/{$ENTRY_ID_ARGUMENT}") { entry ->
                val entryId = entry.arguments
                    ?.getString(ENTRY_ID_ARGUMENT)
                    ?.toLongOrNull()
                if (entryId == null || entryId <= 0L) {
                    InvalidEntryRoute(onBack = navController::popBackStack)
                } else {
                    val factory = remember(appContainer, entryId) {
                        TimelineEntryDetailViewModel.factory(
                            entryId = entryId,
                            observeTimelineEntry = ObserveTimelineEntryUseCase(appContainer.timelineRepository),
                            renameTimelineEntryTitle = RenameTimelineEntryTitleUseCase(appContainer.timelineRepository),
                            renamePlace = RenamePlaceUseCase(appContainer.timelineRepository),
                            correctTimelineMovement = CorrectTimelineMovementUseCase(appContainer.timelineRepository),
                        )
                    }
                    TimelineEntryDetailRoute(
                        onBack = navController::popBackStack,
                        viewModel = viewModel(
                            key = "timeline_entry_$entryId",
                            factory = factory,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun AppLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = Ink)
    }
}

@Composable
private fun InvalidEntryRoute(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper),
        contentAlignment = Alignment.Center,
    ) {
        Text("No pudimos abrir este evento.")
    }
}

private enum class TopLevelDestination(
    val route: String,
    val label: String,
    val symbol: String,
) {
    LifeBar(route = "lifebar", label = "LifeBar", symbol = "▥"),
    Timeline(route = "timeline", label = "Timeline", symbol = "◷"),
    Insights(route = "insights", label = "Insights", symbol = "◌"),
    Settings(route = "settings", label = "Ajustes", symbol = "⚙"),
}

private const val TIMELINE_DETAIL_ROUTE = "timeline_entry"
private const val ENTRY_ID_ARGUMENT = "entryId"
