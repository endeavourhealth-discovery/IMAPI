package org.endeavourhealth.imapi.mysql;

import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.model.requests.QueryRequest;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

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

  public static Connection getConnection() throws SQLException {
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

  public static Set<String> executeQuery(String sql) throws SQLException {
    try (Connection executeConnection = getConnection()) {
      try (PreparedStatement statement = executeConnection.prepareStatement(sql)) {
        ResultSet rs = statement.executeQuery();
        Set<String> results = new HashSet<>();
        while (rs.next()) {
          String patientId = rs.getString("id");
          results.add(patientId);
        }
        return results;
      }
    }
  }

  public static void saveResults(int hashCode, Set<String> results) throws SQLException {
    createTable(hashCode);
    if (!results.isEmpty()) {
      try (Connection connection = getConnection()) {
        String values = "(" + String.join("), \n(", results) + ")";
        String sql = """
                  INSERT INTO `%s` (id) \n VALUES \n %s;
          """.formatted(hashCode, values);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
          statement.execute();
        }
      }
    }
  }

  public static void createTable(int hashCode) throws SQLException {
    try (Connection conn = getConnection()) {
      String sql = """
        CREATE TABLE IF NOT EXISTS `%s` (
            id BIGINT NOT NULL,
            PRIMARY KEY(id)
        )
        """.formatted(hashCode);
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

  public static Set<String> getResults(QueryRequest queryRequest) throws SQLException {
    Set<String> ids = new HashSet<>();
    try (Connection getResultsConnection = getConnection()) {
      try (Statement statement = getResultsConnection.createStatement()) {
        String sql = "SELECT id FROM `" + queryRequest.hashCode() + "`";
        if (queryRequest.getPage() != null && queryRequest.getPage().getPageNumber() > 0 && queryRequest.getPage().getPageSize() > 0) {
          int offset = (queryRequest.getPage().getPageNumber() - 1) * queryRequest.getPage().getPageSize();
          sql = sql + " LIMIT " + queryRequest.getPage().getPageSize() + " OFFSET " + offset + ";";
        } else sql = sql + ";";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
          ids.add(resultSet.getString("id"));
        }
      }
    }
    return ids;
  }

  public static boolean tableExists(int hashCode) throws SQLException {
    try (Connection checkTableConnection = getConnection()) {
      DatabaseMetaData meta = checkTableConnection.getMetaData();
      try (ResultSet rs = meta.getTables(null, null, String.valueOf("`" + hashCode + "`"), new String[]{"TABLE"})) {
        return rs.next();
      }
    }

  }
}
