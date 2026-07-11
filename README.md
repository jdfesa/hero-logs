# HeroLogs

HeroLogs is a native Android project for a private, local-first life tracking app.

The goal is to build a personal timeline that quietly reconstructs the user's day from permitted device signals, turns those signals into readable wellbeing dimensions, and keeps the data on the device.

This repository is intended to be public and process-oriented. Documentation is part of the product, not an afterthought.

## Product Direction

HeroLogs is not a classic habit tracker. The app should reduce manual logging and help users understand their day from signals they already generate:

- Places visited and time spent there.
- Trips between places.
- Movement patterns such as walking, driving, cycling, stillness, and transit.
- Health signals such as steps, workouts, and sleep, when the user grants permission.
- Optional check-ins for intention, mood, reflection, and meaning.
- Daily and weekly scoring that explains why each score moved.

## Technical Direction

The Android app should be built as a native Android application using:

- Kotlin.
- Jetpack Compose.
- Material 3.
- Android architecture layers: UI, domain, data.
- Room for structured local persistence.
- DataStore for preferences.
- Health Connect for health and fitness data.
- Google Play services location APIs for location and motion signals.
- WorkManager for periodic/background processing where appropriate.

The first implementation should start as a single `app` module with a clean package structure. Modularization can come later when the boundaries are proven by real code.

## Repository

Public GitHub repository:

https://github.com/jdfesa/hero-logs

## Documentation Map

Start here:

- [Product Vision](docs/00-product-vision.md)
- [Functional Requirements](docs/01-functional-requirements.md)
- [Android Technical Architecture](docs/02-android-technical-architecture.md)
- [Data And Privacy](docs/03-data-and-privacy.md)
- [Permissions Strategy](docs/04-permissions-strategy.md)
- [Scoring Model](docs/05-scoring-model.md)
- [UI Map](docs/06-ui-map.md)
- [Development Roadmap](docs/07-development-roadmap.md)
- [Repo Workflow](docs/08-repo-workflow.md)
- [Open Questions](docs/09-open-questions.md)
- [Glossary](docs/10-glossary.md)
- [Architecture Decision Records](docs/adr/)

## Current Status

The first local-first Android vertical slice is implemented:

- Kotlin + Jetpack Compose with LifeBar, Timeline, Insights, and Settings navigation.
- Room persistence for places and timeline entries.
- DataStore for onboarding state.
- An explicit, local-only demo Timeline that can be corrected in the UI.
- ViewModels backed by `StateFlow`, repository contracts, and focused unit tests.

No location, activity recognition, Health Connect, or notification permissions are requested yet. Scores remain intentionally unavailable until the app has enough explainable signals.

Build the debug APK with:

```bash
./gradlew :app:assembleDebug
```

Run the unit tests with:

```bash
./gradlew :app:testDebugUnitTest
```

For local terminal builds, use Android Studio's bundled JDK or another compatible JDK. The next milestone is integrating real device signals through progressive permission education.

## Guiding Principles

- Privacy first: no account and no cloud dependency for the MVP.
- Local-first data: the device is the source of truth.
- Explainable scores: every metric should have a reason.
- User correction over false certainty: unknown places and uncertain trips should be editable.
- Battery-aware tracking: background collection must be justified and conservative.
- Documentation before complexity: every major technical decision should be recorded.
