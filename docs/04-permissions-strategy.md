# Permissions Strategy

Permissions are part of the product experience. HeroLogs should ask for access slowly, clearly, and only when the value is understandable.

## Permission Principles

- Ask progressively.
- Explain before system dialogs.
- Allow partial use when possible.
- Make privacy state visible.
- Provide correction and deletion controls.
- Never request sensitive access just because it might be useful later.

## Likely Android Permissions And APIs

### Location

Purpose:

- Detect places.
- Detect visits.
- Reconstruct trips.
- Build the daily timeline.

Android considerations:

- Foreground location is easier to explain and request.
- Background location requires a strong product reason.
- Android 11+ requires users to grant background location from settings.
- Approximate location may reduce timeline quality.

MVP approach:

- Start with foreground location during onboarding or first timeline setup.
- Explain background location only after showing why automatic timelines need it.
- Let the user continue without background tracking, with limited functionality.

### Physical Activity Recognition

Purpose:

- Detect walking, cycling, driving, stillness, and activity transitions.
- Improve trip classification.
- Reduce location polling where possible.

MVP approach:

- Request after timeline value is introduced.
- Store classified intervals, not noisy raw events, unless needed for debugging.

### Health Connect

Purpose:

- Read steps.
- Read sleep sessions.
- Read exercise sessions.
- Improve Vitality and Calm scoring.

MVP approach:

- Health Connect should be optional.
- Ask for only the minimum data types needed.
- Explain that availability depends on user device and connected health apps.

### Notifications

Purpose:

- Morning intention prompt.
- Midday pulse prompt.
- Night reflection prompt.
- Weekly recap reminder.

MVP approach:

- Do not request notifications on first launch.
- Request only when the user enables check-ins or recaps.

## Permission Education Screens

Each sensitive permission should have a pre-permission screen with:

- What the app needs.
- Why it needs it.
- What works without it.
- Where data is stored.
- A continue and skip path.

## Degraded Modes

HeroLogs should have clear behavior when permissions are missing:

```text
No location:
  Manual check-ins and health-based summary only.

Foreground location only:
  Timeline may update when app is open, but background day reconstruction is limited.

No activity recognition:
  Trips may be detected by location only, with lower confidence.

No Health Connect:
  Vitality and Calm use available timeline/activity signals only.

No notifications:
  Check-ins are available only when opening the app.
```

## References

- Background location: https://developer.android.com/develop/sensors-and-location/location/permissions/background
- Fused Location Provider: https://developer.android.com/develop/sensors-and-location/location/retrieve-current
- Activity Recognition: https://developers.google.com/android/reference/com/google/android/gms/location/ActivityRecognitionClient
- Health Connect: https://developer.android.com/health-and-fitness/health-connect
