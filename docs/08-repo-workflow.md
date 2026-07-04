# Repo Workflow

This repository is expected to live publicly at:

```text
https://github.com/jdfesa/hero-logs
```

## Branching

Recommended default branch:

```text
main
```

Recommended feature branches:

```text
feature/android-shell
feature/timeline-mvp
feature/scoring-engine
docs/scoring-model
```

## Commit Style

Use clear, small commits:

```text
docs: add product vision
docs: define permission strategy
chore: initialize android project
feat: add timeline screen placeholder
test: cover scoring baseline calculation
```

## Pull Request Checklist

Before merging:

- The app builds if Android code changed.
- Tests were added or updated for logic changes.
- Documentation changed when behavior or architecture changed.
- Privacy impact was considered for data or permission changes.
- Screenshots were updated for visible UI changes when useful.

## Documentation Rules

- Major technical decisions go in `docs/adr/`.
- Product behavior changes update `docs/01-functional-requirements.md`.
- New permissions update `docs/04-permissions-strategy.md`.
- New stored data updates `docs/03-data-and-privacy.md`.
- Score formula changes update `docs/05-scoring-model.md`.

## Local Reference Files

Temporary screenshots and local research assets should stay in:

```text
tmp/
```

The `tmp/` directory is ignored by Git.
