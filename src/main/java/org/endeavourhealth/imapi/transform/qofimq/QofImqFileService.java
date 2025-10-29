package org.endeavourhealth.imapi.transform.qofimq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Phase 1: File discovery and streamed reads for QOF boolean-query JSON files.
 */
@Component
public class QofImqFileService {
  private static final Logger LOG = LoggerFactory.getLogger(QofImqFileService.class);

  private final QofImqProperties props;

  public QofImqFileService(QofImqProperties props) {
    this.props = props;
  }

  /**
   * Recursively discovers all .json files under the configured input root.
   * Returns a deterministic, sorted list by path.
   */
  public List<Path> discoverJsonFiles() throws IOException {
    Path root = Paths.get(props.getInputPath());
    if (!Files.exists(root)) {
      LOG.warn("Input root does not exist: {}", root);
      return List.of();
    }
    try (Stream<Path> stream = Files.walk(root, Integer.MAX_VALUE, FileVisitOption.FOLLOW_LINKS)) {
      return stream
        .filter(Files::isRegularFile)
        .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".json"))
        .sorted()
        .collect(Collectors.toList());
    }
  }

  /**
   * Opens an InputStream for the given file with buffering and size safeguards.
   * Callers must close the returned stream.
   */
  public InputStream openJsonStream(Path file) throws IOException {
    long maxBytes = props.getMaxFileSizeBytes();
    long actualSize = Files.size(file);
    if (maxBytes > 0 && actualSize > maxBytes) {
      throw new IOException("File exceeds max size (" + actualSize + ">" + maxBytes + "): " + file);
    }
    return new BufferedInputStream(Files.newInputStream(file));
  }

  /**
   * Opens a UTF-8 reader for the given file with buffering and size safeguards.
   */
  public java.io.BufferedReader openUtf8Reader(Path file) throws IOException {
    long maxBytes = props.getMaxFileSizeBytes();
    long actualSize = Files.size(file);
    if (maxBytes > 0 && actualSize > maxBytes) {
      throw new IOException("File exceeds max size (" + actualSize + ">" + maxBytes + "): " + file);
    }
    return Files.newBufferedReader(file, java.nio.charset.StandardCharsets.UTF_8);
  }
}
