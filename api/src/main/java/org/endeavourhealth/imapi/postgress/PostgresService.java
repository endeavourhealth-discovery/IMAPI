package org.endeavourhealth.imapi.postgress;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostgresService {
  @Autowired
  private final PostgresRepository dbRepository;

  public List<DBEntry> getAllByUserId(String userId) {
    return dbRepository.findAllByUserId(userId);
  }

  public List<DBEntry> findAllByStatus(QueryExecutorStatus status) {
    return dbRepository.findAllByStatus(status);
  }

  public List<DBEntry> findAllByUserIdAndStatus(String userId, QueryExecutorStatus status) {
    return dbRepository.findAllByUserIdAndStatus(userId, status);
  }

  public DBEntry getById(UUID id) {
    Optional<DBEntry> dbEntry = dbRepository.findById(id);
    return dbEntry.orElse(null);
  }

  public DBEntry create(DBEntry dbEntry) {
    return dbRepository.save(dbEntry);
  }

  public DBEntry update(DBEntry dbEntry) {
    Optional<DBEntry> existingDbEntry = dbRepository.findById(dbEntry.getId());
    existingDbEntry.ifPresent(entry -> {
      if (null != entry.getQueuedAt()) dbEntry.setQueuedAt(entry.getQueuedAt());
      if (null != entry.getStartedAt()) dbEntry.setStartedAt(entry.getStartedAt());
      if (null != entry.getFinishedAt()) dbEntry.setFinishedAt(entry.getFinishedAt());
      if (null != entry.getKilledAt()) dbEntry.setKilledAt(entry.getKilledAt());
    });
    return dbRepository.save(dbEntry);
  }

  public void delete(UUID id) {
    dbRepository.deleteById(id);
  }

}