# HeroLogs Repository Instructions

These instructions apply to the entire repository. They are written for coding
agents, including Google Antigravity and Gemini Flash.

## Instruction Order

Follow instructions in this order:

1. The user's current request.
2. This `AGENTS.md`.
3. The active phase brief linked from `docs/agent/PROJECT_STATE.md`.
4. Product and engineering documentation under `docs/`.

If two sources conflict, stop and report the conflict. Do not silently choose a
new product direction.

## Required Preflight

Before editing:

1. Inspect `git status --short --branch` and the recent Git history.
2. Read `docs/agent/PROJECT_STATE.md`.
3. Read only the active phase brief linked from that state file.
4. Read the product documents explicitly listed by that phase brief.
5. Inspect the affected implementation and tests before proposing changes.
6. State a short plan naming the files or boundaries that will change.

Preserve all existing user changes. Never discard, overwrite, stash, or reformat
unrelated work.

## One Phase At A Time

- Implement only the phase whose status is `READY` or `IN_PROGRESS` in
  `docs/agent/PROJECT_STATE.md`.
- A phase is the maximum scope, not a request to change every allowed file.
- Do not start the following phase in the same conversation or branch.
- Do not add speculative abstractions, dependencies, screens, permissions, or
  database fields for later phases.
- If the active phase has a human decision gate, stop at that gate and ask for
  the decision. Never invent privacy, permission, retention, or scoring policy.
- If blocked, leave the app runnable, record the exact blocker in
  `docs/agent/PROJECT_STATE.md`, and do not mark the phase complete.

## Product Invariants

- HeroLogs is a private, local-first Android app.
- The MVP has no account, backend, cloud sync, analytics, ads, or remote AI.
- Sensitive permissions are progressive, optional where possible, and preceded
  by an explanation of user value.
- Missing data reduces confidence; it must never produce invented certainty.
- Scores must be explainable and must not make medical claims.
- User corrections take precedence over inferred data.
- Do not log location, health data, reflections, or other sensitive content.
- Do not persist raw location or health data unless the active phase and privacy
  documentation explicitly authorize it.

## Architecture And Code Rules

- Keep the single `app` module until real boundaries justify modularization.
- Maintain the package boundaries documented in
  `docs/02-android-technical-architecture.md`.
- UI code renders state and emits events. It must not access Room, DataStore,
  location APIs, Health Connect, or other device sources directly.
- ViewModels expose immutable `StateFlow` UI state and receive dependencies
  through constructors.
- Domain code contains business rules and should remain Android-light.
- Repository contracts live at the domain boundary; implementations live in
  data or core infrastructure packages.
- Continue using the small manual `AppContainer`. Do not introduce a DI
  framework without an approved ADR.
- Add dependencies through `gradle/libs.versions.toml`; do not perform unrelated
  version upgrades.
- For Room changes, add explicit migrations, keep exported schemas under
  `app/schemas/`, and add migration tests. Never use destructive migration.
- Keep time-dependent logic deterministic by injecting `Clock` or an equivalent
  testable source.
- New identifiers, code comments, docs, branch names, and commit messages are in
  English. User-facing copy remains Spanish (`es-AR`) unless a phase says
  otherwise.
- Put new user-facing strings in Android string resources. Do not rewrite all
  existing strings merely to satisfy this rule.
- Prefer focused files and functions. Split a component when it has more than
  one reason to change; do not create empty layers or one-use abstractions.
- Comment decisions and non-obvious constraints, not syntax.

## Testing And Verification

- Add or update tests for changed business logic, state transitions, mappers,
  persistence behavior, and failure paths.
- Run `./scripts/verify.sh quick` while iterating.
- Run `./scripts/verify.sh full` before completing any code phase.
- Documentation-only changes may run `./scripts/verify.sh docs`.
- Never claim a check passed unless it was executed successfully in the current
  worktree.
- If an Android emulator or device is unavailable, say which manual or
  instrumentation verification remains; do not pretend it ran.
- Review `git diff --check`, the complete diff, and `git status` before commit.

## Documentation And Memory

Update documentation in the same phase when behavior changes:

- Product behavior: `docs/01-functional-requirements.md`
- Architecture: `docs/02-android-technical-architecture.md` or a new ADR
- Stored data or retention: `docs/03-data-and-privacy.md`
- Permissions: `docs/04-permissions-strategy.md`
- Scores or confidence: `docs/05-scoring-model.md`
- Navigation or visible screens: `docs/06-ui-map.md`
- Delivery status: `docs/07-development-roadmap.md`

At phase completion, update `docs/agent/PROJECT_STATE.md` with facts only:

- phase status and last completed phase;
- verification commands and outcomes;
- branch and final commit hash if available;
- remaining blockers or manual checks;
- the next phase pointer, but only if its prerequisites are satisfied.

Replace stale state instead of appending a long diary. Git history is the log.

## Git Delivery

- Start each implementation phase from an up-to-date `main` on the branch named
  in the active phase brief. If already on the correct branch, continue there.
- Never commit implementation work directly to `main`.
- Make small, cohesive Conventional Commits in English:
  `feat:`, `fix:`, `refactor:`, `test:`, `docs:`, `build:`, `ci:`, or `chore:`.
- Separate preparation/refactoring from behavior changes when that improves
  reviewability. Every commit must leave the project coherent.
- Do not amend published commits, force-push, rewrite history, or bypass hooks.
- Do not commit secrets, local SDK paths, generated build outputs, crash dumps,
  or files under `tmp/`.
- After full verification, push the phase branch when delivery is part of the
  user's request. Never push a failing or incomplete phase as complete.

## Completion Report

Report:

1. Outcome and user-visible behavior.
2. Important implementation and documentation files.
3. Exact verification run and result.
4. Branch, commits, and push status.
5. Remaining manual checks, risks, or blockers.
