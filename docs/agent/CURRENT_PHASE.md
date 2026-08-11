# P4A: Progressive Permission Education

Status: `READY`

Branch: `feature/p4a-permission-education`

## Objective

Turn the current single-page welcome into a small, testable onboarding flow that
explains HeroLogs, its local-first promise, and the value of future optional
signals before any Android permission dialog exists.

This is an education phase. It must not request a system permission.

## User Story

As a first-time user, I can move through a concise Spanish onboarding flow,
understand that my data stays on the device, see which optional signals may be
offered later, and start using the local demo without granting access.

## Required Sources

Read before planning:

- `docs/00-product-vision.md`
- `docs/01-functional-requirements.md`, especially Onboarding and Permission Setup
- `docs/03-data-and-privacy.md`
- `docs/04-permissions-strategy.md`
- `docs/06-ui-map.md`, especially Onboarding and Accessibility
- Existing onboarding, DataStore, theme, navigation, and related tests

## In Scope

- A deterministic multi-step onboarding state model owned by
  `OnboardingViewModel`.
- Four concise steps:
  1. HeroLogs value proposition.
  2. Local-first privacy promise.
  3. Preview of optional location, activity, and health signals with a clear
     statement that they are not requested yet.
  4. Ready state with **Empezar sin permisos**.
- Back and next events with bounded transitions; back on the first step is a
  no-op.
- Completion is persisted only from the final step.
- Loading and persistence-in-progress states prevent duplicate completion.
- Unit tests for step transitions, bounds, and completion behavior.
- Update UI map or functional docs only where implemented behavior becomes more
  precise.
- New user-facing copy goes in string resources.

## Out Of Scope

- Manifest permission declarations.
- Android runtime permission launchers or settings intents.
- Location, Activity Recognition, Health Connect, or notification dependencies.
- Persisting individual onboarding step progress.
- Permission status models or a Settings permission dashboard; those are `P4B`.
- Navigation refactors outside what the onboarding flow strictly needs.
- Broad string-resource migration, theme redesign, animations, or screenshots.

## Design Constraints

- Keep `OnboardingScreen` stateless: render an immutable UI state and emit
  explicit events.
- Keep state transition logic outside composables and directly unit-testable.
- Process death may return the user to onboarding step one until onboarding is
  complete; that is acceptable in this phase.
- Declining future permissions must not be framed as an error or degraded moral
  choice.
- Copy must not imply that a permission or automatic Timeline already works.
- Retain the existing local demo path after onboarding.
- Provide accessible labels for controls and do not rely on color alone.

## Suggested Boundaries

These are guidance, not a requirement to change every file:

```text
app/src/main/java/com/herologs/feature/onboarding/
app/src/main/res/values/strings.xml
app/src/test/java/com/herologs/feature/onboarding/
docs/01-functional-requirements.md
docs/06-ui-map.md
docs/agent/PROJECT_STATE.md
```

Do not move onboarding into a new Gradle module.

## Acceptance Criteria

- A fresh install starts at step one and exposes a clear progress indication.
- Next and back produce the expected bounded sequence.
- Only the final step can persist onboarding completion.
- Repeated taps while completion is being saved do not launch duplicate writes.
- Completing onboarding opens the existing app shell.
- Resetting onboarding from Settings returns to step one.
- All copy is accurate for the current no-permission implementation.
- No sensitive permission or new device-signal dependency appears in the diff.
- New state logic has focused passing unit tests.
- `./scripts/verify.sh full` succeeds.

## Manual Check

If an emulator or device is available:

1. Clear app data and launch.
2. Traverse forward and backward through all steps.
3. Finish onboarding and confirm the app shell opens.
4. In Settings choose **Mostrar bienvenida** and confirm step one returns.
5. Rotate or recreate the activity and record the observed behavior.

If no emulator is available, report this manual check as pending.

## Delivery

Use small Conventional Commits in English. A good split is:

```text
feat: add progressive onboarding education
test: cover onboarding step transitions
docs: document permission education flow
```

Combining closely coupled test changes with the feature commit is acceptable.
After successful verification, push `feature/p4a-permission-education` and stop.
