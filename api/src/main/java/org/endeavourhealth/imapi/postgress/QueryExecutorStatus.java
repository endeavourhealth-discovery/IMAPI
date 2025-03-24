package org.endeavourhealth.imapi.postgress;

public enum QueryExecutorStatus {
  QUEUED,
  RUNNING,
  COMPLETED,
  CANCELLED,
  ERRORED,
}
