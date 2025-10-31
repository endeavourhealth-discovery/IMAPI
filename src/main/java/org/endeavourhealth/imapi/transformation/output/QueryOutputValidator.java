package org.endeavourhealth.imapi.transformation.output;

import org.endeavourhealth.imapi.model.imq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Validates Query output before serialization to ensure correctness and completeness.
 * 
 * Performs three levels of validation:
 * 1. Structure validation - Ensures the Query has required structural elements
 * 2. Field validation - Verifies required fields are present and have valid values
 * 3. Reference validation - Checks that all references (nodeRef, etc.) are consistent
 * 
 * Example usage:
 * <pre>
 * Query query = ...;
 * QueryOutputValidator validator = new QueryOutputValidator();
 * ValidationResult result = validator.validate(query);
 * 
 * if (!result.isValid()) {
 *   for (ValidationError error : result.getErrors()) {
 *     System.err.println(error.getMessage());
 *   }
 * }
 * </pre>
 * 
 * @see QueryOutputWriter
 */
public class QueryOutputValidator {
    
    private static final Logger logger = LoggerFactory.getLogger(QueryOutputValidator.class);
    private static final String SCHEMA_VERSION = "1.0";
    
    /**
     * Validates a Query object for output correctness.
     * 
     * @param query The Query to validate
     * @return ValidationResult containing any errors found
     */
    public ValidationResult validate(Query query) {
        logger.debug("Starting Query output validation");
        ValidationResult result = new ValidationResult();
        
        if (query == null) {
            result.addError("QUERY_NULL", "Query object is null", "Query");
            return result;
        }
        
        // Validate basic structure
        validateQueryStructure(query, result);
        
        // Validate required fields
        validateRequiredFields(query, result);
        
        // Validate references consistency
        validateReferences(query, result);
        
        if (result.isValid()) {
            logger.debug("Query output validation passed");
        } else {
            logger.warn("Query output validation found {} errors and {} warnings", 
                result.getErrors().size(), result.getWarnings().size());
        }
        
        return result;
    }
    
    /**
     * Validates the basic structure of a Query.
     * 
     * @param query The Query to validate
     * @param result The ValidationResult to accumulate errors
     */
    private void validateQueryStructure(Query query, ValidationResult result) {
        // Check if Query has at least one content element
        if (query.getWhere() == null && query.getQuery() == null && 
            query.getPath() == null && query.getAnd() == null &&
            query.getOr() == null && query.getNot() == null) {
            result.addWarning("QUERY_EMPTY", "Query has no content (no where, and, or, not, path, or nested queries)", "Query");
        }
    }
    
    /**
     * Validates that required fields are present in the Query.
     * 
     * @param query The Query to validate
     * @param result The ValidationResult to accumulate errors
     */
    private void validateRequiredFields(Query query, ValidationResult result) {
        // IRI is typically required for queries
        if (query.getIri() == null || query.getIri().trim().isEmpty()) {
            result.addWarning("QUERY_IRI_MISSING", "Query IRI is not set", "iri");
        }
        
        // Name is typically required for queries
        if (query.getName() == null || query.getName().trim().isEmpty()) {
            result.addWarning("QUERY_NAME_MISSING", "Query name is not set", "name");
        }
        
        // Validate Where clause if present
        if (query.getWhere() != null) {
            validateWhere(query.getWhere(), result, "Query.where");
        }
        
        // Validate Return clause if present
        if (query.getReturn() != null) {
            validateReturn(query.getReturn(), result, "Query.return");
        }
    }
    
    /**
     * Validates a Where clause.
     * 
     * @param where The Where clause to validate
     * @param result The ValidationResult to accumulate errors
     * @param path The path for error reporting
     */
    private void validateWhere(Where where, ValidationResult result, String path) {
        if (where.getNodeRef() == null || where.getNodeRef().trim().isEmpty()) {
            result.addWarning("WHERE_NODEREF_MISSING", "Where clause missing nodeRef", path + ".nodeRef");
        }
    }
    
    /**
     * Validates a Return clause.
     * 
     * @param returnClause The Return clause to validate
     * @param result The ValidationResult to accumulate errors
     * @param path The path for error reporting
     */
    private void validateReturn(Return returnClause, ValidationResult result, String path) {
        if (returnClause == null) {
            result.addWarning("RETURN_EMPTY", "Return clause is empty", path);
            return;
        }
        
        if (returnClause.getNodeRef() == null || returnClause.getNodeRef().trim().isEmpty()) {
            result.addWarning("RETURN_NODEREF_MISSING", "Return entry missing nodeRef", path + ".nodeRef");
        }
    }
    
