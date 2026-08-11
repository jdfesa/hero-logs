# P4D: Signal Contracts And Retention Decision

Status: `BLOCKED` — human decision required

Branch: `feature/p4d-signal-contracts`

## Required Decision

Before implementation, record an approved ADR answering:

- whether derived place coordinates may be stored;
- whether temporary raw location samples may be stored;
- exact retention and cleanup behavior;
- minimum precision needed for the MVP.

The agent must not choose the ADR outcome.

## Scope After Approval

- Define Android-light location and activity signal models.
- Define source contracts, normalization rules, and deterministic fakes.
- Test validation, time ordering, confidence bounds, and redaction-safe
  diagnostics.

## Out Of Scope

- Platform signal collection.
- Foreground or background tracking.
- Persistence not explicitly authorized by the approved ADR.
