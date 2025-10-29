# Working with the Development Checklist (docs/tasks.md)

These guidelines define how to use and maintain the checklist so progress remains traceable and aligned with the specification.

## Marking Progress
- Mark a task as complete by changing its box from `[ ]` to `[x]`.
- Do not delete completed tasks; keep history intact.
- If a task is no longer applicable, append `(n/a)` and include a brief reason.

## Adding or Modifying Tasks
- Keep existing phase headings and their order intact.
- Add new tasks beneath the most relevant phase; prefer small, testable tasks.
- Every new or modified task must include links to both:
  - Plan item(s) in `docs/plan.md` using `P#` notation (e.g., `P4`).
  - Requirement(s) in `docs/requirements.md` using `R#` notation (e.g., `R8`).
- Maintain the existing formatting style:
  - Enumerated tasks with a leading number and a checkbox, e.g., `12. [ ] ... (P4, R8)`.
  - Group tasks strictly under the predefined phase headings.

## Traceability Rules
- When closing a task, ensure related plan items and requirements are still correctly addressed by remaining open tasks.
- If a requirement or plan item expands, add corresponding tasks and cross-links.
- Avoid generic tasks without explicit P#/R# links.

## Review Cadence
- At least once per development day:
  - Reconcile completed tasks with the implementation.
  - Verify there are no orphan requirements (requirements with no tasks referencing them).
  - Ensure tests and docs tasks are represented for any new functionality.

## Consistency & Quality
- Prefer actionable phrasing that results in verifiable outcomes (code change, test added, doc updated).
- Reference concrete paths or class names where it improves clarity.
- For security or configuration items, note the impacted files (e.g., `SecurityConfig`, `Namespace`).

## Examples
- Adding a new mapping operator:
  - Add a task under "Phase 2 — Transformation Core" like:
    `12a. [ ] Implement BETWEEN operator mapping; update JSON Schema and tests. (P4, P13, R2, R4, R15)`
- Documenting a new configuration option:
  - Add a task under "Phase 6 — Documentation & Packaging" like:
    `33a. [ ] Document new namespace alias in README and config reference. (P12, P14, R14, R17)`
