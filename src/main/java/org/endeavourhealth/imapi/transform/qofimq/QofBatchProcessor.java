package org.endeavourhealth.imapi.transform.qofimq;

import org.endeavourhealth.imapi.model.imq.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

    LOG.info("Batch starting for {} file(s). Input root={}, Output root={} (emitJson={})", files.size(), props.getInputPath(), props.getOutputPath(), props.isEmitJson());

    for (Path file : files) {
      long t0 = System.nanoTime();
      try {
        Query q = transformer.transform(file);
        long dur = (System.nanoTime() - t0) / 1_000_000L;
        if (props.isEmitJson()) {
          try {
            serializer.writeImqJson(file, q);
          } catch (Exception se) {
            LOG.warn("Failed writing artifact for {}: {}", file, se.getMessage());
          }
        }
        results.add(new FileResult(file, true, dur, q, null));
        LOG.info("OK  ({} ms) {}", dur, file);
      } catch (Exception ex) {
        long dur = (System.nanoTime() - t0) / 1_000_000L;
        String msg = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
        results.add(new FileResult(file, false, dur, null, msg));
        LOG.warn("FAIL({} ms) {} -> {}", dur, file, msg);
        if (failFast) {
          LOG.warn("Fail-fast enabled, aborting batch after first failure.");
          break;
        }
      }
    }

    Summary summary = new Summary(started, Instant.now(), results);
    LOG.info("Batch complete: total={}, success={}, failure={}, duration={} ms, output={}", summary.total, summary.success, summary.failure, summary.getDurationMillis(), props.getOutputPath());
    return summary;
  }
}
