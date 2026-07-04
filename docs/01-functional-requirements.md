# Functional Requirements

This document describes what HeroLogs should do from a product perspective.

## MVP Scope

### Onboarding

The app should introduce:

- What HeroLogs does.
- Which permissions are optional or required.
- Why each permission is requested.
- That data remains on the device for the MVP.

The user should be able to continue with limited functionality if they decline non-essential permissions.

### Permission Setup

The app should request permissions progressively, close to the moment of value:

- Location for place and trip detection.
- Background location only after the user understands why the timeline needs it.
- Physical activity recognition for movement classification.
- Health Connect for steps, sleep, workouts, and related signals.
- Notifications only if check-ins or recaps require them.

### Daily Timeline

The timeline should show:

- Time blocks.
- Places visited.
- Unknown places.
- Trips between places.
- Movement type when available.
- Duration.
- Confidence or uncertainty where useful.

The user should be able to:

- Rename or classify a place.
- Correct movement type.
- Merge or split timeline items eventually.
- Mark an item as ignored eventually.

### LifeBar

The LifeBar should show daily dimensions:

- Vitality.
- Joy.
- Drive.
- Calm.
- Connection.
- Meaning.

The UI should show:

- Current score for each dimension.
- Direction of change against baseline or previous day.
- A composite Life Score.
- XP earned today.
- Current level.

### Score Explanation

Each score should have a details screen explaining:

- Which signals contributed.
- Which signals were missing.
- Whether the value is compared to personal baseline.
- What the app is uncertain about.

The app should avoid pretending to know more than the data supports.

### Optional Check-Ins

The user may answer short prompts:

- Morning intention.
- Midday pulse.
- Night reflection.

Check-ins should refine meaning and emotional context, but not be mandatory.

### Weekly Recap

The weekly recap should summarize:

- Score trends.
- Most common places.
- Time distribution.
- Movement/activity summary.
- XP earned.
- Notable changes compared with previous week.

### Privacy Screen

The privacy screen should clearly state:

- No account for MVP.
- No cloud upload for MVP.
- Local database on device.
- Permissions can be revoked.
- Deleting app data removes local history.

## Future Scope

- Calendar integration.
- Notes integration.
- Export data.
- Backup and restore.
- Local AI classification.
- Wear OS companion.
- Widgets.
- Advanced charts.
- Custom dimensions.

## Functional Risks

- Background location permissions are difficult to obtain and must be justified.
- Battery usage can damage trust if tracking is too aggressive.
- Health data availability depends on device, installed apps, and user permissions.
- Score formulas can feel arbitrary unless transparent.
- Too many prompts would make the app feel like a journal instead of automatic tracking.
