package org.endeavourhealth.imapi.statemachine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StateMachineTaskDALJDBC implements StateMachineTaskDAL {
  @Override
  public String load(String type, String id) throws SQLException {
    String sql = "SELECT t.state FROM workflow w JOIN task t ON t.workflow = w.dbid WHERE w.name = ? AND t.id = ?";

    try (Connection conn = ConnectionPool.get()) {
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, type);
        stmt.setString(2, id);
        try (ResultSet rs = stmt.executeQuery()) {
          if (rs.next())
            return rs.getString("state");
          else
            return null;
        }
      }
    }
  }

  @Override
  public void save(String type, String id, String state) throws SQLException {
    String sql = "INSERT INTO task (workflow, id, state) SELECT w.dbid, ?, ? FROM workflow w WHERE w.name = ? ON DUPLICATE KEY UPDATE state = ?";

    try (Connection conn = ConnectionPool.get()) {
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, id);
        stmt.setString(2, state);
        stmt.setString(3, type);
        stmt.setString(4, state);
        stmt.executeUpdate();
      }
    }
  }
}
