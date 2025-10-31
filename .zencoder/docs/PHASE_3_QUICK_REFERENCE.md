# Phase 3 Quick Reference Guide

## Phase 3: Engine Integration & Orchestration

### Files Created/Modified

| File | Lines | Purpose |
|------|-------|---------|
| QOFToIMQTransformer.java | 290 | Main orchestrator coordinating all transformers |
| TransformationValidator.java | 235 | Multi-level validation coordinator |
| TransformationContextBuilder.java | 145 | Fluent builder for test scenario setup |
| TransformationContextDebugger.java | 280 | Diagnostic tools for context analysis |

**Total Phase 3 Code:** ~950 lines of production code

### Quick Start: Basic Transformation

```java
// Simple transformation from JSON file
QOFToIMQTransformer transformer = new QOFToIMQTransformer();
Query result = transformer.transformFromFile("qof_document.json");

// From JSON string
Query result = transformer.transformFromString(jsonString);

// From QOFDocument object
QOFDocument document = loader.load(...);
Query result = transformer.transform(document);
```

### Accessing Transformation Results

```java
QOFToIMQTransformer transformer = new QOFToIMQTransformer();
Query result = transformer.transformFromFile("qof.json");

// Get transformation context
TransformationContext context = transformer.getContext();

// Check for errors
if (context.hasErrors()) {
    List<TransformationError> errors = context.getErrors();
    // Handle errors
}

// Get execution time
long duration = transformer.getTransformationDuration();

// Get reference mappings
Object imqElement = context.getReference("qof-element-id");

// Get correlation ID
String correlationId = context.getCorrelationId();
```

### Testing: Setup with Builder

```java
TransformationContext testContext = new TransformationContextBuilder()
    .withQuery(testQuery)
    .withReference("qof-id-1", imqNode1)
    .withReference("qof-id-2", imqNode2)
    .withMetadata("testMode", true)
    .build();

// Use context in test
transformer.getContext().mapReference("new-id", newElement);
```

### Validation Examples

```java
TransformationValidator validator = new TransformationValidator();

// Validate input document
ValidationResult inputCheck = validator.validateInputDocument(qofDoc);
if (!inputCheck.isValid()) {
    System.out.println("Errors: " + inputCheck.getErrors());
}

// Validate at checkpoint
ValidationResult checkpointCheck = validator.validateCheckpoint(query, "after-metadata");

// Validate output
ValidationResult outputCheck = validator.validateOutputQuery(query);

// Comprehensive validation
ValidationResult comprehensive = validator.validateOutputQueryComprehensive(query);
System.out.println("Errors: " + comprehensive.getErrorCount());
System.out.println("Warnings: " + comprehensive.getWarningCount());
```

### Debugging & Diagnostics

```java
TransformationContext context = transformer.getContext();
TransformationContextDebugger debugger = new TransformationContextDebugger(context);

// Generate diagnostic report
String report = debugger.generateDiagnosticReport();
log.debug(report);

// Log diagnostics
debugger.logDiagnostics();

// Get one-liner summary
String summary = debugger.generateSummary();
// Output: "Context[abc123de]: Query=MyQuery, Errors=0, HasErrors=false"

// Check if context is valid
boolean valid = debugger.isContextValid();

// Get error statistics by phase
Map<String, Integer> stats = debugger.getErrorStatistics();
// Output: {METADATA: 0, SELECTION: 1, REGISTER: 0, ...}

// Get error summary
String errorSummary = debugger.generateErrorSummary();

// Export context state as map
Map<String, Object> state = debugger.exportContextState();
// Useful for JSON serialization, logging frameworks

// Visual dump of context
String dump = debugger.dumpContextState();
log.info(dump);
```

### Error Handling

```java
try {
    Query result = transformer.transformFromFile("qof.json");
} catch (TransformationException e) {
    log.error("Transformation failed: {}", e.getMessage());
    
    // Get error details
    TransformationContext context = transformer.getContext();
    List<TransformationError> errors = context.getErrors();
    
    for (TransformationError error : errors) {
        log.error("Error in {}: {} (field: {})", 
            error.getPhase(), 
            error.getMessage(), 
            error.getField());
    }
}
```

### Transformation Pipeline Steps

1. **Load & Parse**: QOFDocumentLoader deserializes JSON
2. **Validate Input**: TransformationValidator.validateInputDocument()
3. **Initialize**: Create Query and TransformationContext
4. **Transform Metadata**: MetadataTransformer.transformMetadata()
5. **Transform Selections**: SelectionTransformer.transformSelections()
6. **Transform Registers**: RegisterTransformer.transformRegisters()
7. **Transform Fields**: ExtractionFieldTransformer.transformExtractionFields()
8. **Transform Indicators**: IndicatorTransformer.transformIndicators()
9. **Check Errors**: Aggregate from all transformers
10. **Validate Output**: TransformationValidator.validateOutputQuery()
11. **Return Result**: Query or throw TransformationException

### Key Classes and Methods

