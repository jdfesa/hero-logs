# Contributing To HeroLogs

HeroLogs is in an early planning stage. Contributions should protect clarity, privacy, and maintainability.

## Before Changing Code

Check whether the change affects:

- Product behavior.
- Stored data.
- Permissions.
- Scoring formulas.
- Architecture.
- User privacy.

If it does, update the relevant document in `docs/`.

## Pull Request Expectations

Every pull request should answer:

- What changed?
- Why did it change?
- How was it tested?
- Does it affect privacy, permissions, or local data?
- Does documentation need to change?

## Documentation Expectations

Use these files as the source of truth:

- Product behavior: `docs/01-functional-requirements.md`
- Architecture: `docs/02-android-technical-architecture.md`
- Data/privacy: `docs/03-data-and-privacy.md`
- Permissions: `docs/04-permissions-strategy.md`
- Scoring: `docs/05-scoring-model.md`
- Roadmap: `docs/07-development-roadmap.md`
- Decisions: `docs/adr/`

## Commit Style

Prefer small, direct commits:

```text
docs: define timeline MVP
chore: scaffold android app
feat: add lifebar screen
test: cover score confidence
```

## Local Files

Temporary screenshots, references, and private notes belong in `tmp/`.

The `tmp/` directory is intentionally ignored by Git.
