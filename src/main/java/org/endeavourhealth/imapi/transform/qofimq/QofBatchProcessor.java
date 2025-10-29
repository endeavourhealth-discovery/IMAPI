package org.endeavourhealth.imapi.transform.qofimq;

import org.endeavourhealth.imapi.model.imq.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.endeavourhealth.imapi.transform.qofimq.QofBatchModels.FileResult;
import static org.endeavourhealth.imapi.transform.qofimq.QofBatchModels.Summary;

/**
 * Phase 3: Batch processing over discovered files, with per-file timing and optional artifact emission.
 */
@Component
public class QofBatchProcessor {
  private static final Logger LOG = LoggerFactory.getLogger(QofBatchProcessor.class);

  private final QofImqFileService fileService;
  private final QofImqTransformer transformer;
  private final QofImqSerializer serializer;
  private final QofImqProperties props;

  public QofBatchProcessor(QofImqFileService fileService, QofImqTransformer transformer, QofImqSerializer serializer, QofImqProperties props) {
    this.fileService = fileService;
    this.transformer = transformer;
    this.serializer = serializer;
    this.props = props;
  }

  public Summary runBatch(boolean failFast) {
    Instant started = Instant.now();
    List<FileResult> results = new ArrayList<>();
    List<Path> files;
    try {
      files = fileService.discoverJsonFiles();
    } catch (Exception e) {
      LOG.error("Failed discovering files: {}", e.getMessage(), e);
      // Record a synthetic failure so summary isn't empty
      results.add(new FileResult(Path.of("<discover>"), false, 0, null, "Discovery error: " + e.getMessage()));
      return new Summary(started, Instant.now(), results);
    }

    LOG.info("Batch starting for {} file(s). Input root={}, Output root={} (emitJson={}, parallelism={})", files.size(), props.getInputPath(), props.getOutputPath(), props.isEmitJson(), props.getParallelism());

    int parallelism = Math.max(1, props.getParallelism());
    if (parallelism == 1) {
      // Sequential with strict fail-fast
      for (Path file : files) {
        FileResult fr = processOne(file);
        results.add(fr);
        if (!fr.success) {
          if (failFast) {
            LOG.warn("Fail-fast enabled, aborting batch after first failure.");
            break;
          }
        }
      }
    } else {
      // Parallel with bounded thread pool; best-effort fail-fast by cancelling remaining tasks on first failure
      ExecutorService pool = Executors.newFixedThreadPool(parallelism, r -> {
        Thread t = new Thread(r, "qof-batch-" + System.nanoTime());
        t.setDaemon(true);
        return t;
      });
      List<Future<FileResult>> futures = new ArrayList<>();
      for (Path file : files) {
        futures.add(pool.submit(() -> processOne(file)));
      }
      boolean sawFailure = false;
      for (Future<FileResult> f : futures) {
        try {
          FileResult fr = f.get();
          results.add(fr);
          if (!fr.success && failFast) {
            sawFailure = true;
            break;
          }
        } catch (InterruptedException ie) {
          Thread.currentThread().interrupt();
          results.add(new FileResult(Path.of("<interrupted>"), false, 0, null, "Interrupted"));
          sawFailure = true;
          break;
        } catch (ExecutionException ee) {
          Throwable ex = ee.getCause() != null ? ee.getCause() : ee;
          String msg = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
          results.add(new FileResult(Path.of("<future>"), false, 0, null, msg));
          LOG.warn("FAIL(parallel) -> {}", msg);
          if (LOG.isDebugEnabled()) LOG.debug("Stacktrace:", ex);
          if (failFast) {
            sawFailure = true;
            break;
          }
        }
      }
      if (sawFailure) {
        for (Future<FileResult> f : futures) f.cancel(true);
      }
      pool.shutdownNow();
    }

    Summary summary = new Summary(started, Instant.now(), results);
    LOG.info("Batch complete: total={}, success={}, failure={}, duration={} ms, output={}", summary.total, summary.success, summary.failure, summary.getDurationMillis(), props.getOutputPath());
    return summary;
  }

  private FileResult processOne(Path file) {
    String corr = java.util.UUID.randomUUID().toString();
    MDC.put("file", file.toString());
    MDC.put("corr", corr);
    long t0 = System.nanoTime();
    try {
      Query q = transformer.transform(file);
      long dur = (System.nanoTime() - t0) / 1_000_000L;
      if (props.isEmitJson()) {
        try {
          serializer.writeImqJson(file, q);
        } catch (Exception se) {
          LOG.warn("Artifact write failed for {}: {}", file, se.getMessage());
          if (LOG.isDebugEnabled()) LOG.debug("Artifact write stack:", se);
        }
      }
      LOG.info("OK  ({} ms) {}", dur, file);
      return new FileResult(file, true, dur, q, null);
    } catch (Exception ex) {
      long dur = (System.nanoTime() - t0) / 1_000_000L;
      String category = categorize(ex);
      String msg = category + ": " + (ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName());
      LOG.warn("FAIL({} ms) {} -> {}", dur, file, msg);
      if (LOG.isDebugEnabled()) LOG.debug("Stacktrace:", ex);
      return new FileResult(file, false, dur, null, msg);
    } finally {
      MDC.remove("file");
      MDC.remove("corr");
    }
  }

  private String categorize(Exception ex) {
    if (ex instanceof QofMappingException) {
      String m = ex.getMessage() != null ? ex.getMessage() : "";
      if (m.startsWith("Validation failed")) return "validation";
      return "mapping";
    } else if (ex instanceof QofParseException) {
      return "parse";
    } else if (ex instanceof IOException) {
      return "io";
    }
    return "error";
  }
}
