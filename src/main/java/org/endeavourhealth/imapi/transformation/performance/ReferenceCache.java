package org.endeavourhealth.imapi.transformation.performance;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe cache for QOF to IMQ reference mappings.
 * Improves performance during batch transformations by caching previously resolved references.
 * 
 * <p>Supports configurable cache size and eviction policies.</p>
 * 
 * @see PerformanceProfiler
 */
public class ReferenceCache {
    
    private final ConcurrentHashMap<String, String> qofToImqMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> imqToQofMap = new ConcurrentHashMap<>();
    private final int maxSize;
    private volatile long hits = 0;
    private volatile long misses = 0;
    
    /**
     * Constructs a ReferenceCache with default max size of 10000.
     */
    public ReferenceCache() {
        this(10000);
    }
    
    /**
     * Constructs a ReferenceCache with specified max size.
     * 
     * @param maxSize Maximum cache size
     */
    public ReferenceCache(int maxSize) {
        this.maxSize = maxSize;
    }
    
    /**
     * Puts a QOF to IMQ reference mapping in the cache.
     * 
     * @param qofRef QOF reference identifier
     * @param imqRef IMQ reference identifier
     */
    public void putQofToImqMapping(String qofRef, String imqRef) {
        if (shouldEvict()) {
            clear();
        }
        qofToImqMap.put(qofRef, imqRef);
        imqToQofMap.put(imqRef, qofRef);
    }
    
    /**
     * Gets an IMQ reference for a QOF reference.
     * 
     * @param qofRef QOF reference identifier
     * @return IMQ reference identifier or null if not cached
     */
    public String getImqReference(String qofRef) {
        String result = qofToImqMap.get(qofRef);
        if (result != null) {
            hits++;
        } else {
            misses++;
        }
        return result;
    }
    
    /**
     * Gets a QOF reference for an IMQ reference.
     * 
     * @param imqRef IMQ reference identifier
     * @return QOF reference identifier or null if not cached
     */
    public String getQofReference(String imqRef) {
        String result = imqToQofMap.get(imqRef);
        if (result != null) {
            hits++;
        } else {
            misses++;
        }
        return result;
    }
    
    /**
     * Checks if a QOF reference is cached.
     * 
     * @param qofRef QOF reference identifier
     * @return true if cached, false otherwise
     */
    public boolean containsQofReference(String qofRef) {
        return qofToImqMap.containsKey(qofRef);
    }
    
    /**
     * Checks if an IMQ reference is cached.
     * 
     * @param imqRef IMQ reference identifier
     * @return true if cached, false otherwise
     */
    public boolean containsImqReference(String imqRef) {
        return imqToQofMap.containsKey(imqRef);
    }
    
    /**
     * Gets cache hit rate as percentage.
     * 
     * @return Hit rate (0-100)
     */
    public double getHitRate() {
        long total = hits + misses;
        return total > 0 ? (hits * 100.0) / total : 0.0;
    }
    
    /**
     * Gets total cache hits.
     * 
     * @return Number of cache hits
     */
    public long getHits() {
        return hits;
    }
    
    /**
     * Gets total cache misses.
     * 
     * @return Number of cache misses
     */
    public long getMisses() {
        return misses;
    }
    
    /**
     * Gets current cache size.
     * 
     * @return Number of entries in cache
     */
    public int getSize() {
        return qofToImqMap.size();
    }
    
    /**
     * Gets cache utilization percentage.
     * 
     * @return Utilization (0-100)
     */
    public double getUtilization() {
        return (getSize() * 100.0) / maxSize;
    }
    
    /**
     * Clears all cache entries and resets statistics.
     */
    public void clear() {
        qofToImqMap.clear();
        imqToQofMap.clear();
        hits = 0;
        misses = 0;
    }
    
    /**
     * Gets cache statistics as a formatted string.
     * 
     * @return Statistics string
     */
    public String getStatistics() {
        return String.format(
            "ReferenceCache [Size: %d/%d (%.1f%%), Hits: %d, Misses: %d, Hit Rate: %.1f%%]",
            getSize(), maxSize, getUtilization(), hits, misses, getHitRate()
        );
    }
    
    /**
     * Determines if cache should be evicted.
     * 
     * @return true if cache is at or over max size
     */
    private boolean shouldEvict() {
        return qofToImqMap.size() >= maxSize;
    }
}