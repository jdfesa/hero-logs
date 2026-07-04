# Android Technical Architecture

HeroLogs should be developed as a native Android app using Kotlin and Jetpack Compose.

## Initial Architecture Choice

Start with a single Android module:

```text
app/
```

Inside that module, organize code by stable boundaries:

```text
com.herologs/
  HeroLogsApp.kt
  MainActivity.kt
  core/
    common/
    database/
    datastore/
    designsystem/
    location/
    activity/
    health/
    permissions/
    scoring/
    timeline/
  feature/
    onboarding/
    lifebar/
    timeline/
    insights/
    checkin/
    weeklyrecap/
    privacy/
    settings/
  navigation/
  di/
```

This keeps the project approachable while preserving future modularization paths.

## Layers

### UI Layer

Responsibilities:

- Compose screens.
- UI state rendering.
- User events.
- ViewModels.
- Navigation routes.

Expected tools:

- Jetpack Compose.
- Material 3.
- Navigation Compose.
- ViewModel.
- StateFlow.

### Domain Layer

Responsibilities:

- Use cases.
- Scoring rules.
- Timeline reconstruction rules.
- Place classification logic.
- Business rules shared by multiple features.

This layer should remain Kotlin-first and Android-light when possible.

### Data Layer

Responsibilities:

- Local database.
- Preferences.
- Device signal data sources.
- Repository implementations.
- Data mappers.

Expected tools:

- Room.
- DataStore.
- Health Connect.
- Fused Location Provider.
- Activity Recognition API.
- WorkManager.

## Dependency Direction

Dependencies should point inward:

```text
UI -> Domain -> Data contracts
Data implementations -> Data contracts
```

Repositories should expose flows or suspend functions. UI should not read directly from Room, Health Connect, location APIs, or DataStore.

## State Management

Use ViewModels to expose screen state:

```text
ViewModel -> StateFlow<UiState>
Screen -> collectAsStateWithLifecycle()
```

Use explicit UI events:

```text
onPlaceRenamed()
onTripTypeCorrected()
onPermissionRequested()
onCheckInSubmitted()
```

## Background Work

Use WorkManager only for deferrable work:

- Daily timeline processing.
- Weekly recap generation.
- Data cleanup.

Use foreground services only if a feature truly needs continuous active tracking and there is a user-visible notification.

## Local Storage

Room should store structured data:

- Timeline entries.
- Places.
- Visits.
- Trips.
- Daily scores.
- Check-ins.
- Weekly recaps.
- Signal snapshots.

DataStore should store small preferences:

- First launch state.
- Permission education state.
- Theme.
- User settings.
- Feature flags.

## Testing Strategy

Start with focused tests:

- Unit tests for scoring rules.
- Unit tests for timeline reconstruction.
- Repository tests with fake data sources.
- Compose UI tests for core screens.

Add instrumentation tests when permissions and Android framework behavior become important.

## Tooling

Recommended project tooling:

- Kotlin.
- Gradle Kotlin DSL.
- Version catalog.
- ktlint or Spotless.
- detekt.
- GitHub Actions for build and tests.

## References

- Android Kotlin: https://developer.android.com/kotlin
- Jetpack Compose: https://developer.android.com/compose
- App architecture: https://developer.android.com/topic/architecture
- Room: https://developer.android.com/training/data-storage/room
- WorkManager: https://developer.android.com/topic/libraries/architecture/workmanager
