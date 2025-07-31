package org.endeavourhealth.imapi.mysql;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class MYSQLConnectionManager {
  private static final String url = Optional.ofNullable(System.getenv("MYSQL_URL")).orElseThrow();
  private static final String user = Optional.ofNullable(System.getenv("MYSQL_USERNAME")).orElseThrow();
  private static final String password = Optional.ofNullable(System.getenv("MYSQL_PASSWORD")).orElseThrow();
  private static Connection connection;
  private static int connectionId;

  private MYSQLConnectionManager() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  private static Connection getConnection() throws SQLException {
    Connection connection = createNewConnection();
    connectionId = getConnectionId(connection);
    return connection;
  }

  private static Connection createNewConnection() throws SQLException {
    Properties props = new Properties();
    props.setProperty("user", user);
    props.setProperty("password", password);
    return DriverManager.getConnection(url, props);
  }

  public static List<String> executeQuery(String sql) throws SQLException {
    try (Connection executeConnection = getConnection()) {
      try (PreparedStatement statement = executeConnection.prepareStatement(sql)) {
        ResultSet rs = statement.executeQuery();
        List<String> results = new ArrayList<>();
        while (rs.next()) {
          String patientId = rs.getString("id");
          results.add(patientId);
        }
        return results;
      }
    }
  }

  public static void createTable(String tableName) throws SQLException {
    try (Connection conn = getConnection()) {
      String sql = """
        CREATE TABLE IF NOT EXISTS %s (
            id BIGINT NOT NULL,
            iri CHAR(255) NOT NULL,
            PRIMARY KEY(id)
        )
        """.formatted(tableName);
      try (PreparedStatement statement = conn.prepareStatement(sql)) {
        statement.execute();
      }
    }
  }

  private static int getConnectionId(Connection connection) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery("SELECT CONNECTION_ID()");
      if (resultSet.next()) {
        return resultSet.getInt(1);
      }
    }
    return -1;
  }

  public static void killCurrentQuery() throws SQLException {
    try (Connection killConnection = createNewConnection(); Statement stmt = killConnection.createStatement()) {
      String killSql = "KILL QUERY " + connectionId;
      stmt.execute(killSql);
      log.info("Killed active query for connection ID: {}", connectionId);
    }

  }
}