#### QOFToIMQTransformer
```java
Query transformFromFile(String filePath)
Query transformFromString(String jsonString)
Query transform(QOFDocument qofDocument)
TransformationContext getContext()
Query getTargetQuery()
long getTransformationDuration()
```

#### TransformationValidator
```java
ValidationResult validateInputDocument(QOFDocument qofDocument)
ValidationResult validateCheckpoint(Query query, String checkpointName)
ValidationResult validateOutputQuery(Query query)
ValidationResult validateOutputQueryComprehensive(Query query)
```

#### TransformationContextBuilder
```java
TransformationContextBuilder withQuery(Query query)
TransformationContextBuilder withReference(String qofId, Object imqElement)
TransformationContextBuilder withMetadata(String key, Object value)
TransformationContext build()
```

#### TransformationContextDebugger
```java
String generateDiagnosticReport()
String generateSummary()
boolean isContextValid()
Map<String, Integer> getErrorStatistics()
String generateErrorSummary()
Map<String, Object> exportContextState()
String dumpContextState()
```

### Build Status

✅ **Compilation Successful**
- 0 errors
- 0 warnings
- All Phase 3 files compile successfully

### Dependency Tree

```
QOFToIMQTransformer
├─ QOFDocumentLoader (Phase 2)
├─ TransformationValidator
├─ TransformationContext (Phase 1)
│  ├─ TransformationErrorCollector (Phase 1)
│  └─ TransformationLogger (Phase 1)
├─ MetadataTransformer (Phase 2)
├─ SelectionTransformer (Phase 2)
├─ RegisterTransformer (Phase 2)
├─ ExtractionFieldTransformer (Phase 2)
└─ IndicatorTransformer (Phase 2)
```

### Integration with Other Phases

**Phase 1 (Foundation)**
- Uses TransformationContext for state management
- Uses TransformationLogger for logging
- Uses TransformationError for error representation
- Uses builder classes (NodeBuilder, PathBuilder, etc.)

**Phase 2 (Transformers)**
- Orchestrates all 5 component transformers
- Passes Query object through transformation chain
- Collects errors from each transformer
- Validates component outputs

**Phase 3 (This Phase)**
- Provides unified API for transformations
- Manages transformation lifecycle
- Validates at multiple checkpoints
- Aggregates and reports errors
- Supports testing and debugging

### Configuration & Extensibility

**Validator Extensibility:**
```java
TransformationValidator.Validator customValidator = new TransformationValidator.Validator() {
    @Override
    public ValidationResult validate(Query query) {
        // Custom validation logic
        return new ValidationResult(isValid, errors, warnings);
    }
    
    @Override
    public String getName() {
        return "CustomValidator";
    }
};
```

**Context Metadata for Custom Logic:**
```java
// Store custom data in context
context.putMetadata("customKey", customValue);

// Retrieve for custom processing
Object value = context.getMetadata("customKey");
```

### Thread Safety Considerations

⚠️ **Current Status:** NOT thread-safe

Each transformation should use its own `QOFToIMQTransformer` instance:
```java
// CORRECT: Each thread/request gets its own transformer
QOFToIMQTransformer transformer = new QOFToIMQTransformer();

// WRONG: Sharing transformer across threads
QOFToIMQTransformer sharedTransformer = new QOFToIMQTransformer();
executor.execute(() -> sharedTransformer.transform(...));  // Race conditions!
```

### Common Scenarios

**Scenario 1: Simple File Transformation**
```java
QOFToIMQTransformer transformer = new QOFToIMQTransformer();
try {
    Query query = transformer.transformFromFile("input.json");
    // Use query...
} catch (TransformationException e) {
    log.error("Transformation failed", e);
}
```

**Scenario 2: Batch Processing**
```java
for (String file : qofFiles) {
    QOFToIMQTransformer transformer = new QOFToIMQTransformer();
    try {
        Query query = transformer.transformFromFile(file);
        saveQuery(query);
    } catch (TransformationException e) {
        logError(file, e);
    }
}
```

**Scenario 3: With Validation Reporting**
```java
TransformationValidator validator = new TransformationValidator();
QOFToIMQTransformer transformer = new QOFToIMQTransformer();

Query query = transformer.transformFromFile("input.json");

ValidationResult result = validator.validateOutputQueryComprehensive(query);
System.out.println("Valid: " + result.isValid());
System.out.println("Errors: " + result.getErrorCount());
System.out.println("Warnings: " + result.getWarningCount());
```

**Scenario 4: Debugging Failed Transformations**
```java
try {
    Query query = transformer.transformFromFile("input.json");
} catch (TransformationException e) {
    TransformationContextDebugger debugger = 
        new TransformationContextDebugger(transformer.getContext());
    
    log.error(debugger.dumpContextState());
    log.error(debugger.generateErrorSummary());
}
```

---

**Phase 3 Status: ✅ COMPLETE**  
**Build Status: ✅ SUCCESSFUL**  
**Ready for: Phase 4 (Output & Serialization)**