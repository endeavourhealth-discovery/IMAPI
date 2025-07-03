package org.endeavourhealth.imapi.postgress;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.imq.QueryRequest;
import org.endeavourhealth.imapi.model.postgres.DBEntry;
import org.endeavourhealth.imapi.model.postgres.QueryExecutorStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class PostgresRepository {
  private static ObjectMapper om = new ObjectMapper();

  public PostgresRepository() {
    throw new IllegalStateException("Utility class");
  }

  public static DBEntry save(DBEntry entry) throws SQLException {
    String sql = """
      INSERT INTO
        query_queue(id, query_iri, query_name, query_request, user_id, user_name, queued_at, started_at, pid, finished_at, killed_at, status, error)
        VALUES (?, ?, ?, ?::jsonb, ?, ?, ?::timestamp, ?::timestamp, ?, ?::timestamp, ?::timestamp, ?, ?)
        ON CONFLICT(id)
          DO UPDATE SET (query_iri, query_name, query_request, user_id, user_name, queued_at, started_at, pid, finished_at, killed_at, status, error) = (?, ?, ?::jsonb, ?, ?, ?::timestamp, ?::timestamp, ?, ?::timestamp, ?::timestamp, ?, ?);
      """;
    try (PreparedStatement ps = PostgresConnectionManager.prepareStatement(sql)) {
      ps.setObject(1, entry.getId());
      ps.setString(2, entry.getQueryIri());
      ps.setString(3, entry.getQueryName());
      ps.setString(4, om.writeValueAsString(entry.getQueryRequest()));
      ps.setObject(5, entry.getUserId());
      ps.setString(6, entry.getUserName());
      ps.setTimestamp(7, Timestamp.valueOf(entry.getQueuedAt()));
      if (null != entry.getStartedAt()) ps.setTimestamp(8, Timestamp.valueOf(entry.getStartedAt()));
      else ps.setNull(8, java.sql.Types.VARCHAR);
      ps.setInt(9, entry.getPid());
      if (null != entry.getFinishedAt()) ps.setTimestamp(10, Timestamp.valueOf(entry.getFinishedAt()));
      else ps.setNull(10, java.sql.Types.VARCHAR);
      if (null != entry.getKilledAt()) ps.setTimestamp(11, Timestamp.valueOf(entry.getKilledAt()));
      else ps.setNull(11, java.sql.Types.VARCHAR);
      ps.setString(12, entry.getStatus().toString());
      ps.setString(13, entry.getError());
      ps.setString(14, entry.getQueryIri());
      ps.setString(15, entry.getQueryName());
      ps.setString(16, om.writeValueAsString(entry.getQueryRequest()));
      ps.setObject(17, entry.getUserId());
      ps.setString(18, entry.getUserName());
      ps.setTimestamp(19, Timestamp.valueOf(entry.getQueuedAt()));
      if (null != entry.getStartedAt()) ps.setTimestamp(20, Timestamp.valueOf(entry.getStartedAt()));
      else ps.setNull(20, java.sql.Types.VARCHAR);
      ps.setInt(21, entry.getPid());
      if (null != entry.getFinishedAt()) ps.setTimestamp(22, Timestamp.valueOf(entry.getFinishedAt()));
      else ps.setNull(22, java.sql.Types.VARCHAR);
      if (null != entry.getKilledAt()) ps.setTimestamp(23, Timestamp.valueOf(entry.getKilledAt()));
      else ps.setNull(23, java.sql.Types.VARCHAR);
      ps.setString(24, entry.getStatus().toString());
      ps.setString(25, entry.getError());
      ps.executeUpdate();
      return entry;
    } catch (SQLException | JsonProcessingException e) {
      throw new SQLException("Error saving query entry", e);
    }
  }

  public static Optional<DBEntry> findById(UUID id) throws SQLException, JsonProcessingException {
    try (PreparedStatement ps = PostgresConnectionManager.prepareStatement("SELECT * FROM query_queue WHERE id = ?")) {
      ps.setObject(1, id);
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
      ps.setObject(1, id);
      ps.executeUpdate();
    }
  }

  public static Pageable<DBEntry> findAllByUserId(UUID userId, int page, int size) throws SQLException, JsonProcessingException {
    StringJoiner stringJoiner = new StringJoiner(" ");
    stringJoiner.add("SELECT * FROM query_queue WHERE user_id = ?");
    stringJoiner.add("ORDER BY id DESC");
    if (page != 0 && size != 0) stringJoiner.add("LIMIT ? OFFSET ?");
    try (PreparedStatement ps = PostgresConnectionManager.prepareStatement(stringJoiner.toString())) {
      ps.setObject(1, userId);
      if (page != 0 && size != 0) {
        ps.setInt(2, size);
        ps.setInt(3, (page - 1) * size);
      }
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

  public static int getTotalCountByUserId(UUID userId) throws SQLException {
    String sql = "SELECT COUNT(query_iri) AS total FROM query_queue WHERE user_id = ?";
    try (PreparedStatement ps = PostgresConnectionManager.prepareStatement(sql)) {
      ps.setObject(1, userId);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("total");
        }
        return 0;
      }
    }
  }

  public static Pageable<DBEntry> findAllByStatus(QueryExecutorStatus status, int page, int size) throws SQLException, JsonProcessingException {
    StringJoiner stringJoiner = new StringJoiner("");
    stringJoiner.add("SELECT * FROM query_queue WHERE status = ?");
    if (page != 0 && size != 0) stringJoiner.add(" LIMIT ? OFFSET ?");
    try (PreparedStatement ps = PostgresConnectionManager.prepareStatement(stringJoiner.toString())) {
      ps.setString(1, status.toString());
      if (page != 0 && size != 0) {
        ps.setInt(2, size);
        ps.setInt(3, (page - 1) * size);
      }
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
    String sql = "SELECT COUNT(query_iri) AS total FROM query_queue WHERE status = ?";
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

  public static Pageable<DBEntry> findAllByUserIdAndStatus(String userId, QueryExecutorStatus status, int page, int size) throws SQLException, JsonProcessingException {
    StringJoiner stringJoiner = new StringJoiner(" ");
    stringJoiner.add("SELECT * FROM query_queue WHERE user_id = ? AND status = ?");
    if (page != 0 && size != 0) stringJoiner.add(" LIMIT ? OFFSET ?");
    try (PreparedStatement ps = PostgresConnectionManager.prepareStatement(stringJoiner.toString())) {
      ps.setString(1, userId);
      ps.setString(2, status.toString());
      if (page != 0 && size != 0) {
        ps.setInt(3, size);
        ps.setInt(4, (page - 1) * size);
      }
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
    String sql = "SELECT COUNT(query_iri) AS total FROM query_queue WHERE user_id = ? AND status = ?";
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

  private static DBEntry resultSetToDBEntry(ResultSet rs) throws SQLException, JsonProcessingException {
    DBEntry entry = new DBEntry();
    entry.setId(rs.getObject("id", UUID.class));
    entry.setUserId(rs.getObject("user_id", UUID.class));
    entry.setUserName(rs.getString("user_name"));
    entry.setStatus(QueryExecutorStatus.valueOf(rs.getString("status")));
    entry.setQueuedAt(rs.getTimestamp("queued_at").toLocalDateTime());
    if (null != rs.getString("started_at")) entry.setStartedAt(rs.getTimestamp("started_at").toLocalDateTime());
    if (null != rs.getString("finished_at")) entry.setFinishedAt(rs.getTimestamp("finished_at").toLocalDateTime());
    if (null != rs.getString("killed_at")) entry.setKilledAt(rs.getTimestamp("killed_at").toLocalDateTime());
    entry.setQueryIri(rs.getString("query_iri"));
    entry.setQueryName(rs.getString("query_name"));
    entry.setQueryRequest(om.readValue(rs.getString("query_request"), QueryRequest.class));
    entry.setError(rs.getString("error"));
    return entry;
  }
}
