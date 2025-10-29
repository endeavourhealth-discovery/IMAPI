package org.endeavourhealth.imapi.transform.qofimq;

import org.endeavourhealth.imapi.model.imq.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Transformer entry point for converting QOF boolean-query JSON into IMQ objects.
 *
 * Phase 1: adds ingestion+validation pipeline; mapping will be added in Phase 2.
 */
@Component
public class QofImqTransformer {
  private static final Logger LOG = LoggerFactory.getLogger(QofImqTransformer.class);

  private final QofImqFileService fileService;
  private final QofImqValidator validator;

  public QofImqTransformer(QofImqFileService fileService, QofImqValidator validator) {
    this.fileService = fileService;
    this.validator = validator;
    LOG.debug("QofImqTransformer initialized (Phase 1 ingestion+validation ready)");
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
   * Future method: transform a JSON payload (string or model) into an IMQ {@link Query}.
   * Mapping will be implemented in Phase 2.
   */
  public Query transformPlaceholder() {
    LOG.warn("transformPlaceholder called — mapping not implemented yet");
    return null;
  }
}
