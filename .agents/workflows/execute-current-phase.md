# Execute The Current HeroLogs Phase

Use this workflow to implement exactly one phase from the HeroLogs delivery
plan. It is intentionally strict so a smaller model can work safely.

1. Read `AGENTS.md`, then read `docs/agent/PROJECT_STATE.md`.
2. Confirm that exactly one phase is marked `READY` or `IN_PROGRESS`. Open the
   phase brief linked there. Do not implement anything yet.
3. Inspect Git status, the affected code, existing tests, and every source
   document required by the brief.
4. If the worktree contains unrelated changes, preserve them and explain how the
   phase will avoid them. If it cannot, stop and ask the user.
5. If the phase has an unresolved human gate, stop and ask only the blocking
   question. Do not choose a policy on the user's behalf.
6. Present a concise implementation plan that maps each acceptance criterion to
   concrete files and tests.
7. Ensure the work is on the phase branch named in the brief. Do not work
   directly on `main`.
8. Implement the smallest complete vertical slice. Follow existing patterns
   before adding a new abstraction or dependency.
9. Run `./scripts/verify.sh quick` during implementation and fix failures caused
   by the phase.
10. Update the required product docs and `docs/agent/PROJECT_STATE.md`. Do not
    mark the phase `COMPLETE` yet.
11. Run `./scripts/verify.sh full`. Perform any manual checks required by the
    phase. Review the full diff and `git diff --check`.
12. Mark the phase `COMPLETE` only when every acceptance criterion is satisfied.
    If anything remains, use `BLOCKED` and record the exact missing evidence.
13. Create small Conventional Commits in English. Push the phase branch after
    successful verification when the user's request includes delivery.
14. Stop. Report the outcome, verification, commits, push status, and next phase
    identifier. Do not begin the next phase.
