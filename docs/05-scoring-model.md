# Scoring Model

HeroLogs should make wellbeing scores understandable and humble. Scores are not medical claims. They are personal summaries based on available signals.

## Scoring Principles

- Scores should compare the user mostly against their own baseline.
- Missing data should reduce confidence, not invent certainty.
- Each score should provide reasons.
- User corrections should improve future scoring.
- No score should shame the user.

## Dimensions

### Vitality

Possible signals:

- Steps.
- Exercise.
- Sleep duration.
- Sleep consistency.
- Active movement.
- Long sedentary blocks.

Possible sources:

- Health Connect.
- Activity Recognition.
- Timeline.

### Joy

Possible signals:

- Outdoor time.
- Variety of places.
- Leisure places.
- Optional mood check-ins.
- Positive routines.

Possible sources:

- Location/timeline.
- Place categories.
- Check-ins.

### Drive

Possible signals:

- Productive routine blocks.
- Work place time.
- Gym/exercise.
- Morning intention completion.
- Reduced context switching.

Possible sources:

- Timeline.
- User-classified places.
- Check-ins.

### Calm

Possible signals:

- Sleep quality or duration.
- Rest blocks.
- Lower travel burden.
- Less late-night activity.
- Optional stress/mood prompt.

Possible sources:

- Health Connect.
- Timeline.
- Check-ins.

### Connection

Possible signals:

- Time at social places.
- Repeated visits to people-related places.
- Optional user-confirmed social blocks.

Possible sources:

- Place categories.
- Timeline.
- Check-ins.

### Meaning

Possible signals:

- Reflection check-ins.
- Weekly/monthly consistency.
- Purpose-aligned places or routines.
- Completion of stated intentions.

Possible sources:

- Check-ins.
- Timeline.
- Long-term trends.

## Life Score

The Life Score can begin as a weighted average of available dimensions:

```text
Life Score = weighted average of Vitality, Joy, Drive, Calm, Connection, Meaning
```

Initial MVP can use equal weights, with confidence annotations when inputs are missing.

## XP And Levels

XP should reward real actions and app learning:

- Completing a day with enough signal quality.
- Correcting unknown places.
- Completing optional check-ins.
- Maintaining healthy routines.
- Reviewing weekly recap.

XP should not punish users for low-scoring days. Levels should represent long-term engagement and personal history, not moral judgment.

## Score Reasons

Every dimension should expose reasons such as:

```text
Vitality +4:
  More steps than your recent baseline.

Calm -3:
  Sleep data was lower than your recent baseline.

Connection +2:
  More time in places marked as social.

Joy 0:
  Not enough new signal today.
```

## Confidence

Each score should have confidence:

- High: multiple reliable signals.
- Medium: partial signal coverage.
- Low: sparse data or missing permissions.

Low confidence should be displayed as uncertainty, not hidden.

## Research Notes

Potential research areas to evaluate before finalizing score formulas:

- Subjective Vitality Scale.
- PERMA positive psychology framework.
- WHO-5 wellbeing scale.
- Purpose in Life research.

Before implementing final formulas, this repo should collect citations and define what each score can and cannot claim.

## Open Questions

- Should the user customize weights?
- How many days are needed before a personal baseline is trustworthy?
- Should the app show scores before baseline exists?
- Should XP be tied to score movement or only to actions?
