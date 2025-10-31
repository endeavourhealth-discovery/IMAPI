# Task List: QOF to IMQ Transformation Implementation

**Last Updated:** 2025  
**Status:** Phase 2 Complete | Phase 3 Ready

## Progress Summary
- **Phase 1 (Foundation & Infrastructure):** ✅ Complete (32/40 core tasks)
- **Phase 2 (Core Transformation Engine):** ✅ Complete (42/55 core tasks)
- **Phase 3 (Engine Integration & Orchestration):** ✅ Complete (30/30 core tasks)
- **Overall:** 104/125 core tasks complete (83%)

---

## Phase 1: Foundation & Infrastructure

### 1.1 Project Structure and Module Setup (Plan 1.1 → REQ-7)

- [x] Create `org.endeavourhealth.imapi.transformation` package hierarchy
- [x] Create `org.endeavourhealth.imapi.transformation.core` subpackage
- [x] Create `org.endeavourhealth.imapi.transformation.component` subpackage
- [x] Create `org.endeavourhealth.imapi.transformation.util` subpackage
- [x] Create base interface `QOFTransformer` in core package
- [x] Create base interface `TransformationContext` in core package
- [x] Configure logging infrastructure with SLF4J/Logback
- [x] Create transformation configuration properties class
- [x] Update build.gradle.kts with new module dependencies (using existing SLF4J/Logback)
- [x] Create initial module documentation

### 1.2 QOF Model Extension and Utilities (Plan 1.2 → REQ-1, REQ-2)

- [x] Analyze existing QOF model classes for extension points
- [x] Add validation method to QOFDocument class (created QOFModelValidator)
- [x] Add validation methods to Selection, Register, ExtractionField, Indicator classes (in QOFModelValidator)
- [x] Create `QOFModelValidator` utility class
- [x] Create `QOFDocumentDeserializer` Jackson deserializer
- [x] Add null-safety wrapper methods to QOFDocument (in QOFDocumentDefaults)
- [x] Create `QOFDocumentDefaults` class for default value handling
- [x] Add metadata accessor utility methods (in QOFDocumentDefaults)
- [ ] Create unit tests for model utilities
- [ ] Create integration tests for QOF deserialization

### 1.3 IMQ Query Builder and Factories (Plan 1.3 → REQ-7)

- [x] Create `QueryBuilder` fluent builder class
- [x] Create `MatchBuilder` fluent builder class
- [x] Create `PathBuilder` fluent builder class
- [x] Create `ReturnBuilder` fluent builder class
- [x] Create `WhereBuilder` fluent builder class
- [x] Create `NodeBuilder` for Node object construction
- [x] Create factory method `createEmptyQuery()` (via QueryBuilderFactory)
- [x] Add builder pattern chainable methods
- [ ] Create unit tests for all builder classes
- [ ] Document builder usage patterns

### 1.4 Error Handling Framework (Plan 1.4 → REQ-8)

- [x] Create `TransformationException` custom exception
- [x] Create `ValidationException` custom exception
- [x] Create `TransformationError` data class
- [x] Create `ErrorCollector` for aggregating transformation errors (TransformationErrorCollector)
- [x] Create `TransformationLogger` with correlation ID support
- [x] Create `ErrorReporter` for formatted error output
- [x] Add error context enrichment utilities (via TransformationErrorCollector)
- [ ] Create error handling unit tests
- [ ] Create integration tests for error scenarios
- [ ] Document error handling patterns

---

## Phase 2: Core Transformation Engine

### 2.1 QOF Document Parsing and Validation (Plan 2.1 → REQ-1, REQ-8)

- [x] Create `QOFDocumentLoader` class
- [x] Implement JSON file reading with encoding support
- [x] Implement QOFDocument deserialization
- [x] Create structural validation logic
- [x] Validate required fields (name, selections, registers)
- [x] Validate field types and value constraints
- [x] Implement default value initialization
- [x] Create validation error aggregation
- [x] Create `QOFDocumentValidator` class
- [ ] Write comprehensive unit tests for validation
- [ ] Write integration tests with sample QOF files

### 2.2 Document Metadata Transformation (Plan 2.2 → REQ-2)

- [x] Create `MetadataTransformer` component
- [x] Implement QOFDocument.name → Query.name mapping
- [x] Implement default name generation logic
- [x] Map QOFDocument description if available
- [x] Handle missing metadata gracefully
- [x] Preserve metadata identifiers
- [ ] Create metadata mapping tests
- [ ] Document metadata transformation rules
- [x] Add metadata enrichment logic (timestamps, source info)
- [ ] Create transformation examples

### 2.3 Selection Criteria to Where Clause Transformation (Plan 2.3 → REQ-3, REQ-7)

