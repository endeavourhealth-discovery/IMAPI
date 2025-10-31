# Phase 4: Output and Serialization - Quick Reference

## Components Overview

### 1. Serializers
- **QuerySerializer** - Serializes Query objects
- **MatchSerializer** - Serializes Match conditions
- **PathSerializer** - Serializes Path hierarchies
- **ReturnSerializer** - Serializes Return clauses
- **GroupBySerializer** - Serializes GroupBy aggregations

### 2. Validation
- **QueryOutputValidator** - Validates Query before output
- **ValidationResult** - Contains validation results
- **ValidationError** - Represents individual validation issues

### 3. File Output
- **QueryOutputWriter** - Writes Query objects to JSON files
- **WriteResult** - Contains write operation results
- **OverwriteStrategy** - Enum for file overwrite strategies

---

## Quick Start Examples

### Validate a Query

```java
QueryOutputValidator validator = new QueryOutputValidator();
QueryOutputValidator.ValidationResult result = validator.validate(query);

if (!result.isValid()) {
    for (QueryOutputValidator.ValidationError error : result.getErrors()) {
        System.err.println("Error: " + error.getCode() + " - " + error.getMessage());
    }
}
```

### Write Query to File with Backup

```java
QueryOutputWriter writer = new QueryOutputWriter()
    .withOutputDirectory(Paths.get("output"))
    .withOverwriteStrategy(QueryOutputWriter.OverwriteStrategy.BACKUP)
    .withPrettyPrint(true);

QueryOutputWriter.WriteResult result = writer.write(query, "my-query");

if (result.isSuccess()) {
    System.out.println("File written: " + result.getOutputFile());
} else {
    System.err.println("Write failed: " + result.getErrorMessage());
}
```

### Serialize Query to JSON String

```java
ObjectMapper mapper = new ObjectMapper();
mapper.enable(SerializationFeature.INDENT_OUTPUT);

String json = mapper.writeValueAsString(query);
System.out.println(json);
```

---

## Validation Levels

### 1. Structure Validation
- Checks if Query has at least one content element
- Warns if Query is empty (no where, and, or, not, path, or queries)

### 2. Field Validation
- Verifies IRI is set
- Verifies name is set
- Checks Where clause has nodeRef
- Checks Return entries have nodeRef

### 3. Reference Validation
- Collects defined nodeRef from Where and Path
- Collects used nodeRef from Return
- Warns if Return uses undefined nodeRef

---

## File Output Strategies

### ALLOW Strategy
```java
.withOverwriteStrategy(QueryOutputWriter.OverwriteStrategy.ALLOW)
// Overwrites existing files without backup
```

### DENY Strategy
```java
.withOverwriteStrategy(QueryOutputWriter.OverwriteStrategy.DENY)
// Throws exception if file exists
```

### BACKUP Strategy (Default)
```java
.withOverwriteStrategy(QueryOutputWriter.OverwriteStrategy.BACKUP)
// Creates timestamped backup before overwriting
// Backup name: original_backup_YYYY-MM-DD_HH-mm-ss.json
```

---

## JSON Property Ordering

Query fields are serialized in this order:
```json
{
  "prefix": {...},
  "iri": "...",
  "name": "...",
  "description": "...",
  "query": [...],
  "activeOnly": false,
  "typeOf": {...},
  "isCohort": {...},
  "instanceOf": [...],
  "and": [...],
  "or": [...],
  "not": [...],
  "path": [...],
  "where": {...},
  "return": [...],
  "groupBy": [...],
  "dataSet": [...]
}
```

---

## Error Codes

### Validation Errors
- `QUERY_NULL` - Query is null
- `QUERY_EMPTY` - Query has no content
- `QUERY_IRI_MISSING` - Query IRI not set (warning)
- `QUERY_NAME_MISSING` - Query name not set (warning)
- `WHERE_NODEREF_MISSING` - Where nodeRef missing (warning)
- `RETURN_EMPTY` - Return clause is empty (warning)
- `RETURN_NODEREF_MISSING` - Return nodeRef missing (warning)
- `NODEREF_UNDEFINED` - Undefined nodeRef reference (warning)

