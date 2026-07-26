# P4B: Permission Capability And Status

Status: `READY`

Branch: `feature/p4b-permission-status`

## Objective

Add domain-level capability and status models and an Android implementation that can read current foreground-location and activity-recognition permission state. Render accurate status in Settings and connect requests only to explicit user actions after education.

## Scope

- Domain-level permission capability and status models.
- Android platform implementations for checking location and activity recognition permission state.
- Expose permission status to Settings.
- Keep Health Connect as unavailable / not configured.
- Unit tests for permission state mapping.

## Out Of Scope

- Collecting or persisting location/activity signals.
- Background location requests.
