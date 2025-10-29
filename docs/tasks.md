# Development Tasks

Derived from `docs/plan.md`. Each task includes links to Plan items (P#) and Requirements (R#). Mark completion with `[x]`.

## Phase 0 — Setup

1. [x] Confirm availability of IMQ model classes in codebase (`org.endeavourhealth.imapi.model.imq`) and review their constructors and usage patterns. (P4, R8)
2. [x] Create a new module or package for the transformer (e.g., `org.endeavourhealth.imapi.transform.qofimq`). (P4, R17)
3. [x] Define configuration structure (properties/YAML) with defaults for input/output paths and namespace mappings. (P12, R14)

## Phase 1 — Ingestion & Validation

4. [x] Implement recursive file discovery for `.json` under configurable input root (default `Z:\Data\QOF`). (P1, R1)
5. [x] Implement streamed UTF-8 file reads with size safeguards. (P1, R12, R1)
6. [x] Draft JSON Schema for boolean queries (operators, operands, fields, values, types). (P2, R2)
7. [x] Implement schema validation with precise error reporting (file path, JSON pointer, message). (P2, R2)
8. [x] Add structural checks beyond schema (e.g., balanced boolean trees, unsupported operators). (P2, R2, R3)

## Phase 2 — Transformation Core (IMQ Mapping)

9. [x] Implement internal AST model for boolean expressions (AND/OR/NOT, leaf predicates). (P3, R3)
10. [x] Build parser from input JSON to AST, preserving grouping/precedence. (P3, R3)
11. [x] Implement mapper from AST to IMQ objects (`Query`, `PathQuery`, `QueryType`, etc.). (P4, R4, R8)
12. [x] Implement value handling for predicates (equals, range, set, exists, negation). (P4, R4)
13. [x] Implement concept/IRI resolution with `Namespace` and configurable mappings; fail fast on missing. (P5, R5, R14)
14. [x] Define clear exceptions for validation vs mapping vs IO errors. (P11, R6)

## Phase 3 — Execution Modes & Outputs

15. [x] Implement batch processor that iterates files independently; collect per-file results and timings. (P6, R7, R13)
16. [x] Implement final run summary: counts of total/success/failure; output path and duration. (P6, R7, R13)
17. [x] Implement optional serializer to IMQ-JSON artifacts and write per input file to output dir. (P7, R9)
18. [x] Implement CLI entry point with flags `--input`, `--output`, `--emit-json`, `--fail-fast`. (P8, R10)
19. [x] Define exit codes: 0 success, >0 failures; non-zero on unexpected exceptions. (P8, R10)
20. [ ] Optional: Implement REST endpoint to accept JSON body and return IMQ JSON or error; wire `SecurityConfig`. (P9, R11, R16)

## Phase 4 — Non-Functional Concerns

21. [x] Add structured logging with correlation per file; log validation vs mapping categories and stack traces at debug. (P11, R13, R6)
22. [x] Performance: stream IO; consider bounded parallelism for CPU-bound mapping; configure thread pool. (P10, R12)
23. [x] Make configuration pluggable and documented; support overrides via env and CLI. (P12, R14)

## Phase 5 — Testing & QA

24. [ ] Unit tests: file discovery and schema validation with valid and invalid fixtures. (P13, R15, R1, R2)
25. [ ] Unit tests: boolean parser and AST construction for nested AND/OR/NOT. (P13, R15, R3)
26. [ ] Unit tests: mapper to IMQ objects for equality, range, set, exists, negation. (P13, R15, R4, R8)
27. [ ] Unit tests: IRI resolution with happy path and failure modes. (P13, R15, R5)
28. [ ] Integration tests: batch run over sample directory with mixed success; verify summary and artifacts. (P13, R15, R7, R9)
29. [ ] Performance test: process ~1,000 small files to meet time/memory criteria. (P10, P13, R12)
30. [ ] Security tests (if endpoint enabled): unauthorized access returns 403 via `RestAccessDeniedHandler`. (P9, P13, R16)

## Phase 6 — Documentation & Packaging

31. [ ] Author developer docs for schema, mapping rules, configuration, and examples; link to this spec. (P14, R17)
32. [ ] Usage docs for CLI and (optional) REST endpoint with sample invocations. (P14, R10, R11)
33. [ ] Package runnable jar and integrate into Gradle build; update `README.md`. (P15, R10)
34. [ ] Provide extension guidance and strategy interfaces for new operators/fields. (P16, R17)
