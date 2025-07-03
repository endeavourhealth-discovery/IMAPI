package org.endeavourhealth.imapi.model.postgres;

public enum QueryExecutorStatus {
  QUEUED,
  RUNNING,
  COMPLETED,
  CANCELLED,
  ERRORED,
}
