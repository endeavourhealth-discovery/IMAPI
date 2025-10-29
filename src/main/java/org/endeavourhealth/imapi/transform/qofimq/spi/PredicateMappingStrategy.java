package org.endeavourhealth.imapi.transform.qofimq.spi;

import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.transform.qofimq.IriResolver;
import org.endeavourhealth.imapi.transform.qofimq.ast.PredicateNode;

/**
 * SPI for extending predicate mapping without modifying the core mapper.
 * Implementations can be discovered/injected by Spring or invoked manually.
 */
public interface PredicateMappingStrategy {
  /**
   * @return true if this strategy supports mapping the given predicate node.
   */
  boolean supports(PredicateNode node);

  /**
   * Map the given predicate node to an IMQ Match. Implementations may wrap results in NOT depending on node flags.
   */
  Match map(PredicateNode node, IriResolver iriResolver);
}
