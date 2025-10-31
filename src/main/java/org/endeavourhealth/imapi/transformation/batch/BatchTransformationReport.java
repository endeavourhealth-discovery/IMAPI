package org.endeavourhealth.imapi.transformation.batch;

import org.endeavourhealth.imapi.transformation.core.TransformationError;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Report class for batch transformation operations.
 * Tracks successful and failed transformations with detailed error information.
 * 
 * @see BatchTransformationProcessor
 */
public class BatchTransformationReport {
    
    private final LocalDateTime startTime;
    private LocalDateTime endTime;
    private final List<TransformationResult> results = new ArrayList<>();
    private int totalProcessed = 0;
    private int totalSuccessful = 0;
    private int totalFailed = 0;
    private long totalDurationMs = 0;
    
    /**
     * Constructs a new BatchTransformationReport.
     */
    public BatchTransformationReport() {
        this.startTime = LocalDateTime.now();
    }
    
    /**
     * Records a successful transformation result.
     * 
     * @param filename Name of the transformed file
     * @param outputPath Path where the output was written
     * @param durationMs Duration of transformation in milliseconds
     */
    public void recordSuccess(String filename, String outputPath, long durationMs) {
        results.add(new TransformationResult(filename, true, outputPath, durationMs, null));
        totalSuccessful++;
        totalProcessed++;
        totalDurationMs += durationMs;
    }
    
    /**
     * Records a failed transformation result.
     * 
     * @param filename Name of the file that failed
     * @param error TransformationError containing error details
     * @param durationMs Duration of transformation attempt in milliseconds
     */
    public void recordFailure(String filename, TransformationError error, long durationMs) {
        results.add(new TransformationResult(filename, false, null, durationMs, error));
        totalFailed++;
        totalProcessed++;
        totalDurationMs += durationMs;
    }
    
    /**
     * Marks the batch processing as complete.
     */
    public void complete() {
        this.endTime = LocalDateTime.now();
    }
    
    /**
     * Gets all transformation results.
     * 
     * @return List of TransformationResult objects
     */
    public List<TransformationResult> getResults() {
        return new ArrayList<>(results);
    }
    
    /**
     * Gets successful transformation results.
     * 
     * @return List of successful results
     */
    public List<TransformationResult> getSuccessfulResults() {
        return results.stream()
            .filter(TransformationResult::isSuccessful)
            .collect(Collectors.toList());
    }
    
    /**
     * Gets failed transformation results.
     * 
     * @return List of failed results
     */
    public List<TransformationResult> getFailedResults() {
        return results.stream()
            .filter(r -> !r.isSuccessful())
            .collect(Collectors.toList());
    }
    
    /**
     * Gets list of filenames that failed for retry.
     * 
     * @return List of failed filenames
     */
    public List<String> getFailedFilenames() {
        return getFailedResults().stream()
            .map(TransformationResult::getFilename)
            .collect(Collectors.toList());
    }
    
    /**
     * Gets total number of files processed.
     * 
     * @return Total processed count
     */
    public int getTotalProcessed() {
        return totalProcessed;
    }
    
    /**
     * Gets total number of successful transformations.
     * 
     * @return Successful count
     */
    public int getTotalSuccessful() {
        return totalSuccessful;
    }
    
    /**
     * Gets total number of failed transformations.
     * 
     * @return Failed count
     */
    public int getTotalFailed() {
        return totalFailed;
    }
    
    /**
     * Gets total duration of all transformations.
     * 
     * @return Duration in milliseconds
     */
    public long getTotalDurationMs() {
        return totalDurationMs;
    }
    
    /**
     * Gets average duration per file.
     * 
     * @return Average duration in milliseconds
     */
    public long getAverageDurationMs() {
        return totalProcessed > 0 ? totalDurationMs / totalProcessed : 0;
    }
    
    /**
     * Gets success rate percentage.
     * 
     * @return Success rate (0-100)
     */
    public double getSuccessRatePercentage() {
        return totalProcessed > 0 ? (totalSuccessful * 100.0) / totalProcessed : 0.0;
    }
    
    /**
     * Generates a formatted summary report.
     * 
     * @return Report summary string
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Batch Transformation Report ===\n");
        sb.append(String.format("Start Time: %s\n", startTime));
        if (endTime != null) {
            sb.append(String.format("End Time: %s\n", endTime));
        }
        sb.append(String.format("Total Files: %d\n", totalProcessed));
        sb.append(String.format("Successful: %d (%.1f%%)\n", totalSuccessful, getSuccessRatePercentage()));
        sb.append(String.format("Failed: %d\n", totalFailed));
        sb.append(String.format("Total Duration: %d ms\n", totalDurationMs));
        sb.append(String.format("Average Duration per File: %d ms\n", getAverageDurationMs()));
        
        if (!getFailedResults().isEmpty()) {
            sb.append("\nFailed Files:\n");
            for (TransformationResult result : getFailedResults()) {
                sb.append(String.format("  - %s: %s\n", result.getFilename(), 
                    result.getError() != null ? result.getError().getMessage() : "Unknown error"));
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Inner class representing a single transformation result.
     */
    public static class TransformationResult {
        private final String filename;
        private final boolean successful;
        private final String outputPath;
        private final long durationMs;
        private final TransformationError error;
        
        public TransformationResult(String filename, boolean successful, String outputPath, 
                                   long durationMs, TransformationError error) {
            this.filename = filename;
            this.successful = successful;
            this.outputPath = outputPath;
            this.durationMs = durationMs;
            this.error = error;
        }
        
        public String getFilename() {
            return filename;
        }
        
        public boolean isSuccessful() {
            return successful;
        }
        
        public String getOutputPath() {
            return outputPath;
        }
        
        public long getDurationMs() {
            return durationMs;
        }
        
        public TransformationError getError() {
            return error;
        }
    }
}