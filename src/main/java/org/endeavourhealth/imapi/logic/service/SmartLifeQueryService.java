package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.model.postgres.DBEntry;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.smartlife.SmartLifeQueryRunDTO;
import org.endeavourhealth.imapi.postgres.PostgresService;
import org.endeavourhealth.imapi.vocabulary.Graph;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class SmartLifeQueryService {
  private final QueryService queryService = new QueryService();
  private final PostgresService postgresService = new PostgresService();

  public UUID runQuery(UUID userId, String userName, SmartLifeQueryRunDTO query, List<Graph> graphs) throws Exception {
    QueryRequest queryRequest = new QueryRequest();
    queryRequest.setQuery(queryService.getQueryFromIri(query.getQuery_id(), graphs));
    // Should this return an execution ID?
    return queryService.addToExecutionQueue(userId, userName, queryRequest);
  }

  public DBEntry getQueryInfo(UUID id) throws SQLException, JsonProcessingException {
    return postgresService.getById(id);
  }

  public void cancelQuery(UUID id) throws SQLException, JsonProcessingException {
    postgresService.cancelQuery(id);
  }

  public List<String> getQueryResults(UUID id, List<Graph> graphs) throws SQLException, SQLConversionException {
    QueryRequest queryRequest = new QueryRequest();
    // This should be an execution id
    return queryService.getQueryResults(queryRequest, graphs);
  }
}
