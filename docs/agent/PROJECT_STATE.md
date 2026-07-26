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
- Compose navigation includes LifeBar, Timeline, Insights, and Settings.
- Room stores places and editable Timeline entries; schema version is 1.
- DataStore stores onboarding completion.
- Demo Timeline insertion is explicit, local, and correctable.
- No location, activity recognition, Health Connect, notification, account,
  cloud, analytics, or score calculation exists.

Baseline verification on 2026-07-26:

```text
./gradlew :app:testDebugUnitTest
PASS

./gradlew --no-daemon --max-workers=2 :app:assembleDebug
PASS
```

## Active Phase

- Phase: `P4A`
- Name: Progressive permission education
- Status: `READY`
- Branch: `feature/p4a-permission-education`
- Brief: `docs/agent/CURRENT_PHASE.md`

Only `P4A` is authorized. Do not begin `P4B` in the same branch or conversation.

## Known Human Gates

- Before persistent location work: decide whether derived place coordinates may
  be stored and how long temporary raw samples may exist.
- Before background collection: approve the foreground/background tracking
  product behavior and battery budget.
- Before scoring: approve baseline duration, provisional formula, and when low
  confidence suppresses a score.
- Before public alpha: decide whether Health Connect and weekly recap are MVP
  release blockers.

These are future gates. They do not block `P4A`.

## State Update Rules

When the active phase finishes:

1. Record `COMPLETE` only after its full verification passes.
2. Record the branch, commit, verification evidence, and any manual check.
3. Copy the next phase's scope from `PHASE_PLAN.md` into
   `docs/agent/CURRENT_PHASE.md`; do not expand it.
4. Change the active phase pointer only after confirming prerequisites and human
   gates.
5. Stop without implementing the next phase.