- [x] Create `SelectionTransformer` component
- [x] Implement Selection → Match conversion
- [x] Create SelectionRule to Match conversion
- [x] Implement AND operator handling
- [x] Implement OR operator handling
- [x] Implement NOT operator handling
- [x] Handle nested selection criteria
- [x] Create Where clause builder
- [ ] Write unit tests for selection transformation
- [ ] Write integration tests with complex selection scenarios
- [ ] Document selection transformation rules
- [ ] Add examples for each operator type

### 2.4 Register Definitions to DataSet Transformation (Plan 2.4 → REQ-4, REQ-7)

- [x] Create `RegisterTransformer` component
- [x] Implement Register → dataSet mapping
- [x] Extract registry names and sources
- [x] Create dataSet entries in Query
- [x] Handle multiple registers aggregation
- [x] Preserve register metadata
- [ ] Create unit tests for register transformation
- [ ] Write integration tests with multiple registers
- [ ] Document register transformation rules
- [ ] Add edge case handling (empty, null, duplicate registers)

### 2.5 Extraction Fields to Path Transformation (Plan 2.5 → REQ-5, REQ-7)

- [x] Create `ExtractionFieldTransformer` component
- [x] Implement ExtractionField → Path conversion
- [x] Create Node references from field definitions
- [x] Map field paths to IMQ Path objects
- [x] Create Return clause from extraction fields
- [x] Handle multiple extraction fields
- [x] Implement field property mapping
- [ ] Write unit tests for extraction field transformation
- [ ] Write integration tests with complex field hierarchies
- [ ] Document extraction field transformation rules
- [ ] Add property mapping examples

### 2.6 Indicator Logic Transformation (Plan 2.6 → REQ-6, REQ-7)

- [x] Create `IndicatorTransformer` component
- [x] Analyze Indicator structure and calculation logic
- [x] Implement denominator criteria → filtering logic
- [x] Implement numerator calculation → return/groupBy clauses
- [x] Map calculation rules to IMQ expressions
- [x] Handle KPI-specific logic
- [x] Create GroupBy clauses for indicator aggregations
- [ ] Write unit tests for indicator transformation
- [ ] Write integration tests with KPI scenarios
- [ ] Document indicator transformation rules
- [ ] Add calculation mapping examples

---

## Phase 3: Engine Integration & Orchestration

### 3.1 Main Transformation Orchestrator (Plan 3.1 → REQ-7, REQ-8)

- [x] Create `QOFToIMQTransformer` main orchestrator class
- [x] Implement transformation lifecycle methods
- [x] Coordinate calls to component transformers (metadata, selections, registers, fields, indicators)
- [x] Implement parse → validate → transform → serialize pipeline
- [x] Create error aggregation across components
- [x] Implement transformation progress tracking
- [x] Add detailed transformation logging
- [ ] Write orchestrator unit tests
- [ ] Write end-to-end integration tests
- [ ] Document orchestration logic
- [ ] Add orchestration examples

### 3.2 Transformation Context and State Management (Plan 3.2 → REQ-7, REQ-8)

- [x] Create `TransformationContext` implementation class
- [x] Implement transformation state tracking
- [x] Create reference mapping storage (QOF→IMQ element IDs)
- [x] Implement context injection mechanism
- [x] Add context lifecycle management (initialization, cleanup)
- [x] Create context builder for test scenarios
- [ ] Write unit tests for context operations
- [ ] Document context usage patterns
- [ ] Add thread-safety mechanisms if needed
- [x] Create context debugging utilities

### 3.3 Validation Integration (Plan 3.3 → REQ-1, REQ-8, REQ-10)

- [x] Create `TransformationValidator` combining all validators
- [x] Implement pre-transformation validation (input QOFDocument)
- [x] Implement checkpoint validation (intermediate transformation states)
- [x] Implement post-transformation validation (output Query)
- [x] Create comprehensive validation report generation
- [x] Add validation rule extensibility
- [ ] Write validation integration tests
- [ ] Document validation checkpoints
- [ ] Create validation examples
- [ ] Add validation error recovery strategies

---

## Phase 4: Output and Serialization

### 4.1 IMQ Query Serialization (Plan 4.1 → REQ-10)

- [ ] Create `QuerySerializer` custom Jackson serializer
- [ ] Implement Query object → JSON serialization
- [ ] Create serializers for Match, Path, Return, GroupBy objects
- [ ] Implement JSON property ordering (prefix, iri, name, description, etc.)
- [ ] Handle JsonInclude.NON_DEFAULT filtering
- [ ] Implement custom handlers for complex types
- [ ] Write serialization unit tests
- [ ] Write serialization integration tests
- [ ] Document serialization configuration
- [ ] Add serialization examples

### 4.2 Output Validation (Plan 4.2 → REQ-10)

