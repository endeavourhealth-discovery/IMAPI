package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.service.CasdoorService;
import org.endeavourhealth.imapi.logic.service.SmartLifeQueryService;
import org.endeavourhealth.imapi.model.admin.User;
import org.endeavourhealth.imapi.model.postgres.DBEntry;
import org.endeavourhealth.imapi.model.smartlife.SmartLifeQueryRunDTO;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;


@RestController
@RequestMapping("query")
@CrossOrigin(origins = "*")
@Tag(name = "SmartLife Query Controller")
@RequestScope
@Slf4j
public class SmartLifeQueryController {

  private final SmartLifeQueryService smartLifeQueryService = new SmartLifeQueryService();
  private final CasdoorService casdoorService = new CasdoorService();

  @PostMapping(value = "run")
  @Operation(
    summary = "TODO",
    description = "Queues a predefined query for execution based on a given query_id and optional date parameters. " +
      "Returns a query_execution_id to track the execution status."
  )
  public UUID runSmartLifeQuery(
    @RequestBody SmartLifeQueryRunDTO query,
    HttpSession session
  ) throws Exception {
    try (MetricsTimer t = MetricsHelper.recordTime("Query.RunSmartLifeQuery.POST")) {
      log.debug("runSmartLifeQuery");
      User user = casdoorService.getUser(session);
      UUID userId = UUID.fromString(user.getId());
      return smartLifeQueryService.runQuery(userId, user.getUsername(), query);
    }
  }

  @GetMapping(value = "executions/{queryExecutionId}")
  @Operation(
    summary = "TODO",
    description = "Returns the current status and metadata of a specific query execution, " +
      "including its position in the queue if not yet running."
  )
  public DBEntry getSmartLifeQueryInfo(@PathVariable String queryExecutionId) throws IOException, SQLException {
    try (MetricsTimer t = MetricsHelper.recordTime("Query.GetSmartLifeQuery.POST")) {
      log.debug("getSmartLifeQueryInfo");
      return smartLifeQueryService.getQueryInfo(UUID.fromString(queryExecutionId));
    }
  }

  @PostMapping(value = "executions/{queryExecutionId}/cancel")
  @Operation(
    summary = "TODO",
    description = "Cancels a query execution that is in queued state. Has no effect if the execution is already completed, failed, or cancelled. " +
      "Will just be marked for cancellation and skipped when it gets picked up for execution."
  )
  public ResponseEntity<Object> cancelSmartLifeQuery(@PathVariable String queryExecutionId) throws IOException, SQLException {
    try (MetricsTimer t = MetricsHelper.recordTime("Query.CancelSmartLifeQuery.POST")) {
      log.debug("cancelSmartLifeQuery");
      smartLifeQueryService.cancelQuery(UUID.fromString(queryExecutionId));
      return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
  }

  @GetMapping(value = "executions/{queryExecutionId}/results")
  @Operation(
    summary = "TODO",
    description = "Returns the result of a query execution once it has completed successfully."
  )
  public Set<String> getSmartLifeQueryResults(
    HttpServletRequest request,
    @PathVariable String queryExecutionId
  ) throws SQLException {
    try (MetricsTimer t = MetricsHelper.recordTime("Query.GetSmartLifeQueryResults.POST")) {
      log.debug("getSmartLifeQueryResults");
      return smartLifeQueryService.getQueryResults(UUID.fromString(queryExecutionId));
    }
  }
}
