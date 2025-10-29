package org.endeavourhealth.imapi.transform.qofimq;

import org.endeavourhealth.imapi.model.imq.Query;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Batch processing simple models: per-file result and overall summary.
 */
public final class QofBatchModels {
  private QofBatchModels() {}

  public static class FileResult {
    public final Path file;
    public final boolean success;
    public final long durationMillis;
    public final Query query; // present when success && query not null
    public final String errorMessage; // present when !success

    public FileResult(Path file, boolean success, long durationMillis, Query query, String errorMessage) {
      this.file = file;
      this.success = success;
      this.durationMillis = durationMillis;
      this.query = query;
      this.errorMessage = errorMessage;
    }
  }

  public static class Summary {
    public final Instant startedAt;
    public final Instant endedAt;
    public final List<FileResult> results;
    public final int total;
    public final int success;
    public final int failure;

    public Summary(Instant startedAt, Instant endedAt, List<FileResult> results) {
      this.startedAt = startedAt;
      this.endedAt = endedAt;
      this.results = Collections.unmodifiableList(new ArrayList<>(results));
      this.total = results.size();
      int ok = 0;
      for (FileResult r : results) if (r.success) ok++;
      this.success = ok;
      this.failure = this.total - this.success;
    }

    public long getDurationMillis() {
      return Duration.between(startedAt, endedAt).toMillis();
    }
  }
}
