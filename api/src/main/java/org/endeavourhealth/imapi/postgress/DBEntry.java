package org.endeavourhealth.imapi.postgress;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "query_queue")
public class DBEntry {
  @Id
  private UUID id;
  private String queryIri;
  private String queryName;
  private String userId;
  private LocalDateTime queuedAt;
  private LocalDateTime startedAt;
  private int pid;
  private LocalDateTime finishedAt;
  private LocalDateTime killedAt;
  @Enumerated(EnumType.STRING)
  private QueryExecutorStatus status;
  private String queryResult;

  @PrePersist
  public void prePersist() {
    queuedAt = LocalDateTime.now();
  }
}
