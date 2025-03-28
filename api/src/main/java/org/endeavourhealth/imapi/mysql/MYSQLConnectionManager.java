package org.endeavourhealth.imapi.mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class MYSQLConnectionManager {
  private static final String url = "jdbc:mysql://localhost:3306/imapi";
  private static final String user = Optional.ofNullable(System.getenv("user.name")).orElseThrow();
  private static final String password = Optional.ofNullable(System.getenv("password")).orElseThrow();
  private Connection connection;

  private MYSQLConnectionManager() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  private Connection getConnection() throws SQLException {
    if (connection == null) {
      Properties props = new Properties();
      props.setProperty("user", user);
      props.setProperty("password", password);
      props.setProperty("connectionTimeout", "5000");
      connection = DriverManager.getConnection(url, props);
    }
    return connection;
  }

  public List<String> executeQuery(String sql) throws SQLException {
    try (Connection connection = getConnection()) {
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
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
}