---

## Configuration Options

```java
QueryOutputWriter writer = new QueryOutputWriter()
    // Set output directory
    .withOutputDirectory(Paths.get("output"))
    
    // Set overwrite strategy
    .withOverwriteStrategy(QueryOutputWriter.OverwriteStrategy.BACKUP)
    
    // Enable pretty-print (default: true)
    .withPrettyPrint(true)
    
    // Enable validation (default: true)
    .withValidation(true);
```

---

## Common Tasks

### Task: Write query with validation
```java
QueryOutputValidator validator = new QueryOutputValidator();
ValidationResult validationResult = validator.validate(query);

if (validationResult.isValid()) {
    QueryOutputWriter writer = new QueryOutputWriter()
        .withOutputDirectory(Paths.get("output"));
    WriteResult writeResult = writer.write(query, "result");
    
    if (!writeResult.isSuccess()) {
        throw new IOException(writeResult.getErrorMessage());
    }
}
```

### Task: Write multiple queries
```java
QueryOutputWriter writer = new QueryOutputWriter()
    .withOutputDirectory(Paths.get("output/batch"))
    .withValidation(false); // Disable validation for speed

for (Query q : queries) {
    writer.write(q, q.getName());
}
```

### Task: Get validation report
```java
QueryOutputValidator validator = new QueryOutputValidator();
ValidationResult result = validator.validate(query);

String report = result.getReport();
System.out.println(report);
```

---

## Performance Tips

1. **Disable validation for batch operations** - Set `.withValidation(false)`
2. **Disable pretty-print for smaller file size** - Set `.withPrettyPrint(false)`
3. **Reuse ObjectMapper** - Create once, use for multiple queries
4. **Use streaming for large result sets** - Consider chunking output

---

## File Output Process

```
Input Query
    ↓
[Validate] (if enabled)
    ↓
Create output directory (if needed)
    ↓
Check if file exists
    ├─ ALLOW: continue
    ├─ DENY: throw exception
    └─ BACKUP: create backup
    ↓
Write to temporary file
    ↓
Atomic move to target location
    ↓
Return WriteResult
    ├─ success: true
    ├─ outputFile: path to file
    └─ backupFile: path to backup (if created)
```

---

## Typical Integration

```java
// 1. Load QOF document
QOFDocumentLoader loader = new QOFDocumentLoader();
QOFDocument qofDoc = loader.load(inputFile);

// 2. Transform to IMQ
QOFToIMQTransformer transformer = new QOFToIMQTransformer();
Query query = transformer.transform(qofDoc);

// 3. Validate output (Phase 4)
QueryOutputValidator validator = new QueryOutputValidator();
if (!validator.validate(query).isValid()) {
    throw new Exception("Validation failed");
}

// 4. Write to file (Phase 4)
QueryOutputWriter writer = new QueryOutputWriter()
    .withOutputDirectory(Paths.get("output"))
    .withValidation(false); // Already validated

writer.write(query, qofDoc.getName());
```

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Validation warnings for missing IRI | Set IRI in Query before output |
| File permission denied | Check directory permissions |
| Backup not created | Enable BACKUP strategy and ensure write permissions |
| JSON contains null fields | This is expected, use JsonInclude.NON_DEFAULT |
| File write is slow | Disable pretty-print or validation |

---

## Files Location

- **Serializers:** `org.endeavourhealth.imapi.transformation.output`
- **Validation:** `org.endeavourhealth.imapi.transformation.output`
- **File Output:** `org.endeavourhealth.imapi.transformation.output`
- **Tests:** `org.endeavourhealth.imapi.transformation.output` (to be implemented)

---

## Next Steps

Phase 5: Batch Processing and Scalability
- Batch file processing
- Progress tracking
- Error resilience
- Performance optimization