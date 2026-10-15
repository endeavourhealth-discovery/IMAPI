package org.endeavourhealth.imapi.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Counts the database queries and connections made while handling a request, to find N+1 patterns.
 * <p>
 * Disabled unless {@code imapi.querycounter.threshold} is set above 0, in which case every {@code BaseDB} query is
 * recorded against the current thread and {@code QueryCounterConfig} logs a warning for requests that made at
 * least that many queries. When disabled the only cost is a volatile read per query.
 */
public final class QueryCounter {
  private static final int FINGERPRINT_LENGTH = 100;
  private static final Pattern WHITESPACE = Pattern.compile("\\s+");
  private static final ThreadLocal<Stats> CURRENT = new ThreadLocal<>();
  private static volatile int threshold = 0;

  private QueryCounter() {
  }

  public static void configure(int queriesThreshold) {
    threshold = Math.max(0, queriesThreshold);
    CURRENT.remove();
  }

  public static boolean isEnabled() {
    return threshold > 0;
  }

  public static int getThreshold() {
    return threshold;
  }

  /**
   * Begins counting for the current thread (normally at the start of a request).
   */
  public static void start() {
    if (isEnabled()) CURRENT.set(new Stats());
    else CURRENT.remove();
  }

  /**
   * Stops counting for the current thread.
   *
   * @return the counts, or null if counting was not active
   */
  public static Stats finish() {
    Stats stats = CURRENT.get();
    CURRENT.remove();
    return stats;
  }

  public static void connectionOpened() {
    Stats stats = isEnabled() ? CURRENT.get() : null;
    if (stats != null) stats.connections++;
  }

  public static void queryPrepared(String sparql) {
    Stats stats = isEnabled() ? CURRENT.get() : null;
    if (stats != null) stats.add(fingerprint(sparql));
  }

  private static String fingerprint(String sparql) {
    String compact = sparql == null ? "" : WHITESPACE.matcher(sparql.trim()).replaceAll(" ");
    return compact.length() > FINGERPRINT_LENGTH ? compact.substring(0, FINGERPRINT_LENGTH) : compact;
  }

  public static final class Stats {
    private int queries;
    private int connections;
    private final Map<String, Integer> byQuery = new HashMap<>();

    private void add(String fingerprint) {
      queries++;
      byQuery.merge(fingerprint, 1, Integer::sum);
    }

    public int getQueries() {
      return queries;
    }

    public int getConnections() {
      return connections;
    }

    /**
     * The most frequently repeated queries (by their first characters), e.g. "3x SELECT ?o ?oname ...".
     */
    public String topQueries(int limit) {
      return byQuery.entrySet().stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
        .limit(limit)
        .map(e -> e.getValue() + "x [" + e.getKey() + "]")
        .collect(Collectors.joining(", "));
    }
  }
}
