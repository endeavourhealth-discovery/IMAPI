package org.endeavourhealth.imapi.transformation.performance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Performance profiling infrastructure for identifying transformation bottlenecks.
 * Tracks execution time for various transformation phases and provides performance metrics.
 * 
 * <p>Thread-safe and designed for minimal overhead.</p>
 * 
 * @see ReferenceCache
 */
public class PerformanceProfiler {
    
    private static final Logger logger = LoggerFactory.getLogger(PerformanceProfiler.class);
    
    private final ConcurrentHashMap<String, TimingStats> timings = new ConcurrentHashMap<>();
    private volatile long totalStartTime = 0;
    private volatile long totalEndTime = 0;
    private volatile boolean enabled = true;
    private final ReferenceCache cache = new ReferenceCache();
    
    /**
     * Starts profiling a named operation.
     * 
     * @param operationName Name of the operation to profile
     * @return Token for ending the operation
     */
    public OperationToken startOperation(String operationName) {
        if (!enabled) {
            return new OperationToken(operationName, 0);
        }
        
        long startTime = System.nanoTime();
        return new OperationToken(operationName, startTime);
    }
    
    /**
     * Ends profiling of a named operation.
     * 
     * @param token Operation token from startOperation
     */
    public void endOperation(OperationToken token) {
        if (!enabled || token.getStartTime() == 0) {
            return;
        }
        
        long endTime = System.nanoTime();
        long durationNs = endTime - token.getStartTime();
        
        timings.computeIfAbsent(token.getOperationName(), k -> new TimingStats())
            .recordTiming(durationNs);
    }
    
    /**
     * Records a custom timing measurement.
     * 
     * @param operationName Name of the operation
     * @param durationNs Duration in nanoseconds
     */
    public void recordTiming(String operationName, long durationNs) {
        if (!enabled) {
            return;
        }
        
        timings.computeIfAbsent(operationName, k -> new TimingStats())
            .recordTiming(durationNs);
    }
    
    /**
     * Marks the start of total profiling.
     */
    public void startTotal() {
        totalStartTime = System.nanoTime();
    }
    
    /**
     * Marks the end of total profiling.
     */
    public void endTotal() {
        totalEndTime = System.nanoTime();
    }
    
    /**
     * Gets timing statistics for a specific operation.
     * 
     * @param operationName Name of the operation
     * @return TimingStats or null if not found
     */
    public TimingStats getOperationStats(String operationName) {
        return timings.get(operationName);
    }
    
    /**
     * Gets all timing statistics.
     * 
     * @return Map of operation names to their statistics
     */
    public Map<String, TimingStats> getAllStats() {
        return new HashMap<>(timings);
    }
    
    /**
     * Gets the reference cache.
     * 
     * @return ReferenceCache instance
     */
    public ReferenceCache getReferenceCache() {
        return cache;
    }
    
    /**
     * Gets total elapsed time in milliseconds.
     * 
     * @return Total elapsed time
     */
    public long getTotalElapsedMs() {
        if (totalStartTime == 0 || totalEndTime == 0) {
            return 0;
        }
        return (totalEndTime - totalStartTime) / 1_000_000;
    }
    
    /**
     * Gets slowest operation by total time.
     * 
     * @return Operation name or null if no operations recorded
     */
    public String getSlowestOperation() {
        return timings.entrySet().stream()
            .max(Comparator.comparing(e -> e.getValue().getTotalTimeNs()))
            .map(Map.Entry::getKey)
            .orElse(null);
    }
    
    /**
     * Gets slowest operation by average time.
     * 
     * @return Operation name or null if no operations recorded
     */
    public String getSlowestAverageOperation() {
        return timings.entrySet().stream()
            .max(Comparator.comparing(e -> e.getValue().getAverageTimeNs()))
            .map(Map.Entry::getKey)
            .orElse(null);
    }
    
    /**
     * Gets a detailed performance report.
     * 
     * @return Performance report string
     */
    public String getPerformanceReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Performance Profile Report ===\n");
        sb.append(String.format("Total Elapsed Time: %d ms\n", getTotalElapsedMs()));
        sb.append("\nOperation Timings:\n");
        
        timings.entrySet().stream()
            .sorted((a, b) -> Long.compare(b.getValue().getTotalTimeNs(), a.getValue().getTotalTimeNs()))
            .forEach(entry -> {
                TimingStats stats = entry.getValue();
                sb.append(String.format("  %s:\n", entry.getKey()));
                sb.append(String.format("    Count: %d\n", stats.getCount()));
                sb.append(String.format("    Total: %.2f ms\n", stats.getTotalTimeMs()));
                sb.append(String.format("    Average: %.2f ms\n", stats.getAverageTimeMs()));
                sb.append(String.format("    Min: %.2f ms\n", stats.getMinTimeMs()));
                sb.append(String.format("    Max: %.2f ms\n", stats.getMaxTimeMs()));
            });
        
        sb.append("\n").append(cache.getStatistics()).append("\n");
        
        return sb.toString();
    }
    
    /**
     * Enables performance profiling.
     */
    public void enable() {
        enabled = true;
    }
    
    /**
     * Disables performance profiling.
     */
    public void disable() {
        enabled = false;
    }
    
    /**
     * Resets all profiling data.
     */
    public void reset() {
        timings.clear();
        totalStartTime = 0;
        totalEndTime = 0;
        cache.clear();
    }
    
    /**
     * Token class for tracking operation timing.
     */
    public static class OperationToken {
        private final String operationName;
        private final long startTime;
        
        public OperationToken(String operationName, long startTime) {
            this.operationName = operationName;
            this.startTime = startTime;
        }
        
        public String getOperationName() {
            return operationName;
        }
        
        public long getStartTime() {
            return startTime;
        }
    }
    
    /**
     * Statistics for operation timing measurements.
     */
    public static class TimingStats {
        private long count = 0;
        private long totalTimeNs = 0;
        private long minTimeNs = Long.MAX_VALUE;
        private long maxTimeNs = 0;
        
        /**
         * Records a timing measurement.
         * 
         * @param durationNs Duration in nanoseconds
         */
        public void recordTiming(long durationNs) {
            count++;
            totalTimeNs += durationNs;
            minTimeNs = Math.min(minTimeNs, durationNs);
            maxTimeNs = Math.max(maxTimeNs, durationNs);
        }
        
        public long getCount() {
            return count;
        }
        
        public long getTotalTimeNs() {
            return totalTimeNs;
        }
        
        public double getTotalTimeMs() {
            return totalTimeNs / 1_000_000.0;
        }
        
        public long getAverageTimeNs() {
            return count > 0 ? totalTimeNs / count : 0;
        }
        
        public double getAverageTimeMs() {
            return getAverageTimeNs() / 1_000_000.0;
        }
        
        public double getMinTimeMs() {
            return minTimeNs == Long.MAX_VALUE ? 0 : minTimeNs / 1_000_000.0;
        }
        
        public double getMaxTimeMs() {
            return maxTimeNs / 1_000_000.0;
        }
    }
}