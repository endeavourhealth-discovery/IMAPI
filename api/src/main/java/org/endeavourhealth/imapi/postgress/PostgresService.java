package org.endeavourhealth.imapi.postgress;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.model.postgres.DBEntry;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor
@Slf4j
public class PostgresService {
  public List<DBEntry> getAllByUserId(String userId) throws SQLException {
    return PostgresRepository.findAllByUserId(userId);
  }

  public List<DBEntry> findAllByStatus(QueryExecutorStatus status) throws SQLException {
    return PostgresRepository.findAllByStatus(status);
  }

  public List<DBEntry> findAllByUserIdAndStatus(String userId, QueryExecutorStatus status) throws SQLException {
    return PostgresRepository.findAllByUserIdAndStatus(userId, status);
  }

  public DBEntry getById(UUID id) throws SQLException {
    Optional<DBEntry> dbEntry = PostgresRepository.findById(id);
    return dbEntry.orElse(null);
  }

  public void create(DBEntry dbEntry) throws SQLException {
    PostgresRepository.save(dbEntry);
  }

  public DBEntry update(DBEntry dbEntry) throws SQLException {
    Optional<DBEntry> existingDbEntry = PostgresRepository.findById(dbEntry.getId());
    existingDbEntry.ifPresent(entry -> {
      if (null != entry.getQueuedAt()) dbEntry.setQueuedAt(entry.getQueuedAt());
      if (null != entry.getStartedAt()) dbEntry.setStartedAt(entry.getStartedAt());
      if (null != entry.getFinishedAt()) dbEntry.setFinishedAt(entry.getFinishedAt());
      if (null != entry.getKilledAt()) dbEntry.setKilledAt(entry.getKilledAt());
    });
    return PostgresRepository.save(dbEntry);
  }

  public void delete(UUID id) throws SQLException {
    PostgresRepository.deleteById(id);
  }

}