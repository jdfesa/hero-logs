# Agent Execution Plan To HeroLogs MVP

This plan decomposes the existing high-level roadmap into reviewable vertical
slices for a smaller coding model. Only the phase named in
`PROJECT_STATE.md` may be implemented.

## Status Vocabulary

- `PLANNED`: described but not authorized.
- `READY`: prerequisites are satisfied and the phase may start.
- `IN_PROGRESS`: implementation has started on its branch.
- `BLOCKED`: a named decision or external prerequisite is missing.
- `COMPLETE`: acceptance criteria and verification passed.

## Global Phase Exit Criteria

Every code phase must:

- leave the app runnable;
- include focused tests for changed logic;
- run `./scripts/verify.sh full`;
- update affected product docs and `PROJECT_STATE.md`;
- use English Conventional Commits on the named branch;
- stop before the next phase.

## P4A — Progressive Permission Education

Status: `COMPLETE`

Branch: `feature/p4a-permission-education`

Deliver a testable multi-step onboarding flow that explains value, local
privacy, and future optional signals. Do not declare or request Android
permissions. The full authorized scope is in `CURRENT_PHASE.md`.

## P4B — Permission Capability And Status

Status: `COMPLETE`

Branch: `feature/p4b-permission-status`

Add domain-level capability/status models and an Android implementation that can
read current foreground-location and activity-recognition permission state.
Render accurate status in Settings and connect requests only to explicit user
actions after education. Keep Health Connect as unavailable/not configured. Add
unit tests for state mapping and instrumentation tests where framework behavior
requires them.

Do not collect or persist any signal. Do not request background location.

## P4C — Local Data Controls

Status: `COMPLETE`

Branch: `feature/p4c-local-data-controls`

Before collecting sensitive signals, add a Privacy/Data screen that accurately
lists stored categories and provides a confirmed **delete all local HeroLogs
data** flow. Deletion must cover Room data and relevant DataStore preferences
without relying on uninstall. Add repository/use-case tests and update privacy
docs.

Do not add export, encryption, cloud backup, or selective deletion.

## P4D — Signal Contracts And Retention Decision

Status: `BLOCKED`, human gate required

Branch: `feature/p4d-signal-contracts`

First record an ADR answering:

- whether derived place coordinates may be stored;
- whether temporary raw location samples may be stored;
- exact retention and cleanup behavior;
- minimum precision needed for the MVP.

After approval, define Android-light location/activity signal models, source
contracts, normalization rules, and deterministic fakes. Implement no platform
collection yet. Test validation, time ordering, confidence bounds, and
redaction-safe diagnostics.

The agent must not choose the ADR outcome.

## P4E — Foreground Location Vertical Slice

Status: `PLANNED`

Branch: `feature/p4e-foreground-location`

Add the minimum official Google Play services location dependency and collect a
single foreground sample only after an explicit educated user action. Convert it
through the `P4D` contract and show an honest success/unavailable/error state.
Persist only what the approved ADR permits. Handle approximate location,
denial, permanent denial, provider disabled, timeout, and cancellation.

Do not request background location, continuously track, or reconstruct a trip.

## P4F — Activity Recognition Vertical Slice

Status: `PLANNED`

Branch: `feature/p4f-activity-recognition`

Add Activity Recognition behind its own education and explicit permission
action. Normalize a small supported set: still, walking, running, cycling,
in-vehicle, and unknown. Treat confidence conservatively and add fake-driven
tests.

Do not infer visits, write scores, or increase location frequency.

## P4G — Battery-Aware Background Collection

Status: `PLANNED`, with a human gate

Branch: `feature/p4g-background-collection`

Before implementation, approve an ADR for the foreground/background product
behavior, battery budget, notification implications, and degraded mode.
Implement the smallest policy-compliant collection path using official Android
APIs. Make collection user-visible, revocable, idempotent, and resilient to
reboots or revoked permissions as required by the approved design.

Do not use WorkManager for continuous tracking or hide a foreground service.

## P4H — Timeline Reconstruction From Signals

Status: `PLANNED`

Branch: `feature/p4h-timeline-reconstruction`

Build a deterministic domain engine that converts normalized, permitted signals
into candidate visits/trips with confidence and provenance. Preserve all
user-edited entries, expose uncertain items for correction, and keep the
existing demo mode. Use fixtures and boundary-focused unit tests before wiring
Room persistence.

Do not score the day or silently overwrite corrections.

## P4I — Optional Health Connect

Status: `PLANNED`, release-blocker decision required

Branch: `feature/p4i-health-connect`

Decide whether Health Connect is required for the first MVP release. If
approved, add availability detection, education, minimal read permissions, and
summary-only ingestion for the explicitly selected record types. Handle
unavailable, outdated, denied, and partial-access states.

Do not store unnecessary raw health records or make medical interpretations.

## P5A — Explainable Scoring Domain

Status: `PLANNED`, with a human gate

Branch: `feature/p5a-scoring-domain`

Approve baseline duration, provisional formula, missing-data behavior, and score
suppression threshold. Then implement pure Kotlin score inputs, dimensions,
confidence, reasons, and deterministic calculations. Tests must cover sparse
data, boundaries, corrections, no-signal days, and reason traceability.

Do not build score UI or claim research validation.

## P5B — Score Persistence And LifeBar

Status: `PLANNED`

Branch: `feature/p5b-lifebar`

Persist versioned daily score results and reasons with an explicit Room
migration. Replace LifeBar placeholders with honest loading, unavailable,
low-confidence, and explained-score states. Let users inspect reasons and
missing signals. Add migration, ViewModel, and UI-state tests.

Do not recalculate historical scores under a new formula without a versioned
policy.

## P6A — Optional Check-Ins

Status: `PLANNED`

Branch: `feature/p6a-check-ins`

Add local morning intention, midday pulse, and night reflection inputs without
notifications first. Treat text as sensitive, allow skip/edit/delete, and keep
scoring influence disabled until separately specified. Add storage migration,
repository/use-case tests, and privacy documentation.

## P6B — Weekly Recap

Status: `PLANNED`, release-blocker decision required

Branch: `feature/p6b-weekly-recap`

Decide whether recap is required for the first MVP. If approved, generate a
deterministic local recap from persisted timeline and scores, include missing
data/confidence, compare only compatible periods, and provide empty/partial
states. Notifications remain a separate opt-in.

## P7 — Public Alpha Readiness

Status: `PLANNED`

Branch: `feature/p7-public-alpha`

Run the release checklist: permission revocation and degraded modes, deletion,
database migrations, accessibility, battery observations, sensitive-log audit,
offline behavior, screenshots, privacy copy, and release notes. Resolve or
explicitly defer every MVP-blocking open question.

No public release is complete solely because the debug build succeeds.
