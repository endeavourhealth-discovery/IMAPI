package org.endeavourhealth.imapi.transform.qofimq;

import org.endeavourhealth.imapi.transform.qofimq.QofBatchModels.Summary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class QofBatchIntegrationTest {

  @TempDir
  Path tempDir;

  private QofImqProperties props(Path input, Path output) {
    QofImqProperties p = new QofImqProperties();
    p.setInputPath(input.toString());
    p.setOutputPath(output.toString());
    p.setEmitJson(true);
    p.setParallelism(1);
    return p;
  }

  @Test
  void batchProcessesMixedDirectory_andWritesArtifacts() throws IOException {
    // Arrange: valid and invalid inputs
    Path in = Files.createDirectories(tempDir.resolve("in"));
    Path out = Files.createDirectories(tempDir.resolve("out"));

    Path ok = Files.writeString(in.resolve("ok.json"), "{\n  \"field\": \"QOF:age\", \n  \"op\": \"EQUALS\", \n  \"value\": 55\n}\n");
    Path bad = Files.writeString(in.resolve("bad.json"), "{ \n  \"operator\": \"OR\", \"operands\": [ { \"field\": \"QOF:x\", \"op\": \"EQUALS\", \"value\": 1 } ] }\n"); // OR needs >=2

    QofImqProperties p = props(in, out);

    QofImqFileService fs = new QofImqFileService(p);
    QofImqValidator validator = new QofImqValidator();
    QofAstParser parser = new QofAstParser();
    IriResolver iri = new IriResolver(p);
    QofAstToImqMapper mapper = new QofAstToImqMapper(iri);
    QofImqTransformer transformer = new QofImqTransformer(fs, validator, parser, mapper);
    QofImqSerializer serializer = new QofImqSerializer(p, new com.fasterxml.jackson.databind.ObjectMapper());

    QofBatchProcessor batch = new QofBatchProcessor(fs, transformer, serializer, p);

    // Act
    Summary s = batch.runBatch(false);

    // Assert
    assertEquals(2, s.total);
    assertEquals(1, s.success);
    assertEquals(1, s.failure);

    // Artifact exists for ok.json
    Path outFile = out.resolve("ok.imq.json");
    assertTrue(Files.exists(outFile), "Expected artifact for ok.json at " + outFile);
  }
}