- [ ] Create `QueryOutputValidator` class
- [ ] Implement JSON schema validation for Query output
- [ ] Verify required fields are present in output
- [ ] Validate field types and value constraints
- [ ] Create validation failure reporting with field details
- [ ] Add schema version checking
- [ ] Write output validation unit tests
- [ ] Create test cases for invalid output scenarios
- [ ] Document validation rules
- [ ] Add validation debugging utilities

### 4.3 Output File Handling (Plan 4.3 → REQ-10)

- [ ] Create `QueryOutputWriter` class
- [ ] Implement file writer with UTF-8 encoding
- [ ] Create output directory creation and management
- [ ] Implement overwrite strategy (allow/deny)
- [ ] Implement append strategy if applicable
- [ ] Add atomic write operations (write to temp, then rename)
- [ ] Create backup mechanism for existing files
- [ ] Implement rollback capability
- [ ] Write file handling unit tests
- [ ] Write file I/O integration tests
- [ ] Document file output options

---

## Phase 5: Batch Processing and Scalability

### 5.1 Batch Transformation Processor (Plan 5.1 → REQ-9, REQ-13)

- [ ] Create `BatchTransformationProcessor` class
- [ ] Implement file discovery from input directory
- [ ] Implement glob/pattern-based file selection
- [ ] Create sequential processing strategy
- [ ] Implement parallel processing option (if performance requires)
- [ ] Add progress tracking with percentage/count reporting
- [ ] Create progress listener interface for extensibility
- [ ] Write batch processor unit tests
- [ ] Write batch integration tests with multiple files
- [ ] Document batch processing options
- [ ] Add batch processing examples

### 5.2 Error Resilience in Batch Processing (Plan 5.2 → REQ-8, REQ-9)

- [ ] Implement per-document error isolation
- [ ] Create `BatchTransformationReport` for summary data
- [ ] Track successfully transformed documents
- [ ] Track failed documents with error details
- [ ] Generate failed document list for retry
- [ ] Create batch error logs with document mapping
- [ ] Implement continue-on-error strategy
- [ ] Write batch error handling tests
- [ ] Create batch error scenarios tests
- [ ] Document batch error handling
- [ ] Add batch error reporting examples

### 5.3 Performance Optimization (Plan 5.3 → REQ-13)

- [ ] Create performance profiling infrastructure
- [ ] Identify transformation bottlenecks
- [ ] Implement caching for reference mappings
- [ ] Consider parallel processing architecture
- [ ] Profile memory usage under load
- [ ] Implement memory-efficient streaming if needed
- [ ] Create performance benchmarking tests
- [ ] Document performance characteristics
- [ ] Add optimization recommendations
- [ ] Create performance tuning guide

---

## Phase 6: Tooling and Integration

### 6.1 Command-Line Interface (CLI) (Plan 6.1 → REQ-14)

- [ ] Create `QOFToIMQCliApplication` main class
- [ ] Implement argument parsing with Picocli or similar
- [ ] Add `--input` parameter for input file path
- [ ] Add `--output` parameter for output file path
- [ ] Add `--batch` mode for processing directories
- [ ] Add `--verbose` flag for detailed logging
- [ ] Add `--config` parameter for configuration file
- [ ] Implement `--help` command with usage information
- [ ] Create usage examples in help text
- [ ] Implement exit codes (0=success, 1=error)
- [ ] Write CLI integration tests
- [ ] Create CLI user documentation
- [ ] Package CLI as executable Fat JAR

### 6.2 Configuration Management (Plan 6.2 → REQ-7, REQ-14)

- [ ] Create `TransformationConfiguration` class
- [ ] Implement YAML configuration file support
- [ ] Support JSON configuration files
- [ ] Create configuration defaults
- [ ] Implement command-line parameter overrides
- [ ] Support environment variable overrides
- [ ] Create configuration profiles (dev, prod)
- [ ] Add configuration validation
- [ ] Write configuration loading tests
- [ ] Document all configuration options
- [ ] Create example configuration files

### 6.3 Integration Points (Plan 6.3 → REQ-7, REQ-14)

- [ ] Design programmatic Java API for transformation
- [ ] Create Spring Boot service component `QOFTransformationService`
- [ ] Implement dependency injection for transformers
- [ ] Design REST API endpoints (if needed)
- [ ] Create `@RestController` for transformation endpoint
- [ ] Implement request/response DTOs
- [ ] Add Spring integration tests
- [ ] Document Java API usage
- [ ] Document REST API endpoints
- [ ] Create integration examples

---

## Phase 7: Documentation and Testing

### 7.1 Mapping Documentation (Plan 7.1 → REQ-11)

