package org.endeavourhealth.imapi.transformation.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Console-based implementation of ProgressListener.
 * Logs batch transformation progress to console and logger.
 * 
 * @see ProgressListener
 * @see BatchTransformationProcessor
 */
public class ConsoleProgressListener implements ProgressListener {
    
    private static final Logger logger = LoggerFactory.getLogger(ConsoleProgressListener.class);
    private int currentIndex = 0;
    private int totalFiles = 0;
    
    @Override
    public void onBatchStart(int totalFiles) {
        this.totalFiles = totalFiles;
        this.currentIndex = 0;
        String message = String.format("Starting batch transformation of %d files", totalFiles);
        logger.info(message);
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║  Batch Transformation Started         ║");
        System.out.println("║  Total Files: " + String.format("%-21d", totalFiles) + "║");
        System.out.println("╚═══════════════════════════════════════╝");
    }
    
    @Override
    public void onFileStart(String filename, int currentIndex, int totalFiles) {
        this.currentIndex = currentIndex;
        int percentage = getProgressPercentage(currentIndex - 1, totalFiles);
        String message = String.format("[%d/%d - %d%%] Processing %s...", 
            currentIndex, totalFiles, percentage, filename);
        logger.debug(message);
    }
    
    @Override
    public void onFileSuccess(String filename, int currentIndex, int totalFiles) {
        int percentage = getProgressPercentage(currentIndex, totalFiles);
        String progressBar = buildProgressBar(currentIndex, totalFiles);
        String message = String.format("[%d/%d - %d%%] ✓ %s", 
            currentIndex, totalFiles, percentage, filename);
        logger.info(message);
        System.out.println(progressBar + " " + message);
    }
    
    @Override
    public void onFileFailure(String filename, int currentIndex, int totalFiles, String error) {
        int percentage = getProgressPercentage(currentIndex, totalFiles);
        String message = String.format("[%d/%d - %d%%] ✗ %s: %s", 
            currentIndex, totalFiles, percentage, filename, error);
        logger.error(message);
        System.out.println("  ✗ " + filename + " - " + error);
    }
    
    @Override
    public void onBatchComplete(int successCount, int failureCount, int totalFiles) {
        double successRate = totalFiles > 0 ? (successCount * 100.0) / totalFiles : 0.0;
        
        String summary = String.format(
            "Batch transformation complete: %d successful, %d failed, %.1f%% success rate",
            successCount, failureCount, successRate
        );
        logger.info(summary);
        
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║  Batch Transformation Complete        ║");
        System.out.println("║  Successful: " + String.format("%-24d", successCount) + "║");
        System.out.println("║  Failed:     " + String.format("%-24d", failureCount) + "║");
        System.out.println("║  Success Rate: " + String.format("%-21.1f%%", successRate) + "║");
        System.out.println("╚═══════════════════════════════════════╝");
    }
    
    /**
     * Builds a visual progress bar.
     * 
     * @param current Current progress
     * @param total Total items
     * @return Progress bar string
     */
    private String buildProgressBar(int current, int total) {
        int barLength = 20;
        int filledLength = (int) ((double) current / total * barLength);
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < barLength; i++) {
            bar.append(i < filledLength ? "=" : "-");
        }
        bar.append("]");
        return bar.toString();
    }
}