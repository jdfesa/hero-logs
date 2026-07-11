# Data And Privacy

HeroLogs should be local-first. The MVP should not require an account, backend, or cloud sync.

## Privacy Position

The product promise:

```text
Your day is reconstructed on your device. Your data stays there.
```

This should guide architecture, onboarding copy, permissions, and future feature decisions.

## Data Categories

### Location Data

Possible data:

- Latitude and longitude samples.
- Derived places.
- Visit start and end times.
- Trip start and end times.
- Movement type.
- Confidence values.

Storage guidance:

- Prefer derived places and visits over raw high-frequency coordinates.
- Keep raw samples only as long as needed for reconstruction.
- Provide deletion controls before expanding retention.

### Activity Data

Possible data:

- Still.
- Walking.
- Running.
- Cycling.
- In vehicle.
- Unknown.

Storage guidance:

- Store time windows and confidence, not every raw event unless needed.

### Health Data

Possible data through Health Connect:

- Steps.
- Sleep sessions.
- Exercise sessions.
- Heart rate eventually, if justified.

Storage guidance:

- Request the smallest useful permission set.
- Store summaries for scoring where possible.
- Avoid medical interpretation.

### User Input

Possible data:

- Place labels.
- Place categories.
- Timeline corrections.
- Check-ins.
- Reflections.
- Settings.

Storage guidance:

- Treat written reflections as sensitive.
- Keep user-generated text local.

## Local Database Model

Initial conceptual entities:

```text
Place
  id
  name
  category
  latitude
  longitude
  radiusMeters
  createdAt
  updatedAt

Visit
  id
  placeId
  startedAt
  endedAt
  confidence
  source

Trip
  id
  startedAt
  endedAt
  fromPlaceId
  toPlaceId
  movementType
  distanceMeters
  confidence

TimelineEntry
  id
  type
  startedAt
  endedAt
  title
  subtitle
  confidence

DailyScore
  date
  lifeScore
  vitality
  joy
  drive
  calm
  connection
  meaning
  xpEarned

ScoreReason
  id
  date
  dimension
  reason
  impact
  source

CheckIn
  id
  type
  occurredAt
  prompt
  response

WeeklyRecap
  weekStartDate
  summary
  generatedAt
```

This model should evolve once real Android implementation begins.

## Implemented MVP Storage

The first Android implementation stores only the minimum data needed for a local, editable Timeline:

- `Place`: name, category, and creation/update timestamps. Coordinates are not stored.
- `TimelineEntry`: day, type, time range, optional linked place, optional movement type, title, subtitle, confidence, and whether the user edited it.
- DataStore: onboarding-completion state only.

Demo data is inserted only when the user chooses **Cargar día de ejemplo**. It is local to the device, does not request a permission, and is never inserted automatically at launch.

The Room schema is exported with the source under `app/schemas/` so later schema migrations can be reviewed and tested.

## Retention Policy

Draft MVP policy:

- Derived timeline data is kept until user deletion.
- Temporary raw samples may be deleted after daily processing.
- Check-ins are kept until user deletion.
- Weekly recaps are kept until user deletion.

Before shipping, the app needs a visible data deletion path.

## Security Notes

- Do not log sensitive location, health, or check-in data.
- Avoid analytics in the MVP.
- Avoid crash reports containing sensitive local data.
- Use Android Keystore if future encrypted secrets are needed.
- Consider SQLCipher or Android encrypted storage only after evaluating complexity and performance.

## Open Questions

- Should raw location samples ever be persisted?
- How long should temporary signal data be retained?
- Should export be available before cloud backup?
- Should the app support full local database encryption from the first public build?
