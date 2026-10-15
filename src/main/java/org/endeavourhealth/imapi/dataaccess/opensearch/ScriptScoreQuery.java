package org.endeavourhealth.imapi.dataaccess.opensearch;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.utility.SharedObjectMapper;

public class ScriptScoreQuery extends OsQuery {
  private final OsQuery query;
  private final Script script;

  public ScriptScoreQuery(OsQuery query, Script script) {
    this.query = query;
    this.script = script;
  }

  @Override
  public ObjectNode toJson() {
    ObjectNode node = SharedObjectMapper.INSTANCE.createObjectNode();
    ObjectNode body = node.putObject("script_score");
    body.set("query", query.toJson());
    body.set("script", script.toJson());
    return node;
  }
}
