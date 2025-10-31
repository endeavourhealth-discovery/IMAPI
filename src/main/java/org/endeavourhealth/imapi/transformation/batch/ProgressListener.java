package org.endeavourhealth.imapi.transformation.batch;

/**
 * Listener interface for batch transformation progress tracking.
 * Allows extensible progress reporting during batch processing operations.
 * 
 * @see BatchTransformationProcessor
 */
public interface ProgressListener {
    
    /**
     * Called when batch processing starts.
     * 
     * @param totalFiles Total number of files to process
     */
    void onBatchStart(int totalFiles);
    
    /**
     * Called when a file processing begins.
     * 
     * @param filename Name of the file being processed
     * @param currentIndex Current file index (1-based)
     * @param totalFiles Total files in batch
     */
    void onFileStart(String filename, int currentIndex, int totalFiles);
    
    /**
     * Called when a file is successfully processed.
     * 
     * @param filename Name of the file processed
     * @param currentIndex Current file index (1-based)
     * @param totalFiles Total files in batch
     */
    void onFileSuccess(String filename, int currentIndex, int totalFiles);
    
    /**
     * Called when a file processing fails.
     * 
     * @param filename Name of the file that failed
     * @param currentIndex Current file index (1-based)
     * @param totalFiles Total files in batch
     * @param error Error message
     */
    void onFileFailure(String filename, int currentIndex, int totalFiles, String error);
    
    /**
     * Called when batch processing completes.
     * 
     * @param successCount Number of successfully processed files
     * @param failureCount Number of failed files
     * @param totalFiles Total files processed
     */
    void onBatchComplete(int successCount, int failureCount, int totalFiles);
    
    /**
     * Gets the current progress percentage.
     * 
     * @return Progress percentage (0-100)
     */
    default int getProgressPercentage(int processed, int total) {
        return total > 0 ? (processed * 100) / total : 0;
    }
}