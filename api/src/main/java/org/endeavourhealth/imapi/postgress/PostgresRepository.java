package org.endeavourhealth.imapi.postgress;

import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.postgres.DBEntry;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class PostgresRepository {
  public PostgresRepository() {
    throw new IllegalStateException("Utility class");
  }

  public static DBEntry save(DBEntry entry) throws SQLException {
    String sql = """
      INSERT INTO
        query_queue(id, query_iri, query_name, user_id, queued_at, started_at, pid, finished_at, killed_at, status)
        VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10)
        ON CONFLICT(id)
          DO UPDATE SET (query_iri, query_name, user_id, queued_at, started_at, pid, finished_at, killed_at, status) = (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9);
      """;
    try (PreparedStatement ps = PostgresConnectionManager.prepareStatement(sql)) {
      ps.setString(1, entry.getId().toString());
      ps.setString(2, entry.getQueryIri());
      ps.setString(3, entry.getQueryName());
      ps.setString(4, entry.getUserId());
      ps.setString(5, entry.getQueuedAt().toString());
      ps.setString(6, entry.getStartedAt().toString());
      ps.setInt(7, entry.getPid());
      ps.setString(8, entry.getFinishedAt().toString());
      ps.setString(9, entry.getKilledAt().toString());
      ps.setString(10, entry.getStatus().toString());
      ps.executeUpdate();
      return entry;
    }
  }

  public static Optional<DBEntry> findById(UUID id) throws SQLException {
    try (PreparedStatement ps = PostgresConnectionManager.prepareStatement("SELECT * FROM query_queue WHERE id = ?")) {
      ps.setString(1, id.toString());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(resultSetToDBEntry(rs));
        } else {
          return Optional.empty();
        }
      }
    }
  }

  public static void deleteById(UUID id) throws SQLException {
    try (PreparedStatement ps = PostgresConnectionManager.prepareStatement("DELETE FROM query_queue WHERE id = ?")) {
      ps.setString(1, id.toString());
      ps.executeUpdate();
    }
  }

  public static Pageable<DBEntry> findAllByUserId(String userId, int page, int size) throws SQLException {
    StringJoiner stringJoiner = new StringJoiner("");
    stringJoiner.add("SELECT * FROM query_queue WHERE user_id = quote_literal(?1)");
    stringJoiner.add("ORDER BY id DESC");
    if (page != 0 && size != 0) stringJoiner.add(" LIMIT " + size + " OFFSET " + (page - 1) * size);
    try (PreparedStatement ps = PostgresConnectionManager.prepareStatement(stringJoiner.toString())) {
      ps.setString(1, userId);
      try (ResultSet rs = ps.executeQuery()) {
        Pageable<DBEntry> pageable = new Pageable<>();
        List<DBEntry> list = new ArrayList<>();
        while (rs.next()) {
          DBEntry entry = resultSetToDBEntry(rs);
          list.add(entry);
        }
        pageable.setResult(list);
        pageable.setCurrentPage(page);
        pageable.setPageSize(size);
        return pageable;
      }
    }
  }

  public static int getTotalCountByUserId(String userId) throws SQLException {
    String sql = "SELECT COUNT(query_iri) AS total FROM query_queue WHERE user_id = quote_literal(?1)";
    try (PreparedStatement ps = PostgresConnectionManager.prepareStatement(sql)) {
      ps.setString(1, userId);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("total");
        }
        return 0;
      }
    }
  }

  public static Pageable<DBEntry> findAllByStatus(QueryExecutorStatus status, int page, int size) throws SQLException {
    StringJoiner stringJoiner = new StringJoiner("");
    stringJoiner.add("SELECT * FROM query_queue WHERE status = ?1");
    if (page != 0 && size != 0) stringJoiner.add(" LIMIT " + size + " OFFSET " + (page - 1) * size);
    try (PreparedStatement ps = PostgresConnectionManager.prepareStatement(stringJoiner.toString())) {
      ps.setString(1, status.toString());
      try (ResultSet rs = ps.executeQuery()) {
        Pageable<DBEntry> pageable = new Pageable<>();
        List<DBEntry> list = new ArrayList<>();
        while (rs.next()) {
          DBEntry entry = resultSetToDBEntry(rs);
          list.add(entry);
        }
        pageable.setResult(list);
        pageable.setCurrentPage(page);
        pageable.setPageSize(size);
        return pageable;
      }
    }
  }

  public static int getTotalCountByStatus(QueryExecutorStatus status) throws SQLException {
    String sql = "SELECT COUNT(query_iri) AS total FROM query_queue WHERE status = quote_literal(?1)";
    try (PreparedStatement ps = PostgresConnectionManager.prepareStatement(sql)) {
      ps.setString(1, status.toString());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("total");
        }
        return 0;
      }
    }
  }

  public static Pageable<DBEntry> findAllByUserIdAndStatus(String userId, QueryExecutorStatus status, int page, int size) throws SQLException {
    try (PreparedStatement ps = PostgresConnectionManager.prepareStatement("SELECT * FROM query_queue WHERE user_id = quote_literal(?1) AND status = quote_literal(?2)")) {
      ps.setString(1, status.toString());
      try (ResultSet rs = ps.executeQuery()) {
        Pageable<DBEntry> pageable = new Pageable<>();
        List<DBEntry> list = new ArrayList<>();
        while (rs.next()) {
          DBEntry entry = resultSetToDBEntry(rs);
          list.add(entry);
        }
        pageable.setResult(list);
        pageable.setCurrentPage(page);
        pageable.setPageSize(size);
        return pageable;
      }
    }
  }

  public static int getTotalCountByUserIdAndStatus(String userId, QueryExecutorStatus status) throws SQLException {
    String sql = "SELECT COUNT(query_iri) AS total FROM query_queue WHERE user_id = quote_literal(?1) AND status = quote_literal(?2)";
    try (PreparedStatement ps = PostgresConnectionManager.prepareStatement(sql)) {
      ps.setString(1, userId);
      ps.setString(2, status.toString());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("total");
        }
        return 0;
      }
    }
  }

  private static DBEntry resultSetToDBEntry(ResultSet rs) throws SQLException {
    DBEntry entry = new DBEntry();
    entry.setId(UUID.fromString(rs.getString("id")));
    entry.setUserId(rs.getString("user_id"));
    entry.setUserName(rs.getString("user_name"));
    entry.setStatus(QueryExecutorStatus.valueOf(rs.getString("status")));
    entry.setQueuedAt(LocalDateTime.parse(rs.getString("queued_at")));
    if (null != rs.getString("started_at")) entry.setStartedAt(LocalDateTime.parse(rs.getString("started_at")));
    if (null != rs.getString("finished_at")) entry.setFinishedAt(LocalDateTime.parse(rs.getString("finished_at")));
    if (null != rs.getString("killed_at")) entry.setKilledAt(LocalDateTime.parse(rs.getString("killed_at")));
    entry.setQueryIri(rs.getString("query_iri"));
    entry.setQueryName(rs.getString("query_name"));
    return entry;
  }
}
