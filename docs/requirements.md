# Requirements Document

## Introduction
This initiative delivers a transformation utility that reads a collection of JSON files containing boolean query definitions from `Z:\Data\QOF` and converts each into an IMQ (Information Model Query) representation as defined by the IM API domain model under `org.endeavourhealth.imapi.model.imq` (e.g., `Query`, `PathQuery`, `QueryType`, and related classes). The result will be consumable programmatically (Java objects) and optionally serializable (e.g., JSON) for downstream services in IMAPI.

The primary goals are: robust ingestion, precise mapping to IMQ constructs, validation with meaningful errors, traceable logging, and simple interfaces (CLI and/or service endpoint) to run the transformation at scale.

## Requirements

1. Input discovery and ingestion
   - User Story:
     > As a user, I want the tool to discover and read all JSON query files from `Z:\Data\QOF` so that I can transform them in a single run without manual selection.
   - Acceptance Criteria:
     > WHEN I run the transformation pointing to `Z:\Data\QOF` THEN the system SHALL recursively enumerate `.json` files and read their contents using UTF-8.

2. JSON schema compliance
   - User Story:
     > As a user, I want the tool to validate input JSON against a defined schema so that malformed inputs are caught early.
   - Acceptance Criteria:
     > WHEN a JSON file does not conform to the expected boolean-query schema THEN the system SHALL reject it with a clear validation error listing the offending path(s).

3. Boolean expression parsing
   - User Story:
     > As a user, I want boolean query structures parsed into an abstract representation so that they can be faithfully transformed to IMQ.
   - Acceptance Criteria:
     > WHEN a query contains nested boolean operators (AND/OR/NOT) THEN the system SHALL construct an equivalent internal representation preserving grouping and precedence.

4. Field/path mapping to IMQ
   - User Story:
     > As a user, I want query fields/paths mapped to IMQ `PathQuery`/`Query` constructs so that semantics are preserved.
   - Acceptance Criteria:
     > WHEN a field condition is encountered (e.g., term equals, ranges, sets) THEN the system SHALL map it to the appropriate IMQ class/enum (e.g., `QueryType`) with correct IRIs where applicable.

5. Concept/IRI resolution
   - User Story:
     > As a user, I want source identifiers converted to appropriate IRIs/namespaces so that IMQ is resolvable within IMAPI.
   - Acceptance Criteria:
     > WHEN a source identifier requires an IRI THEN the system SHALL apply the correct namespace (e.g., from `org.endeavourhealth.imapi.vocabulary.Namespace`) or configured mapping, failing fast if unresolved.

6. Error handling and reporting
   - User Story:
     > As a user, I want clear error messages per file so that I can fix inputs efficiently.
   - Acceptance Criteria:
     > WHEN a file fails to transform THEN the system SHALL emit a structured error including file path, cause (validation, mapping, IO), and suggested remediation if known.

7. Batch processing and partial success
   - User Story:
     > As a user, I want the batch run to continue on other files even if some fail so that I can maximize throughput.
   - Acceptance Criteria:
     > WHEN one or more files fail THEN the system SHALL continue processing remaining files and produce a final summary with counts of successes/failures.

8. Output: IMQ object generation
   - User Story:
     > As a developer, I want the transformation to produce in-memory IMQ objects (`Query`, `PathQuery`, etc.) so that I can use them directly in the IMAPI services.
   - Acceptance Criteria:
     > WHEN a file is successfully transformed THEN the system SHALL return a strongly-typed IMQ object graph ready for downstream processing.

9. Output serialization (optional artifact)
   - User Story:
     > As a user, I want an option to serialize the IMQ to JSON for inspection and archiving.
   - Acceptance Criteria:
     > WHEN the `--emit-json` option is enabled THEN the system SHALL write an IMQ-JSON artifact per input file to a target directory, preserving filename stems.

10. Command-line interface (CLI)
    - User Story:
      > As a user, I want to run the transformation from the command line so that I can integrate it into scripts and CI.
    - Acceptance Criteria:
      > WHEN I invoke the CLI with input path and options THEN the system SHALL execute with flags for `--input`, `--output`, `--emit-json`, and `--fail-fast` (optional), returning a non-zero exit code on failures.

11. Service integration (optional endpoint)
    - User Story:
      > As an integrator, I want an API endpoint to transform an uploaded JSON so that I can integrate with IMAPI services.
    - Acceptance Criteria:
      > WHEN a POST is made with a JSON body to the endpoint THEN the system SHALL respond with either IMQ-JSON or a validation error using consistent error structure.

12. Performance and scalability
    - User Story:
      > As a user, I want reasonable performance on hundreds to thousands of files so that batch runs complete quickly.
    - Acceptance Criteria:
      > WHEN processing 1,000 small JSON files (<10KB each) on standard hardware THEN the system SHALL complete in under 60 seconds and use bounded memory (streamed IO).

13. Logging and auditability
    - User Story:
      > As an operator, I want structured logs so that I can audit what was transformed and why failures occurred.
    - Acceptance Criteria:
      > WHEN the run completes THEN the system SHALL produce logs with per-file status, timing, and error details using the project’s logging framework.

14. Configuration
    - User Story:
      > As an operator, I want configurable namespaces and field mappings so that the transformer adapts to data variations.
    - Acceptance Criteria:
      > WHEN configuration is provided (properties or YAML) THEN the system SHALL load it at startup and apply it consistently with sensible defaults.

15. Testing and quality
    - User Story:
      > As a developer, I want comprehensive tests so that changes do not break transformations.
    - Acceptance Criteria:
      > WHEN unit and integration tests run THEN the system SHALL cover parsing, mapping, error paths, and sample fixtures from `TestTransforms` and `src/test/resources` where applicable.

16. Security and access control (service mode)
    - User Story:
      > As a security engineer, I want service endpoints aligned with existing `SecurityConfig` so that access is controlled.
    - Acceptance Criteria:
      > WHEN the endpoint is enabled THEN the system SHALL enforce authentication/authorization per `SecurityConfig` and return `403` with `RestAccessDeniedHandler` when unauthorized.

17. Documentation and maintainability
    - User Story:
      > As a maintainer, I want clear docs so that new contributors can extend mappings.
    - Acceptance Criteria:
      > WHEN viewing project docs THEN the system SHALL include mapping rules, examples, configuration options, and troubleshooting guidance aligned with this requirements document.