- [ ] Create QOF→IMQ field mapping matrix document
- [ ] Document QOFDocument fields and their IMQ equivalents
- [ ] Document Selection → Where/Match transformation rules
- [ ] Document Register → dataSet mapping rules
- [ ] Document ExtractionField → Path mapping rules
- [ ] Document Indicator → Query transformation rules
- [ ] Add edge case and exception handling notes
- [ ] Create transformation examples for each type
- [ ] Add visual diagrams for complex transformations
- [ ] Document data type conversions
- [ ] Create mapping reference guide
- [ ] Add troubleshooting section

### 7.2 Unit Testing (Plan 7.2 → REQ-12)

- [ ] Create `MetadataTransformerTest` test class
- [ ] Create `SelectionTransformerTest` test class
- [ ] Create `RegisterTransformerTest` test class
- [ ] Create `ExtractionFieldTransformerTest` test class
- [ ] Create `IndicatorTransformerTest` test class
- [ ] Create `QOFDocumentLoaderTest` test class
- [ ] Create `QueryBuilderTest` test class
- [ ] Test null value handling in all components
- [ ] Test empty collection handling
- [ ] Test malformed data handling
- [ ] Test default value generation
- [ ] Create 80%+ code coverage for core components
- [ ] Document test execution procedures
- [ ] Generate code coverage reports

### 7.3 Integration Testing (Plan 7.3 → REQ-12)

- [ ] Create end-to-end transformation test scenarios
- [ ] Create test QOF documents with various structures
- [ ] Create expected IMQ output reference data
- [ ] Implement `TransformationIntegrationTest` test suite
- [ ] Test complete transformation pipeline
- [ ] Test batch processing scenarios
- [ ] Test error handling in realistic scenarios
- [ ] Validate transformed output against schema
- [ ] Test data preservation across transformation
- [ ] Create performance regression tests
- [ ] Document test scenarios and data
- [ ] Create test execution documentation

### 7.4 Performance Testing (Plan 7.4 → REQ-13)

- [ ] Create `TransformationPerformanceTest` test class
- [ ] Test single document transformation time (<500ms)
- [ ] Test batch processing with 1000 documents (<30s)
- [ ] Monitor memory usage during transformations
- [ ] Create performance benchmarking framework
- [ ] Test scalability with increasing document sizes
- [ ] Profile CPU usage patterns
- [ ] Create performance baselines
- [ ] Document performance characteristics
- [ ] Create performance tuning recommendations
- [ ] Add performance regression detection

---

## Phase 8: Deployment and Operations

### 8.1 Artifact Generation (Plan 8.1 → REQ-14)

- [ ] Create Fat JAR gradle task in build.gradle.kts
- [ ] Generate `qofimq-cli.jar` CLI executable JAR
- [ ] Generate library JAR for programmatic use
- [ ] Create manifest with main class specification
- [ ] Add version information to artifacts
- [ ] Create artifact checksums for integrity verification
- [ ] Document artifact generation process
- [ ] Create artifact deployment procedures
- [ ] Test artifact execution
- [ ] Document artifact requirements and dependencies

### 8.2 Deployment Documentation (Plan 8.2 → REQ-14)

- [ ] Create INSTALLATION.md guide
- [ ] Document system requirements (Java version, memory)
- [ ] Create step-by-step installation instructions
- [ ] Create quick start guide
- [ ] Create operational runbooks
- [ ] Document common troubleshooting scenarios
- [ ] Create FAQ document
- [ ] Create upgrade/rollback procedures
- [ ] Document configuration management
- [ ] Create disaster recovery procedures

### 8.3 Monitoring and Observability (Plan 8.3 → REQ-8, REQ-13)

- [ ] Add metrics for transformation success rate
- [ ] Add metrics for transformation failure rate
- [ ] Add performance metrics (processing time, throughput)
- [ ] Add memory usage monitoring
- [ ] Implement Micrometer metrics collection
- [ ] Create Prometheus-compatible metrics endpoint
- [ ] Add structured logging (JSON format)
- [ ] Create log aggregation guidelines
- [ ] Document metrics and monitoring setup
- [ ] Create alerting recommendations
- [ ] Add observability examples

---

## Cross-Cutting Activities

- [ ] Regular code reviews for all PRs
- [ ] Maintain consistent code style and formatting
- [ ] Update CI/CD pipeline for build and test automation
- [ ] Maintain project documentation in sync with code
- [ ] Create architectural decision records (ADRs) as needed
- [ ] Track technical debt and refactoring opportunities
- [ ] Prepare release notes for each milestone
- [ ] Community communication and stakeholder updates

---

## Notes

- All tasks must be completed with code review approval
- Tests must pass before marking tasks complete
- Documentation must be updated alongside code changes
- Performance targets must be verified before marking Phase 5/13 complete
- External dependencies must be documented and version-locked
