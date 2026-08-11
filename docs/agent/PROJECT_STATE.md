# HeroLogs Agent State

Keep this file short. Replace stale values; do not append a session diary.

## Repository Baseline

- Last verified: 2026-08-11
- Base branch: `main`
- Baseline commit: `5284787` (merge of P4B completion state)
- Android package: `com.herologs`
- Minimum SDK: 26
- Target and compile SDK: 36
- Architecture: single `app` module with UI, domain, data, and core boundaries

## Implemented And Verified

- Phases 0–3 from `docs/07-development-roadmap.md` are complete for the initial
  local Timeline slice.
- Phase `P4A` (Progressive Permission Education) is complete:
  - 4-step onboarding state model in `OnboardingViewModel`.
  - Multi-step onboarding UI in `OnboardingScreen` with step indicator and Spanish `es-AR` resources.
  - Unit tests covering step transitions, bounds, and guarded completion.
  - Verified with `./scripts/verify.sh full` (Tests + assembleDebug PASS).
- Phase `P4B` (Permission Capability And Status) is complete:
  - Android-light capability and access-status models with deterministic mapping.
  - Foreground-location and activity-recognition status reader behind a domain
    contract and the manual `AppContainer`.
  - Scrollable Settings cards with precise, approximate, missing, legacy, and
    unconfigured states.
  - Explicit foreground-location and activity-recognition request actions.
  - JVM mapping/state tests and Compose instrumentation coverage.
  - Delivered through PRs #3–#8; latest verified code merge: `4773eea`.
- Compose navigation includes LifeBar, Timeline, Insights, and Settings.
- Room stores places and editable Timeline entries; schema version is 1.
- DataStore stores onboarding completion.
- Demo Timeline insertion is explicit, local, and correctable.
- No location/activity signal collection, Health Connect integration,
  notification, account, cloud, analytics, or score calculation exists.

Full verification on 2026-08-11:

```text
./scripts/verify.sh full
PASS (testDebugUnitTest + assembleDebug + assembleDebugAndroidTest)
```

Pending manual checks: run the Compose instrumentation tests and exercise both
system permission dialogs on an Android device or emulator.

## Active Phase

- Phase: `P4C`
- Name: Local Data Controls
- Status: `IN_PROGRESS`
- Branch: `feature/p4c-local-data-repository`
- Brief: `docs/agent/CURRENT_PHASE.md`

Last completed phase: `P4B`.

Only `P4C` is authorized. Do not begin `P4D` in this branch or conversation.

## Known Human Gates

- Before persistent location work: decide whether derived place coordinates may
  be stored and how long temporary raw samples may exist.
- Before background collection: approve the foreground/background tracking
  product behavior and battery budget.
- Before scoring: approve baseline duration, provisional formula, and when low
  confidence suppresses a score.
- Before public alpha: decide whether Health Connect and weekly recap are MVP
  release blockers.

These are future gates. They do not block `P4C`.

## State Update Rules

When the active phase finishes:

1. Record `COMPLETE` only after its full verification passes.
2. Record the branch, commit, verification evidence, and any manual check.
3. Copy the next phase's scope from `PHASE_PLAN.md` into
   `docs/agent/CURRENT_PHASE.md`; do not expand it.
4. Change the active phase pointer only after confirming prerequisites and human
   gates.
5. Stop without implementing the next phase.
