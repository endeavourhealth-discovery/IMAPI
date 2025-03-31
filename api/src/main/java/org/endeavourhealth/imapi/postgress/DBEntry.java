package org.endeavourhealth.imapi.postgress;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
public class DBEntry {
  private UUID id;
  private String queryIri;
  private String queryName;
  private String userId;
  private LocalDateTime queuedAt;
  private LocalDateTime startedAt;
  private int pid;
  private LocalDateTime finishedAt;
  private LocalDateTime killedAt;
  private QueryExecutorStatus status;
  private String queryResult;

  public DBEntry setId(UUID id) {
    this.id = id;
    return this;
  }

  public DBEntry setQueryIri(String queryIri) {
    this.queryIri = queryIri;
    return this;
  }

  public DBEntry setQueryName(String queryName) {
    this.queryName = queryName;
    return this;
  }

  public DBEntry setUserId(String userId) {
    this.userId = userId;
    return this;
  }

  public DBEntry setQueuedAt(LocalDateTime queuedAt) {
    this.queuedAt = queuedAt;
    return this;
  }

  public DBEntry setStartedAt(LocalDateTime startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  public DBEntry setPid(int pid) {
    this.pid = pid;
    return this;
  }

  public DBEntry setFinishedAt(LocalDateTime finishedAt) {
    this.finishedAt = finishedAt;
    return this;
  }

  public DBEntry setKilledAt(LocalDateTime killedAt) {
    this.killedAt = killedAt;
    return this;
  }

  public DBEntry setStatus(QueryExecutorStatus status) {
    this.status = status;
    return this;
  }

  public DBEntry setQueryResult(String queryResult) {
    this.queryResult = queryResult;
    return this;
  }
}
