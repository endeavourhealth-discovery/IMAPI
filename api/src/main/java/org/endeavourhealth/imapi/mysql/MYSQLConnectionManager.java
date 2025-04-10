package org.endeavourhealth.imapi.mysql;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class MYSQLConnectionManager {
  private static final String url = "jdbc:mysql://localhost:3306/imapi";
  private static final String user = Optional.ofNullable(System.getenv("user.name")).orElseThrow();
  private static final String password = Optional.ofNullable(System.getenv("password")).orElseThrow();
  private Connection connection;
  private int connectionId;

  private MYSQLConnectionManager() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  private Connection getConnection() throws SQLException {
    if (connection == null) {
      connection = createNewConnection();
      connectionId = getConnectionId();
    }
    return connection;
  }

  private Connection createNewConnection() throws SQLException {
    Properties props = new Properties();
    props.setProperty("user", user);
    props.setProperty("password", password);
    props.setProperty("connectionTimeout", "5000");
    return DriverManager.getConnection(url, props);
  }

  public List<String> executeQuery(String sql) throws SQLException {
    try (Connection executeConnection = getConnection()) {
      try (PreparedStatement statement = executeConnection.prepareStatement(sql)) {
        ResultSet rs = statement.executeQuery();
        List<String> results = new ArrayList<>();
        while (rs.next()) {
          String patientId = rs.getString("PATIENT_ID");
          results.add(patientId);
        }
        return results;
      }
    }
  }

  private int getConnectionId() throws SQLException {
    try (Connection idConnection = getConnection()) {
      try (Statement statement = idConnection.createStatement()) {
        ResultSet resultSet = statement.executeQuery("SELECT CONNECTION_ID()");
        if (resultSet.next()) {
          return resultSet.getInt(1);
        }
      }
    }
    return -1;
  }

  public void killCurrentQuery() throws SQLException {
    try (Connection killConnection = createNewConnection(); Statement stmt = killConnection.createStatement()) {
      String killSql = "KILL QUERY " + connectionId;
      stmt.execute(killSql);
      log.info("Killed active query for connection ID: {}", connectionId);
    }

  }
}
