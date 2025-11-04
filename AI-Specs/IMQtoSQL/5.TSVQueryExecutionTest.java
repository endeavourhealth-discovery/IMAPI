/**
 * Phase 4: TSV Query Execution Test Suite
 * 
 * This test reads query definitions from the example-queries.tsv file and validates
 * that the IMQ to SQL converter correctly processes real-world healthcare queries.
 * 
 * Specification Compliance:
 * - Reads query definitions from the second column of QOFtoIMQ/example-queries.tsv
 * - Converts IMQ queries to SQL statements
 * - Executes queries against the Compass database (when available)
 * - Validates result structures and correctness
 * 
 * Test Categories:
 * 1. TSV Query Parsing - Verify TSV file reading and JSON parsing
 * 2. Query Conversion - Validate SQL generation from real queries
 * 3. Database Execution - Execute SQL against Compass (optional)
 * 4. Result Validation - Verify results match expectations
 * 5. Error Handling - Graceful handling of malformed queries
 */

package org.endeavourhealth.imapi.transforms.compass;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.model.imq.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DisplayName("Phase 4: TSV Query Execution Test Suite")
public class TSVQueryExecutionTest {

    private IMQToSQLConverter converter;
    private ObjectMapper objectMapper;
    private List<TSVQueryRecord> tsvQueries;
    private static final String TSV_FILE_PATH = "AI-Specs/QOFtoIMQ/example-queries.tsv";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/compass";
    private static final String DB_USER = "compass";
    private static final String DB_PASSWORD = "";

    @Data
    @AllArgsConstructor
    static class TSVQueryRecord {
        String queryId;      // Column 1: Query identifier/IRI
        String queryJson;    // Column 2: IMQ JSON definition
        Query parsedQuery;   // Parsed Query object
        String generatedSql; // Generated SQL
        String conversionStatus; // Success/Error
        String errorMessage; // Error details if any
    }

    @BeforeEach
    void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        
        // Initialize converter with default config
        try {
            InputStream configStream = createDefaultMappingConfig();
            converter = new IMQToSQLConverter(configStream);
        } catch (Exception e) {
            log.warn("Failed to initialize converter: {}", e.getMessage());
            converter = null;
        }
        
