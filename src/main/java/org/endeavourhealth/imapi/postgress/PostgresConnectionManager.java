package org.endeavourhealth.imapi.postgress;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class PostgresConnectionManager {
  private static final String POSTGRES_URL = Optional.ofNullable(System.getenv("POSTGRES_URL")).orElseThrow(() -> new IllegalArgumentException("Env var 'POSTGRES_URL' is not defined"));
  private static final String POSTGRES_USER = Optional.ofNullable(System.getenv("POSTGRES_USER")).orElseThrow(() -> new IllegalArgumentException("Env var 'POSTGRES_USER' is not defined"));
  private static final String POSTGRES_PASSWORD = Optional.ofNullable(System.getenv("POSTGRES_PASSWORD")).orElseThrow(() -> new IllegalArgumentException("Env var 'POSTGRES_PASSWORD' is not defined"));
  private static Connection connection = null;

  private PostgresConnectionManager() {
    throw new IllegalStateException("Utility class");
  }

  private static Connection getConnection() throws SQLException {
    if (connection != null) {
      return connection;
    }
    Properties props = new Properties();
    props.setProperty("user", POSTGRES_USER);
    props.setProperty("password", POSTGRES_PASSWORD);
    connection = DriverManager.getConnection(POSTGRES_URL, props);
    return connection;
  }

  public static PreparedStatement prepareStatement(String sql) throws SQLException {
    return getConnection().prepareStatement(sql);
  }
}
