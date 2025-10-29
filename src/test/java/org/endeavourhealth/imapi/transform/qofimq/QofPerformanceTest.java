package org.endeavourhealth.imapi.transform.qofimq;

import org.endeavourhealth.imapi.transform.qofimq.QofBatchModels.Summary;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Lightweight performance test creating ~1,000 tiny files.
 * Disabled by default to avoid CI flakiness; enable locally when needed.
 */
public class QofPerformanceTest {

  @TempDir
  Path tempDir;

  @Test
  @Disabled("Performance test disabled by default; enable locally when profiling")
  void processesThousandSmallFilesUnderReasonableTime() throws IOException {
    Path in = Files.createDirectories(tempDir.resolve("in"));
    Path out = Files.createDirectories(tempDir.resolve("out"));

    // Generate ~1000 tiny valid inputs
    int total = 1000;
    for (int i = 0; i < total; i++) {
      int age = 18 + ThreadLocalRandom.current().nextInt(60);
      String json = "{\n  \"field\": \"QOF:age\", \n  \"op\": \"EQUALS\", \n  \"value\": " + age + "\n}\n";
      Files.writeString(in.resolve("f" + i + ".json"), json);
    }

    QofImqProperties p = new QofImqProperties();
    p.setInputPath(in.toString());
    p.setOutputPath(out.toString());
    p.setEmitJson(false);
    p.setParallelism(Runtime.getRuntime().availableProcessors());

    QofImqFileService fs = new QofImqFileService(p);
    QofImqValidator validator = new QofImqValidator();
    QofAstParser parser = new QofAstParser();
    IriResolver iri = new IriResolver(p);
    QofAstToImqMapper mapper = new QofAstToImqMapper(iri);
    QofImqTransformer transformer = new QofImqTransformer(fs, validator, parser, mapper);
    QofImqSerializer serializer = new QofImqSerializer(p, new com.fasterxml.jackson.databind.ObjectMapper());
    QofBatchProcessor batch = new QofBatchProcessor(fs, transformer, serializer, p);

    long t0 = System.currentTimeMillis();
    Summary s = batch.runBatch(false);
    long dur = System.currentTimeMillis() - t0;

    assertEquals(total, s.total);
    assertEquals(total, s.success);
    // Heuristic threshold: adjust as needed for environment
    assertTrue(dur < 10_000, "Expected to finish under 10s, took " + dur + "ms");
  }
}
