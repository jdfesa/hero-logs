# HeroLogs Agent State

Keep this file short. Replace stale values; do not append a session diary.

## Repository Baseline

- Last verified: 2026-07-26
- Base branch: `main`
- Baseline commit: `8d42ffc` (`docs: document local timeline MVP`)
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
- Compose navigation includes LifeBar, Timeline, Insights, and Settings.
- Room stores places and editable Timeline entries; schema version is 1.
- DataStore stores onboarding completion.
- Demo Timeline insertion is explicit, local, and correctable.
- No location, activity recognition, Health Connect, notification, account,
  cloud, analytics, or score calculation exists.

Baseline verification on 2026-07-26:

```text
./scripts/verify.sh full
PASS (testDebugUnitTest + assembleDebug)
```

## Active Phase

- Phase: `P4B`
- Name: Permission Capability And Status
- Status: `IN_PROGRESS`
- Branch: `test/p4b-permission-ui`
- Brief: `docs/agent/CURRENT_PHASE.md`

Current P4B delivery:

- Pure Kotlin capability, access-status, and grant-snapshot models.
- Deterministic mapping for precise/approximate location, runtime activity
  recognition, legacy Android, and unconfigured Health Connect.
- Android permission-status reader wired through the manual `AppContainer`.
- Foreground location and activity-recognition manifest declarations.
- Immutable Settings state and truthful status cards for location, activity
  recognition, and unconfigured Health Connect.
- Foreground location and activity-recognition requests launch only from
  explicit Settings actions and refresh status after the system result.
- Compose instrumentation coverage verifies that request events require button
  clicks and disappear for connected or unavailable capabilities.
- Product, architecture, privacy, permission, UI, and roadmap docs match the
  implemented behavior.
- No background request, signal collection, or persistence.

Only `P4B` is authorized. Do not begin `P4C` in this branch or conversation.

## Known Human Gates

- Before persistent location work: decide whether derived place coordinates may
  be stored and how long temporary raw samples may exist.
- Before background collection: approve the foreground/background tracking
  product behavior and battery budget.
- Before scoring: approve baseline duration, provisional formula, and when low
  confidence suppresses a score.
- Before public alpha: decide whether Health Connect and weekly recap are MVP
  release blockers.

These are future gates. They do not block `P4B`.

## State Update Rules

When the active phase finishes:

1. Record `COMPLETE` only after its full verification passes.
2. Record the branch, commit, verification evidence, and any manual check.
3. Copy the next phase's scope from `PHASE_PLAN.md` into
   `docs/agent/CURRENT_PHASE.md`; do not expand it.
4. Change the active phase pointer only after confirming prerequisites and human
   gates.
5. Stop without implementing the next phase.
