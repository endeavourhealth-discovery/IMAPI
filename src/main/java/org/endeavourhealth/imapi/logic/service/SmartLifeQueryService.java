package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.postgres.DBEntry;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.smartlife.SmartLifeQueryRunDTO;
import org.endeavourhealth.imapi.postgres.PostgresService;

import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

public class SmartLifeQueryService {
  private final QueryService queryService = new QueryService();
  private final PostgresService postgresService = new PostgresService();

  public UUID runQuery(UUID userId, String userName, SmartLifeQueryRunDTO query) throws Exception {
    QueryRequest queryRequest = new QueryRequest();
    queryRequest.setQuery(queryService.getQueryFromIri(query.getQuery_id()));
    // Should this return an execution ID?
    return queryService.addToExecutionQueue(userId, userName, queryRequest);
  }

  public DBEntry getQueryInfo(UUID id) throws SQLException, JsonProcessingException {
    return postgresService.getById(id);
  }

  public void cancelQuery(UUID id) throws SQLException, JsonProcessingException {
    postgresService.cancelQuery(id);
  }

  public Set<String> getQueryResults(UUID id) throws SQLException {
    QueryRequest queryRequest = new QueryRequest();
    // This should be an execution id
    return queryService.getQueryResults(queryRequest);
  }
}
