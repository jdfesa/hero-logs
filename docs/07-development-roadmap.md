# Development Roadmap

This roadmap is intentionally practical for spare-time development.

## Phase 0: Repository Foundation

Goals:

- Create documentation.
- Record architecture decisions.
- Define MVP boundaries.
- Initialize public repo hygiene.

Deliverables:

- README.
- Docs folder.
- ADR folder.
- `.gitignore`.
- GitHub remote.

## Phase 1: Android Shell

Goals:

- Create native Android project.
- Establish Compose theme.
- Establish navigation.
- Add basic screen placeholders.

Deliverables:

- `app` module.
- MainActivity.
- Compose navigation.
- LifeBar placeholder.
- Timeline placeholder.
- Settings placeholder.
- CI build.

## Phase 2: Local Data Foundation

Goals:

- Add Room database.
- Add DataStore.
- Define first entities.
- Add repositories.

Deliverables:

- Local database schema.
- Fake/demo data mode.
- Repository interfaces.
- Unit tests for repositories and mappers.

## Phase 3: Timeline MVP

Goals:

- Build a manually seeded timeline first.
- Add edit/correction UI.
- Then integrate real signals.

Deliverables:

- Timeline list.
- Entry detail.
- Place edit.
- Trip edit.
- Confidence display.

## Phase 4: Device Signals

Goals:

- Integrate location.
- Integrate activity recognition.
- Integrate Health Connect.

Deliverables:

- Permission education screens.
- Location data source.
- Activity data source.
- Health data source.
- Battery-aware processing strategy.

## Phase 5: Scoring MVP

Goals:

- Calculate first daily scores.
- Explain score reasons.
- Add XP and levels.

Deliverables:

- Scoring engine.
- DailyScore persistence.
- ScoreReason persistence.
- LifeBar screen.
- Unit tests for scoring.

## Phase 6: Check-Ins And Recaps

Goals:

- Add optional prompts.
- Generate weekly recap.

Deliverables:

- Check-in screen.
- Notification opt-in.
- Weekly recap generator.
- Weekly recap screen.

## Phase 7: Polish And Public Alpha

Goals:

- Improve UX.
- Add tests.
- Improve privacy controls.
- Prepare public release notes.

Deliverables:

- Data deletion flow.
- Permission status screen.
- Accessibility pass.
- README screenshots.
- Alpha checklist.

## Development Rule

Every phase should leave the app runnable and the docs updated.
