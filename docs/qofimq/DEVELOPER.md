# QOF → IMQ Transformer — Developer Guide

This document describes the transformer internals: input schema, parsing, IMQ mapping rules, configuration, and worked examples. See also `docs/requirements.md` and `docs/plan.md`.

## Input JSON Schema (Boolean Query)
- Location: `src/main/resources/schemas/qof-boolean-query.schema.json`
- Two node shapes:
  - Bool node: `{ "operator": "AND|OR|NOT", "operands": [ Bool|Predicate, ... ] }`
  - Predicate: `{ "field": "<PREFIX:local>", "op": "EQUALS|NOT_EQUALS|IN|NOT_IN|RANGE|EXISTS", ... }`
- Arity rules:
  - `NOT` requires exactly 1 operand
  - `AND`/`OR` require at least 2 operands
- Predicate fields by op:
  - `EQUALS`, `NOT_EQUALS`: require `value`
  - `IN`, `NOT_IN`: require non-empty `values` array
  - `RANGE`: requires `min` and/or `max`
  - `EXISTS`: must not include `value|values|min|max`

Validation is performed by `QofImqValidator` with precise JSON-pointer errors.

## Architecture Overview
- JSON → AST: `QofAstParser`
  - Builds an internal AST consisting of `BoolNode` and `PredicateNode` (package `org.endeavourhealth.imapi.transform.qofimq.ast`).
- AST → IMQ: `QofAstToImqMapper`
  - Produces IMQ model objects (`Query`, `Match`, `Where`, `Range`, `Value`).
- Field IRI Resolution: `IriResolver`
  - Resolves `prefix:local` via `qofimq.namespace-mappings` or `Namespace` enum fallback.
- Batch Orchestration: `QofBatchProcessor` with optional artifact emission by `QofImqSerializer`.
- CLI Entry: `QofImqCli` gated by `--qofimq-cli` flag.

## Mapping Rules (AST → IMQ)
Given a predicate on `field` (resolved to IRI) and operator `op`:
- `EQUALS`: `Where.operator=eq`, `Where.value=<string>`
- `NOT_EQUALS`: builds equality then wraps in a `Match.not`
- `IN`: builds OR of equality `Where`s for each value
- `NOT_IN`: builds OR of equality `Where`s then wraps in NOT
- `RANGE`: maps `{min,max}` to `Where.range.from(operator=gte,value=min)` and/or `Where.range.to(operator=lte,value=max)`
- `EXISTS`: `Where.isNotNull=true` if exists (default), otherwise `Where.isNull=true`
- Predicate-level `negated=true`: additional NOT wrapper around the whole predicate mapping
- Boolean nodes become `Match.and`, `Match.or`, `Match.not`

## Configuration (qofimq)
Defined in `QofImqProperties` and `src/main/resources/application.yml`:
- `input-path` (String): Input root for discovery. Default `Z:\\Data\\QOF`.
- `output-path` (String): Output root for artifacts. Default `Z:\\Data\\QOF\\_imq_out`.
- `emit-json` (boolean): Emit IMQ JSON artifacts per input. Default `false`.
- `max-file-size-bytes` (long): Safeguard for input size. Default `5 MiB`.
- `parallelism` (int): Bounded parallelism for batch. Default `1`.
- `namespace-mappings` (map): Prefix → IRI base for field resolution.

Overrides can be provided via `application.yml`, environment variables, or CLI flags (see Usage).

## Worked Example
Input:
```json
{
  "operator": "AND",
  "operands": [
    { "field": "QOF:age", "op": "RANGE", "min": 18, "max": 75 },
    { "operator": "OR", "operands": [
      { "field": "QOF:sex", "op": "IN", "values": ["male", "female"] },
      { "field": "QOF:active", "op": "EXISTS" }
    ]}
  ]
}
```
Produces an IMQ `Query` with one `Match.and` of two items: a `Where` with `Range` on `QOF:age`, and a `Match.or` with a set membership and an existence test for `QOF:active`.

## Error Handling
- Validation errors are reported with file path and JSON pointer.
- Mapping errors (unsupported operator/value shapes) are thrown as `QofMappingException`.
- Parse errors (malformed JSON) surface as `QofParseException`.
- IRI resolution failures as `IriResolutionException`.

## Extension Points
See `docs/qofimq/EXTENDING.md` and `org.endeavourhealth.imapi.transform.qofimq.spi.PredicateMappingStrategy`.
