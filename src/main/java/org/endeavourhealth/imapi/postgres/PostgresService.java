package org.endeavourhealth.imapi.postgres;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.postgres.DBEntry;
import org.endeavourhealth.imapi.model.postgres.QueryExecutorStatus;
import org.springframework.context.annotation.Lazy;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Lazy
@NoArgsConstructor
@Slf4j
public class PostgresService {
  public Pageable<DBEntry> getAllByUserId(UUID userId, int page, int size) throws SQLException, JsonProcessingException {
    Pageable<DBEntry> results = PostgresRepository.findAllByUserId(userId, page, size);
    int totalCount = PostgresRepository.getTotalCountByUserId(userId);
    results.setTotalCount(totalCount);
    return results;
  }

  public Pageable<DBEntry> findAllByStatus(QueryExecutorStatus status, int page, int size) throws SQLException, JsonProcessingException {
    Pageable<DBEntry> results = PostgresRepository.findAllByStatus(status, page, size);
    int totalCount = PostgresRepository.getTotalCountByStatus(status);
    results.setTotalCount(totalCount);
    return results;
  }

  public Pageable<DBEntry> findAllByUserIdAndStatus(String userId, QueryExecutorStatus status, int page, int size) throws SQLException, JsonProcessingException {
    Pageable<DBEntry> results = PostgresRepository.findAllByUserIdAndStatus(userId, status, page, size);
    int totalCount = PostgresRepository.getTotalCountByUserIdAndStatus(userId, status);
    results.setTotalCount(totalCount);
    return results;
  }

  public DBEntry getById(UUID id) throws SQLException, JsonProcessingException {
    Optional<DBEntry> dbEntry = PostgresRepository.findById(id);
    return dbEntry.orElse(null);
  }

  public void create(DBEntry dbEntry) throws SQLException {
    PostgresRepository.save(dbEntry);
  }

  public DBEntry update(DBEntry dbEntry) throws SQLException, JsonProcessingException {
    Optional<DBEntry> existingDbEntry = PostgresRepository.findById(dbEntry.getId());
    existingDbEntry.ifPresent(entry -> {
      if (null != entry.getQueuedAt()) dbEntry.setQueuedAt(entry.getQueuedAt());
      if (null != entry.getStartedAt()) dbEntry.setStartedAt(entry.getStartedAt());
      if (null != entry.getFinishedAt()) dbEntry.setFinishedAt(entry.getFinishedAt());
      if (null != entry.getKilledAt()) dbEntry.setKilledAt(entry.getKilledAt());
    });
    return PostgresRepository.save(dbEntry);
  }

  public void delete(UUID userId, UUID id) throws SQLException, JsonProcessingException {
    DBEntry entry = getById(id);
    if (!userId.equals(entry.getUserId())) {
      throw new IllegalArgumentException("Can only delete a query that belongs to the user making the request.");
    } else if (QueryExecutorStatus.QUEUED.equals(entry.getStatus()) || QueryExecutorStatus.RUNNING.equals(entry.getStatus())) {
      throw new IllegalArgumentException("Can only delete an item that has already been cancelled or has errored.");
    }
    PostgresRepository.deleteById(id);
  }

  public void cancelQuery(UUID id) throws SQLException, JsonProcessingException {
    DBEntry entry = getById(id);
    entry.setKilledAt(entry.getKilledAt());
    entry.setStatus(QueryExecutorStatus.CANCELLED);
    update(entry);
  }

}