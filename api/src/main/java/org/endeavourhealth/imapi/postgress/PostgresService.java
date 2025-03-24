package org.endeavourhealth.imapi.postgress;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
//@RequiredArgsConstructor
@Slf4j
public class PostgresService {
  private final PostgresRepository dbRepository;

  public PostgresService(PostgresRepository dbRepository) {
    this.dbRepository = dbRepository;
  }

//  public List<DBEntry> getAllByUserId(String userId) throws SQLException {
//    String sql = """
//      SELECT * FROM table WHERE user_id = quote_literal($1)
//      """;
//    List<String> bindings = new ArrayList<>();
//    bindings.add(userId);
//    return customSelectStatement(sql, bindings);
//  }

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

//  private List<DBEntry> customSelectStatement(String sql, List<String> bindings) throws SQLException {
//    try (PreparedStatement statement = PostgresConnectionManager.getConnection().prepareStatement(sql)) {
//      for (int i = 0; i < bindings.size(); i++) {
//        statement.setString(i + 1, bindings.get(i));
//      }
//      try (ResultSet rs = statement.executeQuery(sql)) {
//        List<DBEntry> dbEntries = new ArrayList<>();
//        while (rs.next()) {
//          DBEntry dbEntry = new DBEntry();
//          dbEntry.setId(rs.getObject("id", UUID.class));
//          dbEntry.setQueuedAt(LocalDateTime.parse(rs.getString("queued_at")));
//          dbEntry.setStartedAt(LocalDateTime.parse(rs.getString("started_at")));
//          dbEntry.setFinishedAt(LocalDateTime.parse(rs.getString("finished_at")));
//          dbEntry.setKilledAt(LocalDateTime.parse(rs.getString("killed_at")));
//          dbEntry.setQueryIri(rs.getString("query_iri"));
//          dbEntry.setUserId(rs.getString("user_id"));
//          dbEntry.setStatus(QueryExecutorStatus.valueOf(rs.getString("status")));
//          dbEntry.setQueryResult(rs.getString("query_result"));
//          dbEntries.add(dbEntry);
//        }
//        return dbEntries;
//      }
//    }
//  }
}