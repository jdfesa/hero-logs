# UI Map

This document maps the expected screens and navigation.

## Primary Navigation

Initial bottom navigation:

```text
LifeBar
Timeline
Insights
Settings
```

Optional future sections:

```text
Check-In
Weekly Recap
Privacy
```

These may be accessible from cards or settings rather than always visible.

## Screens

### Onboarding

Purpose:

- Explain HeroLogs.
- Set privacy expectations.
- Request initial permissions progressively.

Key states:

- Welcome.
- Privacy promise.
- Location explanation.
- Activity explanation.
- Health Connect explanation.
- Ready state.

### LifeBar

Purpose:

- Show today's score at a glance.

Main UI:

- Date selector.
- XP and level.
- Composite Life Score.
- Six dimension bars.
- Score reason entry points.
- Today summary.

### Timeline

Purpose:

- Show the reconstructed day.

Main UI:

- Day selector.
- Timeline list.
- Place entries.
- Trip entries.
- Unknown entries.
- Correction actions.

### Timeline Entry Detail

Purpose:

- Inspect and correct one item.

Main UI:

- Time range.
- Type.
- Place or movement classification.
- Confidence.
- Edit controls.

### Insights

Purpose:

- Explain score changes and trends.

Main UI:

- Daily score explanation.
- Dimension details.
- Baseline comparison.
- Missing signal notices.

### Check-In

Purpose:

- Capture optional subjective signal.

Main UI:

- Prompt.
- Short response.
- Mood/pulse selector.
- Skip action.

### Weekly Recap

Purpose:

- Summarize a week in one scroll.

Main UI:

- Week score trend.
- Time by category.
- Places visited.
- Movement summary.
- XP summary.
- Notable changes.

### Privacy

Purpose:

- Make data handling visible and controllable.

Main UI:

- Local-only status.
- Exact stored categories: Timeline entries, places, and app preferences.
- Notice that Android permission grants are system-managed.
- Confirmed delete-all-local-data action.
- Progress, success, database failure, and partial preferences failure states;
  progress and final results are announced as polite accessibility updates.
- Back navigation to Settings; the bottom navigation remains hidden on this
  detail route.
- Export data eventually.

### Settings

Purpose:

- Configure app preferences.

Main UI:

- Theme.
- Notifications.
- Check-in schedule.
- Scrollable permission cards with accurate foreground-location,
  activity-recognition, and Health Connect status.
- Explicit request actions only for missing foreground-location and
  activity-recognition access.
- Entry point to Privacy & Data controls.
- About.

## Visual Direction

The product can use a playful hero/adventure identity, but the actual tool surface should stay calm and readable.

Recommended tone:

- Warm.
- Clear.
- Slightly game-like.
- Not childish.
- Not overly clinical.

## Accessibility

The app should support:

- Dynamic font sizes.
- Sufficient contrast.
- TalkBack labels.
- Non-color-only score meaning.
- Reduced motion where animations are used.
