package org.endeavourhealth.imapi.transformation.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.transformation.util.TransformationLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Writes Query objects to JSON files with comprehensive output handling.
 * 
 * Features:
 * - UTF-8 encoding for all output files
 * - Automatic output directory creation
 * - Configurable overwrite strategy (ALLOW, DENY, BACKUP)
 * - Atomic write operations (write to temp file, then rename)
 * - Automatic backup mechanism for existing files
 * - Rollback capability for failed writes
 * - Pretty-printed JSON output
 * 
 * Example usage:
 * <pre>
 * Query query = ...;
 * QueryOutputWriter writer = new QueryOutputWriter()
 *   .withOutputDirectory(Paths.get("/output"))
 *   .withOverwriteStrategy(OverwriteStrategy.BACKUP)
 *   .withPrettyPrint(true);
 * 
 * QueryOutputWriter.WriteResult result = writer.write(query, "output-name");
 * if (result.isSuccess()) {
 *   System.out.println("Written to: " + result.getOutputFile());
 * }
 * </pre>
 * 
 * @see QueryOutputValidator
 */
public class QueryOutputWriter {
    
    private static final Logger logger = LoggerFactory.getLogger(QueryOutputWriter.class);
    private static final DateTimeFormatter BACKUP_TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    private Path outputDirectory;
    private OverwriteStrategy overwriteStrategy = OverwriteStrategy.BACKUP;
    private boolean prettyPrint = true;
    private boolean validateBeforeWrite = true;
    private TransformationLogger transformationLogger;
    
    /**
     * Strategy for handling existing files during write operations.
     */
    public enum OverwriteStrategy {
        /** Allow overwriting existing files */
        ALLOW,
        
        /** Deny overwriting, throw exception if file exists */
        DENY,
        
        /** Create backup of existing file before overwriting */
        BACKUP
    }
    
    /**
     * Creates a new QueryOutputWriter with default settings.
     */
    public QueryOutputWriter() {
        this.transformationLogger = null;
    }
    
    /**
     * Creates a new QueryOutputWriter with transformation logger.
     * 
     * @param transformationLogger Logger for tracking operations
     */
    public QueryOutputWriter(TransformationLogger transformationLogger) {
        this.transformationLogger = transformationLogger;
    }
    
    /**
     * Sets the output directory for written files.
     * 
     * @param outputDirectory Path to output directory
     * @return This writer for method chaining
     */
    public QueryOutputWriter withOutputDirectory(Path outputDirectory) {
        this.outputDirectory = outputDirectory;
        return this;
    }
    
    /**
     * Sets the overwrite strategy.
     * 
     * @param strategy The overwrite strategy to use
     * @return This writer for method chaining
     */
    public QueryOutputWriter withOverwriteStrategy(OverwriteStrategy strategy) {
        this.overwriteStrategy = strategy;
        return this;
    }
    