    /**
     * Validates that all references in the Query are consistent.
     * 
     * @param query The Query to validate
     * @param result The ValidationResult to accumulate errors
     */
    private void validateReferences(Query query, ValidationResult result) {
        Set<String> definedNodeRefs = new HashSet<>();
        Set<String> usedNodeRefs = new HashSet<>();
        
        // Collect defined node references from Where clause
        if (query.getWhere() != null) {
            collectNodeRefsFromWhere(query.getWhere(), definedNodeRefs);
        }
        
        // Collect defined node references from Paths
        if (query.getPath() != null) {
            for (Path path : query.getPath()) {
                collectNodeRefsFromPath(path, definedNodeRefs);
            }
        }
        
        // Collect used node references from Return clause
        if (query.getReturn() != null) {
            Return ret = query.getReturn();
            if (ret.getNodeRef() != null) {
                usedNodeRefs.add(ret.getNodeRef());
            }
        }
        
        // Check for undefined node references
        for (String nodeRef : usedNodeRefs) {
            if (!definedNodeRefs.contains(nodeRef) && !nodeRef.isEmpty()) {
                result.addWarning("NODEREF_UNDEFINED", 
                    "Return references undefined nodeRef: " + nodeRef, 
                    "Query.return");
            }
        }
    }
    
    /**
     * Collects node references from a Where clause.
     * 
     * @param where The Where clause
     * @param nodeRefs Set to collect node references into
     */
    private void collectNodeRefsFromWhere(Where where, Set<String> nodeRefs) {
        if (where.getNodeRef() != null && !where.getNodeRef().isEmpty()) {
            nodeRefs.add(where.getNodeRef());
        }
    }
    
    /**
     * Collects node references from a Path.
     * 
     * @param path The Path
     * @param nodeRefs Set to collect node references into
     */
    private void collectNodeRefsFromPath(Path path, Set<String> nodeRefs) {
        if (path.getVariable() != null && !path.getVariable().isEmpty()) {
            nodeRefs.add(path.getVariable());
        }
        
        if (path.getPath() != null) {
            for (Path nestedPath : path.getPath()) {
                collectNodeRefsFromPath(nestedPath, nodeRefs);
            }
        }
    }
    
    /**
     * Inner class representing validation results.
     */
    public static class ValidationResult {
        private final List<ValidationError> errors = new ArrayList<>();
        private final List<ValidationError> warnings = new ArrayList<>();
        
        /**
         * Adds an error to the validation result.
         * 
         * @param code Error code
         * @param message Error message
         * @param field Field where error occurred
         */
        public void addError(String code, String message, String field) {
            errors.add(new ValidationError(code, message, field, true));
        }
        
        /**
         * Adds a warning to the validation result.
         * 
         * @param code Warning code
         * @param message Warning message
         * @param field Field where warning occurred
         */
        public void addWarning(String code, String message, String field) {
            warnings.add(new ValidationError(code, message, field, false));
        }
        
        /**
         * Checks if validation passed (no errors).
         * 
         * @return true if no errors, false otherwise
         */
        public boolean isValid() {
            return errors.isEmpty();
        }
        
        /**
         * Gets all errors.
         * 
         * @return List of ValidationError objects
         */
        public List<ValidationError> getErrors() {
            return new ArrayList<>(errors);
        }
        
        /**
         * Gets all warnings.
         * 
         * @return List of ValidationError objects
         */
        public List<ValidationError> getWarnings() {
            return new ArrayList<>(warnings);
        }
        
        /**
         * Gets total count of issues (errors + warnings).
         * 
         * @return Total issue count
         */
        public int getTotalIssues() {
            return errors.size() + warnings.size();
        }
        
        /**
         * Generates a human-readable report.
         * 
         * @return Formatted validation report
         */
        public String getReport() {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Validation Report ===\n");
            sb.append(String.format("Errors: %d, Warnings: %d\n", errors.size(), warnings.size()));
            
            if (!errors.isEmpty()) {
                sb.append("\nErrors:\n");
                for (ValidationError error : errors) {
                    sb.append(String.format("  [%s] %s (field: %s)\n", 
                        error.getCode(), error.getMessage(), error.getField()));
                }
            }
            
            if (!warnings.isEmpty()) {
                sb.append("\nWarnings:\n");
                for (ValidationError warning : warnings) {
                    sb.append(String.format("  [%s] %s (field: %s)\n", 
                        warning.getCode(), warning.getMessage(), warning.getField()));
                }
            }
            
            return sb.toString();
        }
    }
    
    /**
     * Inner class representing a validation error or warning.
     */
    public static class ValidationError {
        private final String code;
        private final String message;
        private final String field;
        private final boolean isError;
        
        /**
         * Constructs a ValidationError.
         * 
         * @param code Error/warning code
         * @param message Error/warning message
         * @param field Field where issue occurred
         * @param isError true if error, false if warning
         */
        public ValidationError(String code, String message, String field, boolean isError) {
            this.code = code;
            this.message = message;
            this.field = field;
            this.isError = isError;
        }
        
        /**
         * Gets the error code.
         * 
         * @return Error code
         */
        public String getCode() {
            return code;
        }
        
        /**
         * Gets the error message.
         * 
         * @return Error message
         */
        public String getMessage() {
            return message;
        }
        
        /**
         * Gets the field name.
         * 
         * @return Field name
         */
        public String getField() {
            return field;
        }
        
        /**
         * Checks if this is an error (vs warning).
         * 
         * @return true if error, false if warning
         */
        public boolean isError() {
            return isError;
        }
    }
}