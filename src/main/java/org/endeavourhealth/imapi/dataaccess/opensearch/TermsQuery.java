package org.endeavourhealth.imapi.dataaccess.opensearch;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.utility.SharedObjectMapper;

import java.util.Collection;

public class TermsQuery extends OsQuery {
  private final String field;
  private final Collection<String> values;

  public TermsQuery(String field, Collection<String> values) {
    this.field = field;
    this.values = values;
  }

  @Override
  public ObjectNode toJson() {
    ObjectNode node = SharedObjectMapper.INSTANCE.createObjectNode();
    ArrayNode array = node.putObject("terms").putArray(field);
    values.forEach(array::add);
    return node;
  }
}
