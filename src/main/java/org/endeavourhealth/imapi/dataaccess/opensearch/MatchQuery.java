package org.endeavourhealth.imapi.dataaccess.opensearch;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.utility.SharedObjectMapper;

public class MatchQuery extends OsQuery {
  private final String field;
  private final String text;
  private String analyzer;
  private Operator operator;
  private Fuzziness fuzziness;

  public MatchQuery(String field, String text) {
    this.field = field;
    this.text = text;
  }

  public MatchQuery analyzer(String analyzer) {
    this.analyzer = analyzer;
    return this;
  }

  public MatchQuery operator(Operator operator) {
    this.operator = operator;
    return this;
  }

  public MatchQuery fuzziness(Fuzziness fuzziness) {
    this.fuzziness = fuzziness;
    return this;
  }

  @Override
  public ObjectNode toJson() {
    ObjectNode node = SharedObjectMapper.INSTANCE.createObjectNode();
    ObjectNode body = node.putObject("match").putObject(field);
    body.put("query", text);
    if (analyzer != null) body.put("analyzer", analyzer);
    if (operator != null) body.put("operator", operator.name());
    if (fuzziness != null) body.put("fuzziness", fuzziness.value());
    return node;
  }
}
