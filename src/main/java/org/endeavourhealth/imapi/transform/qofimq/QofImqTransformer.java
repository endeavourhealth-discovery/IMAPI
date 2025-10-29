package org.endeavourhealth.imapi.transform.qofimq;

import org.endeavourhealth.imapi.model.imq.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Placeholder transformer entry point for converting QOF boolean-query JSON into IMQ objects.
 *
 * Phase 0 scaffold only — real implementation will be added in Phase 1/2 tasks.
 */
@Component
public class QofImqTransformer {
  private static final Logger LOG = LoggerFactory.getLogger(QofImqTransformer.class);

  public QofImqTransformer() {
    LOG.debug("QofImqTransformer initialized (Phase 0 scaffold)");
  }

  /**
   * Future method: transform a JSON payload (string or model) into an IMQ {@link Query}.
   * This method is intentionally left unimplemented in Phase 0.
   */
  public Query transformPlaceholder() {
    LOG.warn("transformPlaceholder called — no-op in Phase 0");
    return null;
  }
}
