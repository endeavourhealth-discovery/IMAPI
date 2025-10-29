package org.endeavourhealth.imapi.transform.qofimq;

import org.endeavourhealth.imapi.model.imq.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Transformer entry point for converting QOF boolean-query JSON into IMQ objects.
 */
@Component
public class QofImqTransformer {
  private static final Logger LOG = LoggerFactory.getLogger(QofImqTransformer.class);

  private final QofImqFileService fileService;
  private final QofImqValidator validator;
  private final QofAstParser parser;
  private final QofAstToImqMapper mapper;

  public QofImqTransformer(QofImqFileService fileService, QofImqValidator validator, QofAstParser parser, QofAstToImqMapper mapper) {
    this.fileService = fileService;
    this.validator = validator;
    this.parser = parser;
    this.mapper = mapper;
    LOG.debug("QofImqTransformer initialized (ingestion+validation+mapping ready)");
  }

  /**
   * Discover input files and validate each, returning a map from file path to list of validation errors.
   * An empty list indicates the file passed validation and structural checks.
   */
  public Map<Path, List<ValidationError>> validateInputRoot() {
    Map<Path, List<ValidationError>> result = new LinkedHashMap<>();
    try {
      List<Path> files = fileService.discoverJsonFiles();
      for (Path p : files) {
        List<ValidationError> errs = validator.validateFile(p);
        result.put(p, errs);
      }
      LOG.info("Validated {} file(s)", files.size());
    } catch (Exception e) {
      LOG.error("Error during discovery/validation: {}", e.getMessage(), e);
    }
    return result;
  }

  /**
   * Transform a single JSON string payload to an IMQ Query.
   */
  public Query transform(String json) {
    var ast = parser.parse(json);
    return mapper.toImq(ast);
  }

  /**
   * Transform a file at the given path. The file is validated first; if validation errors exist,
   * a QofMappingException is thrown. Otherwise, it is read and transformed.
   */
  public Query transform(Path file) {
    List<ValidationError> errs = validator.validateFile(file);
    if (errs != null && !errs.isEmpty()) {
      throw new QofMappingException("Validation failed for file " + file + ": " + errs.size() + " error(s)");
    }
    try (BufferedReader br = fileService.openUtf8Reader(file)) {
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null) sb.append(line).append('\n');
      return transform(sb.toString());
    } catch (IOException e) {
      throw new QofMappingException("IO error reading file " + file + ": " + e.getMessage(), e);
    }
  }
}
