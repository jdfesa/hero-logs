# Agent Development Guide

This directory gives coding agents a small, deterministic operating context for
finishing HeroLogs in phases.

Google Antigravity reads repository instructions from `AGENTS.md` and supports
workspace rules in `.agents/rules/` and slash-command workflows in
`.agents/workflows/`. The HeroLogs rule should be configured as **Always On** in
Antigravity if the IDE does not preserve that activation when the repository is
first opened.

Official references:

- [Antigravity rules and workflows](https://antigravity.google/docs/ide/rules)
- [Google Antigravity IDE codelab](https://codelabs.developers.google.com/getting-started-agy-ide)

## Files

- `AGENTS.md`: always-applicable repository contract.
- `.agents/rules/hero-logs.md`: native Antigravity workspace rule.
- `.agents/workflows/execute-current-phase.md`: manual
  `/execute-current-phase` workflow.
- `PROJECT_STATE.md`: short-lived operational memory and current phase pointer.
- `CURRENT_PHASE.md`: complete specification for the only authorized phase.
- `PHASE_PLAN.md`: ordered map to MVP, with human decision gates.
- `scripts/verify.sh`: reproducible documentation, unit-test, and build checks.

## Starting A Gemini Flash Session

Open a new conversation for each phase and use:

```text
/execute-current-phase
```

If the workflow is not visible, use this prompt:

```text
Read AGENTS.md and execute only the active phase linked from
docs/agent/PROJECT_STATE.md. Follow its acceptance criteria, verification, Git,
documentation, and stop conditions. Do not start the next phase.
```

Do not paste the whole roadmap into the chat. The repository files are the
source of truth, and limiting context helps the model stay inside one phase.

## Human Review Rhythm

Review the plan before implementation, then review:

- the final diff;
- tests and build evidence;
- privacy or permission changes;
- the state file and next-phase pointer;
- Conventional Commits and remote branch.

For phases with a human gate, make the decision explicitly and record it in an
ADR before asking the agent to continue.
