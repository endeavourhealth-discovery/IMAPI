package org.endeavourhealth.imapi.dataaccess.opensearch;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.utility.SharedObjectMapper;

public class TermQuery extends OsQuery {
  private final String field;
  private final String value;

  public TermQuery(String field, String value) {
    this.field = field;
    this.value = value;
  }

  @Override
  public ObjectNode toJson() {
    ObjectNode node = SharedObjectMapper.INSTANCE.createObjectNode();
    node.putObject("term").putObject(field).put("value", value);
    return node;
  }
}
