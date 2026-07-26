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
feature/p4a-permission-education
docs/scoring-model
```

Each agent implementation phase uses one branch. Do not combine the next phase
into the same branch, and do not commit implementation work directly to `main`.

## Commit Style

Use clear, small Conventional Commits in English:

```text
docs: add product vision
docs: define permission strategy
chore: initialize android project
feat: add timeline screen placeholder
test: cover scoring baseline calculation
```

Allowed types include `feat`, `fix`, `refactor`, `test`, `docs`, `build`, `ci`,
and `chore`. Do not force-push phase branches.

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

## Agent-Assisted Phases

Repository-wide agent rules live in `AGENTS.md`. Google Antigravity also loads
the workspace rule and workflow under `.agents/`.

Before coding, an agent must read `docs/agent/PROJECT_STATE.md` and the linked
current-phase brief. A code phase is complete only after:

- its acceptance criteria are met;
- `./scripts/verify.sh full` succeeds;
- affected documentation and agent state are updated;
- the branch contains reviewable Conventional Commits;
- the agent stops before the next phase.
