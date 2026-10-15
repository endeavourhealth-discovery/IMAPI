package org.endeavourhealth.imapi.dataaccess.opensearch;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.utility.SharedObjectMapper;

public class ExistsQuery extends OsQuery {
  private final String field;

  public ExistsQuery(String field) {
    this.field = field;
  }

  @Override
  public ObjectNode toJson() {
    ObjectNode node = SharedObjectMapper.INSTANCE.createObjectNode();
    node.putObject("exists").put("field", field);
    return node;
  }
}
