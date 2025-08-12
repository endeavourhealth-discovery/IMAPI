package org.endeavourhealth.imapi.postgres;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class PostgresConnectionManager {
  private static final HikariConfig config = new HikariConfig();
  private static HikariDataSource ds = null;

  static {
    config.setJdbcUrl(Optional.ofNullable(System.getenv("POSTGRES_URL")).orElseThrow());
    config.setDriverClassName("org.postgresql.Driver");
    config.setUsername(Optional.ofNullable(System.getenv("POSTGRES_USER")).orElseThrow());
    config.setPassword(Optional.ofNullable(System.getenv("POSTGRES_PASSWORD")).orElseThrow());
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
  }

  private PostgresConnectionManager() {
    throw new IllegalStateException("Utility class");
  }

  public static Connection getConnection() throws SQLException {
    if (ds == null)
      ds = new HikariDataSource(config);

    return ds.getConnection();
  }
}
