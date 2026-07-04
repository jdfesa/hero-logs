# ADR 0001: Build HeroLogs As A Native Android App With Kotlin And Compose

## Status

Accepted.

## Context

HeroLogs is intended to be a native Android life tracking app.

The app needs deep Android integration:

- Location permissions.
- Background location behavior.
- Activity recognition.
- Health Connect.
- Local storage.
- Battery-aware background work.
- Native privacy and permission UX.

Cross-platform UI would not remove the need for Android-specific signal handling.

## Decision

Build HeroLogs as a native Android app using:

- Kotlin.
- Jetpack Compose.
- Material 3.
- Android Jetpack architecture components.

Start with one Android app module and organize code by clear package boundaries. Consider multi-module architecture only after the codebase has real feature boundaries.

## Consequences

Positive:

- Best fit for Android permissions and background behavior.
- Strong Compose support.
- Easier access to Health Connect and Google Play services APIs.
- Clear path for public Android best practices.

Negative:

- Android implementation will not share UI code with other platforms.
- Requires learning Android-specific lifecycle, permissions, and background constraints.
- Some features will need careful device testing.

## Alternatives Considered

### React Native

Rejected for MVP because the core risk is native signal collection and background behavior, not UI speed.

### Flutter

Rejected for MVP for the same reason. Flutter could build a polished UI, but native Android integration would still be central.

### Kotlin Multiplatform

Deferred. It may be useful later for shared scoring/domain logic, but it adds project complexity before the Android MVP exists.

## References

- Kotlin on Android: https://developer.android.com/kotlin
- Jetpack Compose: https://developer.android.com/compose
- Android app architecture: https://developer.android.com/topic/architecture