    /**
     * Sets whether to pretty-print JSON output.
     * 
     * @param prettyPrint true to pretty-print, false for compact output
     * @return This writer for method chaining
     */
    public QueryOutputWriter withPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        return this;
    }
    
    /**
     * Sets whether to validate Query before writing.
     * 
     * @param validate true to validate, false to skip validation
     * @return This writer for method chaining
     */
    public QueryOutputWriter withValidation(boolean validate) {
        this.validateBeforeWrite = validate;
        return this;
    }
    
    /**
     * Writes a Query to a JSON file.
     * 
     * @param query The Query to write
     * @param outputName The base name for the output file (without extension)
     * @return WriteResult containing success status and file path
     */
    public WriteResult write(Query query, String outputName) {
        logger.info("Writing Query to file: {}", outputName);
        
        try {
            // Validate Query if configured
            if (validateBeforeWrite) {
                QueryOutputValidator validator = new QueryOutputValidator();
                QueryOutputValidator.ValidationResult validationResult = validator.validate(query);
                
                if (!validationResult.isValid()) {
                    String errorMsg = "Query validation failed:\n" + validationResult.getReport();
                    if (transformationLogger != null) {
                        transformationLogger.error(errorMsg);
                    }
                    return new WriteResult(false, null, "Validation failed: " + validationResult.getErrors().size() + " errors");
                }
            }
            
            // Ensure output directory exists
            ensureOutputDirectory();
            
            // Construct output file path
            Path outputFile = outputDirectory.resolve(outputName + ".json");
            
            // Handle existing file
            Path backupFile = null;
            if (Files.exists(outputFile)) {
                switch (overwriteStrategy) {
                    case DENY:
                        String denyMsg = "File already exists and overwrite strategy is DENY: " + outputFile;
                        logger.error(denyMsg);
                        return new WriteResult(false, outputFile, denyMsg);
                    
                    case BACKUP:
                        backupFile = createBackup(outputFile);
                        break;
                    
                    case ALLOW:
                        // Just overwrite, no backup
                        break;
                }
            }
            
            // Write to temporary file first (atomic operation)
            Path tempFile = Files.createTempFile(
                outputDirectory,
                outputName + "_",
                ".tmp"
            );
            
            try {
                writeToFile(query, tempFile);
                
                // Move temp file to final location
                Files.move(tempFile, outputFile, StandardCopyOption.REPLACE_EXISTING);
                logger.info("Query successfully written to: {}", outputFile);
                
                if (transformationLogger != null) {
                    transformationLogger.info("Query written to file: " + outputFile);
                }
                
                return new WriteResult(true, outputFile, null, backupFile);
                
            } catch (Exception e) {
                // Clean up temp file
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException ex) {
                    logger.warn("Failed to delete temporary file: {}", tempFile, ex);
                }
                
                // Restore backup if it was created
                if (backupFile != null && Files.exists(backupFile)) {
                    try {
                        Files.move(backupFile, outputFile, StandardCopyOption.REPLACE_EXISTING);
                        logger.info("Restored backup file: {}", backupFile);
                    } catch (IOException ex) {
                        logger.error("Failed to restore backup file: {}", backupFile, ex);
                    }
                }
                
                throw e;
            }
            
        } catch (Exception e) {
            String errorMsg = "Failed to write Query to file: " + e.getMessage();
            logger.error(errorMsg, e);
            
            if (transformationLogger != null) {
                transformationLogger.error(errorMsg + ": " + e.getClass().getSimpleName());
            }
            
            return new WriteResult(false, null, errorMsg);
        }
    }
    
    /**
     * Writes Query JSON content to a file.
     * 
     * @param query The Query to write
     * @param file The file to write to
     * @throws IOException If an I/O error occurs
     */
    private void writeToFile(Query query, Path file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        if (prettyPrint) {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        
        try (FileOutputStream fos = new FileOutputStream(file.toFile());
             OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
            
            String jsonContent = mapper.writeValueAsString(query);
            writer.write(jsonContent);
            writer.flush();
        }
    }
    
    /**
     * Ensures the output directory exists, creating it if necessary.
     * 
     * @throws IOException If directory creation fails
     */
    private void ensureOutputDirectory() throws IOException {
        if (outputDirectory == null) {
            outputDirectory = Paths.get(".");
        }
        
        if (!Files.exists(outputDirectory)) {
            Files.createDirectories(outputDirectory);
            logger.info("Created output directory: {}", outputDirectory);
        }
    }
    
    /**
     * Creates a backup of an existing file.
     * 
     * @param originalFile The file to backup
     * @return Path to the backup file
     * @throws IOException If backup creation fails
     */
    private Path createBackup(Path originalFile) throws IOException {
        String timestamp = LocalDateTime.now().format(BACKUP_TIMESTAMP);
        String backupName = originalFile.getFileName().toString()
            .replaceAll("\\.([^.]*)$", "_backup_" + timestamp + ".$1");
        
        Path backupFile = originalFile.getParent().resolve(backupName);
        Files.copy(originalFile, backupFile, StandardCopyOption.REPLACE_EXISTING);
        
        logger.info("Created backup file: {} -> {}", originalFile.getFileName(), backupName);
        return backupFile;
    }
    
    /**
     * Result of a write operation.
     */
    public static class WriteResult {
        private final boolean success;
        private final Path outputFile;
        private final String errorMessage;
        private final Path backupFile;
        
        /**
         * Constructs a successful WriteResult.
         * 
         * @param outputFile Path to the written file
         */
        WriteResult(Path outputFile) {
            this(true, outputFile, null, null);
        }
        
        /**
         * Constructs a successful WriteResult with backup.
         * 
         * @param success true if successful
         * @param outputFile Path to the written file
         * @param errorMessage Error message if failed
         * @param backupFile Path to backup file if created
         */
        WriteResult(boolean success, Path outputFile, String errorMessage, Path backupFile) {
            this.success = success;
            this.outputFile = outputFile;
            this.errorMessage = errorMessage;
            this.backupFile = backupFile;
        }
        
        /**
         * Constructs a failed WriteResult.
         * 
         * @param success false
         * @param outputFile Path that was attempted
         * @param errorMessage Error message
         */
        WriteResult(boolean success, Path outputFile, String errorMessage) {
            this(success, outputFile, errorMessage, null);
        }
        
        /**
         * Checks if write was successful.
         * 
         * @return true if successful
         */
        public boolean isSuccess() {
            return success;
        }
        
        /**
         * Gets the output file path.
         * 
         * @return Path to output file, or null if failed
         */
        public Path getOutputFile() {
            return outputFile;
        }
        
        /**
         * Gets the error message.
         * 
         * @return Error message if failed, null if successful
         */
        public String getErrorMessage() {
            return errorMessage;
        }
        
        /**
         * Gets the backup file path if one was created.
         * 
         * @return Path to backup file, or null if none created
         */
        public Path getBackupFile() {
            return backupFile;
        }
        
        /**
         * Checks if a backup was created.
         * 
         * @return true if backup exists
         */
        public boolean hasBackup() {
            return backupFile != null;
        }
    }
}