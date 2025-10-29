# QOF → IMQ Transformer — Usage

This module discovers JSON files under an input root, validates them against the boolean query schema, transforms them into IMQ queries, and optionally emits IMQ-JSON artifacts.

## CLI
The CLI is available via the standard Spring Boot application when you pass the `--qofimq-cli` flag.

Examples:

```
java -jar imapi.jar --qofimq-cli
```

With explicit flags and artifact emission:

```
java -jar imapi.jar --qofimq-cli \
  --input="Z:\\Data\\QOF" \
  --output="Z:\\Data\\QOF\\_imq_out" \
  --emit-json \
  --fail-fast \
  --parallelism=4
```

- Exit code: `0` when all files succeed; number of failures (capped at 255) when any file fails; `2` on unexpected exceptions.
- Artifacts: When `--emit-json` is specified (or `qofimq.emit-json=true`), IMQ JSON files are written next to the relative structure of inputs into the configured output path, with the `.imq.json` suffix.

## Configuration
Configuration properties (defaults in `src/main/resources/application.yml`):

- `qofimq.input-path` — Input root for discovery (`Z:\\Data\\QOF`).
- `qofimq.output-path` — Output root for artifacts (`Z:\\Data\\QOF\\_imq_out`).
- `qofimq.emit-json` — Whether to emit IMQ JSON artifacts (default `false`).
- `qofimq.max-file-size-bytes` — Max allowed input file size (default 5 MiB).
- `qofimq.parallelism` — Number of worker threads for batch (default 1 = sequential).
- `qofimq.namespace-mappings` — Map of `prefix → IRI base` to resolve fields like `QOF:age`.

Overrides may be provided via `--qofimq.<prop>=...`, environment variables, or `application.yml`.

## REST Endpoint
Not implemented by default. If enabled later, ensure `SecurityConfig` is wired and add security tests.

## Inputs and Schema
Inputs must conform to `schemas/qof-boolean-query.schema.json`. See `docs/qofimq/DEVELOPER.md` for details.
