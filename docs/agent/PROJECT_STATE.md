# HeroLogs Agent State

Keep this file short. Replace stale values; do not append a session diary.

## Repository Baseline

- Last verified: 2026-08-12
- Base branch: `main`
- Baseline commit: `05f23e9` (merge of P4C-QA retry coverage)
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
- Phase `P4C` (Local Data Controls) is complete:
  - Android-light storage inventory and deletion result contracts.
  - Transactional Room clearing plus retry-safe DataStore clearing.
  - Truthful database and partial-preferences failure semantics.
  - Confirmed Privacy & Data screen reachable from Settings.
  - JVM, in-memory Room, and Compose coverage for core deletion behavior.
  - Delivered through PRs #9–#14; verified code commit: `21de170`.
- Phase `P4C-QA` (Local Data Controls Quality Follow-Up) is complete:
  - Repository and ViewModel tests prove recovery after a partial preferences
    deletion failure.
  - Deletion progress is visible as text, and progress/final results use polite
    accessibility live regions.
  - Delivered through PRs #15–#16; verified accessibility commit: `adad7c9`.
- Compose navigation includes LifeBar, Timeline, Insights, Settings, and the
  Privacy & Data detail route.
- Room stores places and editable Timeline entries; schema version is 1.
- DataStore stores onboarding completion.
- Demo Timeline insertion is explicit, local, and correctable.
- No location/activity signal collection, Health Connect integration,
  notification, account, cloud, analytics, or score calculation exists.

Full verification on 2026-08-12:

```text
./scripts/verify.sh full
PASS (testDebugUnitTest + assembleDebug + assembleDebugAndroidTest)
```

Pending manual checks: run the Room and Compose instrumentation tests, exercise
the delete-all flow, and exercise both system permission dialogs on an Android
device or emulator.

## Active Phase

- Phase: none authorized
- Status: `BLOCKED`
- Next candidate: `P4D` (Signal Contracts And Retention Decision)
- Candidate branch: `feature/p4d-signal-contracts`
- Blocked brief: `docs/agent/CURRENT_PHASE.md`

Last completed phase: `P4C-QA`.

Do not begin `P4D` until its retention and precision ADR receives a human
decision.

## Known Human Gates

- `P4D` is blocked until a human decides whether derived place coordinates and
  temporary raw samples may be stored, their exact retention/cleanup behavior,
  and the minimum precision needed for the MVP.
- Before background collection: approve the foreground/background tracking
  product behavior and battery budget.
- Before scoring: approve baseline duration, provisional formula, and when low
  confidence suppresses a score.
- Before public alpha: decide whether Health Connect and weekly recap are MVP
  release blockers.

The first gate blocks the next implementation phase. The others remain future
gates.

## State Update Rules

When the active phase finishes:

1. Record `COMPLETE` only after its full verification passes.
2. Record the branch, commit, verification evidence, and any manual check.
3. Copy the next phase's scope from `PHASE_PLAN.md` into
   `docs/agent/CURRENT_PHASE.md`; do not expand it.
4. Change the active phase pointer only after confirming prerequisites and human
   gates.
5. Stop without implementing the next phase.
