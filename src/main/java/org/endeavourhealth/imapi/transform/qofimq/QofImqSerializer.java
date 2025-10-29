package org.endeavourhealth.imapi.transform.qofimq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.endeavourhealth.imapi.model.imq.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Writes IMQ artifacts to disk when enabled.
 */
@Component
public class QofImqSerializer {
  private static final Logger LOG = LoggerFactory.getLogger(QofImqSerializer.class);

  private final QofImqProperties props;
  private final ObjectMapper mapper;

  public QofImqSerializer(QofImqProperties props, ObjectMapper mapper) {
    this.props = props;
    // Use a copy to ensure pretty printing for artifacts
    this.mapper = mapper.copy().enable(SerializationFeature.INDENT_OUTPUT);
  }

  /**
   * Writes the given Query as JSON to the output directory, preserving relative path from input root
   * and changing extension to .imq.json
   */
  public Path writeImqJson(Path inputFile, Query query) throws IOException {
    Path inputRoot = Paths.get(props.getInputPath()).toAbsolutePath().normalize();
    Path rel = inputRoot.relativize(inputFile.toAbsolutePath().normalize());
    String fileName = rel.getFileName().toString();
    int dot = fileName.lastIndexOf('.');
    String base = (dot > 0) ? fileName.substring(0, dot) : fileName;
    Path outRoot = Paths.get(props.getOutputPath());
    Path outPath = outRoot.resolve(rel).getParent();
    if (outPath == null) outPath = outRoot;
    Files.createDirectories(outPath);
    Path outFile = outPath.resolve(base + ".imq.json");
    mapper.writeValue(outFile.toFile(), query);
    LOG.debug("Wrote IMQ JSON artifact: {}", outFile);
    return outFile;
  }
}
