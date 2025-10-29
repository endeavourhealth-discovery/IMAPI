# Implementation Plan

This plan is derived from `docs/requirements.md`. Each item lists its related Requirement IDs (R#) and a Priority.

## A. Ingestion & Validation

P1. Input discovery and reading (R1) — Priority: High
- Implement recursive discovery of `.json` files from a configured input directory (default `Z:\Data\QOF`).
- Stream file reads (UTF-8) with bounded memory.

P2. JSON schema and structural validation (R2, R3) — Priority: High
- Define a JSON schema for boolean queries (operators, operands, fields, values).
- Validate input against schema and basic structural rules before transformation.

## B. Transformation Core (IMQ Mapping)

P3. Boolean AST construction (R3) — Priority: High
- Parse nested AND/OR/NOT into an internal AST preserving order and grouping.

P4. IMQ object graph mapping (R4, R8) — Priority: High
- Map AST nodes to IMQ classes: `Query`, `PathQuery`, `QueryType`, etc.
- Ensure correct type selection (term equality, ranges, sets, existence, negation).

P5. Concept/IRI resolution (R5, R14) — Priority: High
- Resolve identifiers to IRIs using `Namespace` and configurable mappings.
- Fail fast on unresolved IRIs with actionable messages.

## C. Execution Modes & Outputs

P6. Batch processing with partial success (R7, R13) — Priority: Medium
- Process files independently; collect successes/failures and timings.
- Emit final summary.

P7. Optional IMQ JSON serialization (R9) — Priority: Medium
- Provide serializer to emit IMQ object graphs as JSON, one file per input.

P8. CLI interface (R10) — Priority: Medium
- Provide a command-line tool with flags: `--input`, `--output`, `--emit-json`, `--fail-fast`.
- Exit codes reflect aggregate status.

P9. Optional service endpoint (R11, R16) — Priority: Low
- Add REST endpoint to accept a JSON payload, return IMQ JSON or error.
- Wire security to existing `SecurityConfig`.

## D. Non-Functional Concerns

P10. Performance optimization (R12) — Priority: Medium
- Stream IO, avoid large in-memory collections; consider parallelism with a bounded thread pool.

P11. Logging and diagnostics (R6, R7, R13) — Priority: High
- Structured logging with per-file correlation; include validation vs mapping error categories.

P12. Configuration management (R14) — Priority: Medium
- Properties/YAML for namespace and field/path mappings; sensible defaults.

P13. Testing strategy (R15) — Priority: High
- Unit tests for parser, mapper, IRI resolution; integration tests over sample fixtures.

P14. Documentation (R17) — Priority: Medium
- Write developer docs: schema, mapping rules, examples, configuration, troubleshooting.

## E. Deliverables & Integration

P15. Packaging and distribution (R10, R11) — Priority: Low
- Package as runnable jar/classpath entry; integrate into the IMAPI build.

P16. Maintenance hooks (R17) — Priority: Low
- Provide extension points for new operators/fields via configuration and strategy interfaces.
