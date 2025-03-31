package org.endeavourhealth.imapi.postgress;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor
@Slf4j
public class PostgresService {
  private final PostgresRepository dbRepository = new PostgresRepository();

  public List<DBEntry> getAllByUserId(String userId) throws SQLException {
    return dbRepository.findAllByUserId(userId);
  }

  public List<DBEntry> findAllByStatus(QueryExecutorStatus status) throws SQLException {
    return dbRepository.findAllByStatus(status);
  }

  public List<DBEntry> findAllByUserIdAndStatus(String userId, QueryExecutorStatus status) throws SQLException {
    return dbRepository.findAllByUserIdAndStatus(userId, status);
  }

  public DBEntry getById(UUID id) throws SQLException {
    Optional<DBEntry> dbEntry = dbRepository.findById(id);
    return dbEntry.orElse(null);
  }

  public void create(DBEntry dbEntry) throws SQLException {
    dbRepository.save(dbEntry);
  }

  public DBEntry update(DBEntry dbEntry) throws SQLException {
    Optional<DBEntry> existingDbEntry = dbRepository.findById(dbEntry.getId());
    existingDbEntry.ifPresent(entry -> {
      if (null != entry.getQueuedAt()) dbEntry.setQueuedAt(entry.getQueuedAt());
      if (null != entry.getStartedAt()) dbEntry.setStartedAt(entry.getStartedAt());
      if (null != entry.getFinishedAt()) dbEntry.setFinishedAt(entry.getFinishedAt());
      if (null != entry.getKilledAt()) dbEntry.setKilledAt(entry.getKilledAt());
    });
    return dbRepository.save(dbEntry);
  }

  public void delete(UUID id) throws SQLException {
    dbRepository.deleteById(id);
  }

}