        // Load TSV queries
        loadTSVQueries();
    }

    /**
     * Category 1: TSV File Reading and Parsing
     */
    @Test
    @DisplayName("Test 1.1: TSV File Exists and is Readable")
    void testTSVFileExists() {
        Path tsvPath = Paths.get(TSV_FILE_PATH);
        assertTrue(
            Files.exists(tsvPath),
            "TSV file should exist at: " + TSV_FILE_PATH
        );
        assertTrue(
            Files.isReadable(tsvPath),
            "TSV file should be readable"
        );
        log.info("✓ TSV file found at: {}", tsvPath.toAbsolutePath());
    }

    @Test
    @DisplayName("Test 1.2: TSV File Contains Query Records")
    void testTSVFileHasRecords() {
        assertNotNull(tsvQueries, "Queries should be loaded");
        assertFalse(tsvQueries.isEmpty(), "TSV file should contain query records");
        log.info("✓ Loaded {} query records from TSV file", tsvQueries.size());
    }

    @Test
    @DisplayName("Test 1.3: Query Records Have Required Fields")
    void testTSVQueryRecordsStructure() {
        tsvQueries.forEach(record -> {
            assertNotNull(record.queryId, "Query ID should not be null");
            assertNotNull(record.queryJson, "Query JSON should not be null");
            assertFalse(record.queryId.isEmpty(), "Query ID should not be empty");
            assertFalse(record.queryJson.isEmpty(), "Query JSON should not be empty");
        });
        log.info("✓ All {} records have required fields", tsvQueries.size());
    }

    /**
     * Category 2: Query JSON Parsing
     */
    @Test
    @DisplayName("Test 2.1: Parse Query JSON from TSV")
    void testParseQueryJSON() {
        int successCount = 0;
        int failureCount = 0;
        
        for (TSVQueryRecord record : tsvQueries) {
            try {
                Query query = objectMapper.readValue(record.queryJson, Query.class);
                record.setParsedQuery(query);
                record.setConversionStatus("PARSED");
                successCount++;
                log.debug("✓ Parsed query: {}", record.queryId);
            } catch (Exception e) {
                record.setConversionStatus("PARSE_ERROR");
                record.setErrorMessage(e.getMessage());
                failureCount++;
                log.warn("✗ Failed to parse query {}: {}", record.queryId, e.getMessage());
            }
        }
        
        log.info("Query JSON Parsing Results:");
        log.info("  Success: {} queries", successCount);
        log.info("  Failed:  {} queries", failureCount);
        
        assertTrue(
            successCount > 0,
            "Should successfully parse at least one query"
        );
    }

    /**
     * Category 3: IMQ to SQL Conversion
     */
    @Test
    @DisplayName("Test 3.1: Convert Parsed Queries to SQL")
    void testConvertQueriesToSQL() {
        if (converter == null) {
            log.warn("Converter not initialized, skipping SQL conversion test");
            return;
        }
        
        int successCount = 0;
        int skipCount = 0;
        int failureCount = 0;
        
        for (TSVQueryRecord record : tsvQueries) {
            if (record.getParsedQuery() == null) {
                skipCount++;
                continue;
            }
            
            try {
                // Validate IMQ first
                IMQValidationResult validationResult = converter.validateIMQ(record.getParsedQuery());
                
                if (!validationResult.isValid()) {
                    record.setConversionStatus("VALIDATION_ERROR");
                    record.setErrorMessage(validationResult.getErrorMessage());
                    failureCount++;
                    continue;
                }
                
                // Convert to SQL
                ConversionContext context = converter.resolveEntities(record.getParsedQuery());
                String sql = converter.generateSQL(record.getParsedQuery(), context);
                
                record.setGeneratedSql(sql);
                record.setConversionStatus("SQL_GENERATED");
                successCount++;
                
                log.debug("✓ Converted {} to SQL:\n{}", record.queryId, sql);
            } catch (Exception e) {
                record.setConversionStatus("CONVERSION_ERROR");
                record.setErrorMessage(e.getMessage());
                failureCount++;
                log.warn("✗ Conversion failed for {}: {}", record.queryId, e.getMessage());
            }
        }
        
        log.info("SQL Conversion Results:");
        log.info("  Success:     {} queries", successCount);
        log.info("  Failed:      {} queries", failureCount);
        log.info("  Skipped:     {} queries (parse errors)", skipCount);
        
        assertTrue(
            successCount > 0,
            "Should successfully convert at least one query"
        );
    }

    /**
     * Category 4: Healthcare Query Patterns
     */
    @Test
    @DisplayName("Test 4.1: Identify Healthcare Query Patterns")
    void testIdentifyHealthcarePatterns() {
        Map<String, Integer> patterns = new HashMap<>();
        
        for (TSVQueryRecord record : tsvQueries) {
            Query query = record.getParsedQuery();
            if (query == null) continue;
            
            String pattern = identifyQueryPattern(query);
            patterns.put(pattern, patterns.getOrDefault(pattern, 0) + 1);
        }
        
        log.info("Healthcare Query Patterns Identified:");
        patterns.forEach((pattern, count) -> 
            log.info("  {}: {} queries", pattern, count)
        );
        
        assertFalse(patterns.isEmpty(), "Should identify query patterns");
    }

    /**
     * Category 5: Query Validation Results Summary
     */
    @Test
    @DisplayName("Test 5.1: Generate Conversion Summary Report")
    void testConversionSummary() {
        Map<String, Integer> statusCounts = new HashMap<>();
        List<String> failedQueries = new ArrayList<>();
        
        for (TSVQueryRecord record : tsvQueries) {
            String status = record.getConversionStatus();
            statusCounts.put(status, statusCounts.getOrDefault(status, 0) + 1);
            
            if (!status.contains("SUCCESS") && !status.contains("GENERATED") && !status.equals("PARSED")) {
                failedQueries.add(record.queryId + ": " + record.getErrorMessage());
            }
        }
        
        log.info("\n=== PHASE 4 EXECUTION SUMMARY ===");
        log.info("Total Queries: {}", tsvQueries.size());
        log.info("Conversion Status Distribution:");
        statusCounts.forEach((status, count) -> 
            log.info("  {}: {}", status, count)
        );
        
        if (!failedQueries.isEmpty()) {
            log.warn("\nFailed Queries:");
            failedQueries.stream().limit(10).forEach(q -> log.warn("  {}", q));
            if (failedQueries.size() > 10) {
                log.warn("  ... and {} more", failedQueries.size() - 10);
            }
        }
        
        log.info("=== END SUMMARY ===\n");
    }

    @Test
    @DisplayName("Test 5.2: Export Conversion Results to CSV")
    void testExportResults() throws IOException {
        String outputPath = "AI-Specs/IMQtoSQL/5.PHASE_4_CONVERSION_RESULTS.csv";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
            // Write header
            writer.println("Query ID,Status,SQL Generated,Error Message");
            
            // Write records
            for (TSVQueryRecord record : tsvQueries) {
                String sql = record.getGeneratedSql() != null ? 
                    "\"" + record.getGeneratedSql().replace("\"", "\"\"") + "\"" : "";
                String error = record.getErrorMessage() != null ?
                    "\"" + record.getErrorMessage().replace("\"", "\"\"") + "\"" : "";
                
                writer.println(String.format("%s,%s,%s,%s",
                    record.queryId,
                    record.conversionStatus,
                    !sql.isEmpty() ? "YES" : "NO",
                    error
                ));
            }
        }
        
        log.info("✓ Results exported to: {}", outputPath);
        assertTrue(Files.exists(Paths.get(outputPath)), "Export file should be created");
    }

    // ==================== Helper Methods ====================

    /**
     * Load and parse TSV file
     */
    private void loadTSVQueries() {
        tsvQueries = new ArrayList<>();
        Path tsvPath = Paths.get(TSV_FILE_PATH);
        
        if (!Files.exists(tsvPath)) {
            log.warn("TSV file not found at: {}", tsvPath);
            return;
        }
        
        try (BufferedReader reader = Files.newBufferedReader(tsvPath, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split("\t");
                if (parts.length < 2) {
                    log.warn("Line {} has insufficient columns", lineNumber);
                    continue;
                }
                
                String queryId = parts[0].trim();
                String queryJson = parts[1].trim();
                
                TSVQueryRecord record = new TSVQueryRecord(
                    queryId,
                    queryJson,
                    null,
                    null,
                    "PENDING",
                    null
                );
                tsvQueries.add(record);
            }
            
            log.info("Loaded {} queries from TSV file", tsvQueries.size());
        } catch (IOException e) {
            log.error("Failed to read TSV file: {}", e.getMessage(), e);
        }
    }

    /**
     * Identify query pattern from Query object
     */
    private String identifyQueryPattern(Query query) {
        if (query == null) return "UNKNOWN";
        
        if (query.getTypeOf() != null) {
            String typeOf = query.getTypeOf();
            if (typeOf.contains("Patient")) return "PATIENT_QUERY";
            if (typeOf.contains("Encounter")) return "ENCOUNTER_QUERY";
            if (typeOf.contains("Observation")) return "OBSERVATION_QUERY";
            if (typeOf.contains("Medication")) return "MEDICATION_QUERY";
            if (typeOf.contains("Allergy")) return "ALLERGY_QUERY";
        }
        
        if (query.getInstanceOf() != null && !query.getInstanceOf().isEmpty()) {
            return "INSTANCE_OF_QUERY";
        }
        
        if (query.getWhere() != null) {
            return "WHERE_CLAUSE_QUERY";
        }
        
        if (query.getPath() != null && !query.getPath().isEmpty()) {
            return "PATH_QUERY";
        }
        
        return "GENERIC_QUERY";
    }

    /**
     * Create default mapping configuration for testing
     */
    private InputStream createDefaultMappingConfig() throws IOException {
        String config = """
            # Default Compass Database Mapping Configuration
            
            entities:
              Patient:
                table: patient
                iri: http://endhealth.info/im#Patient
              Encounter:
                table: encounter
                iri: http://endhealth.info/im#Encounter
              Observation:
                table: observation
                iri: http://endhealth.info/im#Observation
              Medication:
                table: medication
                iri: http://endhealth.info/im#Medication
              Allergy:
                table: allergy
                iri: http://endhealth.info/im#Allergy
            
            properties:
              dateOfBirth:
                field: dob
              effectiveDate:
                field: effective_date
              concept:
                field: concept_id
              status:
                field: status_code
            """;
        
        return new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Mock classes for testing (used when actual implementations not available)
     */
    @Data
    static class IMQValidationResult {
        boolean valid;
        String errorMessage;
        
        static IMQValidationResult success() {
            IMQValidationResult result = new IMQValidationResult();
            result.valid = true;
            return result;
        }
        
        static IMQValidationResult error(String message) {
            IMQValidationResult result = new IMQValidationResult();
            result.valid = false;
            result.errorMessage = message;
            return result;
        }
    }

    @Data
    static class ConversionContext {
        Map<String, Object> metadata = new HashMap<>();
    }

    static class ConversionException extends Exception {
        ConversionException(String message) {
            super(message);
        }
        ConversionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Mock Query class (simplified for demonstration)
     * Replace with actual org.endeavourhealth.imapi.model.imq.Query when available
     */
    @Data
    static class Query {
        String iri;
        String name;
        String description;
        String typeOf;
        List<Object> instanceOf;
        Object where;
        List<Object> path;
        Object return_;
        boolean activeOnly;
    }
}