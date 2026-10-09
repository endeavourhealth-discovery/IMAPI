package org.endeavourhealth.imapi.dataaccess.opensearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.utility.SharedObjectMapper;

/**
 * A query clause of the OpenSearch query DSL. The classes in this package build the small subset of the DSL that IMAPI
 * uses as Jackson trees, so no search-engine client library is needed just to produce JSON.
 */
public abstract class OsQuery {

  /**
   * @return this clause as the JSON object that goes inside a "query", for example the object holding a "term" clause
   */
  public abstract ObjectNode toJson();

  @Override
  public String toString() {
    try {
      return SharedObjectMapper.INSTANCE.writerWithDefaultPrettyPrinter().writeValueAsString(toJson());
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Unable to write query as JSON", e);
    }
  }
}
