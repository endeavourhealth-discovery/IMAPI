package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.protocol.HTTP;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;


@RestController
@CrossOrigin(origins = "*")
@Tag(name = "SmartLife Query Controller")
@RequestScope
@Slf4j
public class SmartLifeQueryController {

  @PostMapping(value = "query/run")
  @Operation(
    summary = "TODO",
    description = "Queues a predefined query for execution based on a given query_id and optional date parameters. " +
      "Returns a query_execution_id to track the execution status."
  )
  public ResponseEntity<Object> runSmartLifeQuery(@RequestBody String query) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("Query.RunSmartLifeQuery.POST")) {
      log.debug("runSmartLifeQuery");
      String query_execution_id = "";
      return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
  }

  @GetMapping(value = "/query/executions/{query_execution_id}")
  @Operation(
    summary = "TODO",
    description = "Returns the current status and metadata of a specific query execution, " +
      "including its position in the queue if not yet running."
  )
  public ResponseEntity<Object> getSmartLifeQuery(@PathVariable String query_execution_id) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("Query.GetSmartLifeQuery.POST")) {
      log.debug("getSmartLifeQuery");
      return new ResponseEntity<>(HttpStatus.OK);
    }
  }

  @PostMapping(value = "/query/executions/{query_execution_id}/cancel")
  @Operation(
    summary = "TODO",
    description = "Cancels a query execution that is in queued state. Has no effect if the execution is already completed, failed, or cancelled. " +
      "Will just be marked for cancellation and skipped when it gets picked up for execution."
  )
  public ResponseEntity<Object> cancelSmartLifeQuery(@PathVariable String query_execution_id) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("Query.CancelSmartLifeQuery.POST")) {
      log.debug("cancelSmartLifeQuery");
      return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
  }

  @GetMapping(value = "/query/executions/{query_execution_id}/results")
  @Operation(
    summary = "TODO",
    description = "Returns the result of a query execution once it has completed successfully."
  )
  public ResponseEntity<Object> getSmartLifeQueryResults(@PathVariable String query_execution_id) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("Query.GetSmartLifeQueryResults.POST")) {
      log.debug("getSmartLifeQueryResults");
      return new ResponseEntity<>(HttpStatus.OK);
    }
  }
}
