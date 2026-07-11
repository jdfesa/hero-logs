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

Status: completed.

Goals:

- Create native Android project.
- Establish Compose theme.
- Establish navigation.
- Add basic screen placeholders.

Deliverables:

- `app` module.
- MainActivity.
- Compose navigation for LifeBar, Timeline, Insights, and Settings.
- LifeBar placeholder.
- Timeline and Settings placeholders.
- Reproducible Gradle Wrapper build.

## Phase 2: Local Data Foundation

Status: completed for the initial Timeline scope.

Goals:

- Add Room database.
- Add DataStore.
- Define first entities.
- Add repositories.

Deliverables:

- Local Room database schema and exported schema artifact.
- Explicit local demo-data mode.
- Timeline repository interface and Room implementation.
- Unit tests for domain models, mappers, and Timeline ViewModel state.

## Phase 3: Timeline MVP

Status: first functional cut completed.

Goals:

- Build a manually seeded timeline first.
- Add edit/correction UI.
- Then integrate real signals.

Deliverables:

- Timeline list backed by Room.
- Entry detail.
- Place/title correction.
- Trip movement correction.
- Textual confidence display.

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
