package org.endeavourhealth.imapi.dataaccess.opensearch;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.utility.SharedObjectMapper;

public class PrefixQuery extends OsQuery {
  private final String field;
  private final String value;
  private boolean caseInsensitive;

  public PrefixQuery(String field, String value) {
    this.field = field;
    this.value = value;
  }

  public PrefixQuery caseInsensitive(boolean caseInsensitive) {
    this.caseInsensitive = caseInsensitive;
    return this;
  }

  @Override
  public ObjectNode toJson() {
    ObjectNode node = SharedObjectMapper.INSTANCE.createObjectNode();
    ObjectNode body = node.putObject("prefix").putObject(field);
    body.put("value", value);
    if (caseInsensitive) body.put("case_insensitive", true);
    return node;
  }
}
