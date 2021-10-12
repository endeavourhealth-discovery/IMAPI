package org.endeavourhealth.imapi.statemachine;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionPool.class);
    private static final Object lock = new Object();
    private static ConnectionPool instance = null;

    public static Connection get() throws SQLException {
        synchronized (lock) {
            if (instance == null) {
                instance = new ConnectionPool();
            }
                return instance.dataSource.getConnection();
        }
    }

    private final HikariDataSource dataSource;

    public ConnectionPool() {
        LOG.debug("Initializing connection pool");

        String url = System.getenv("SPRING_DATASOURCE_URL");
        String user = System.getenv("SPRING_DATASOURCE_USERNAME");
        String pass = System.getenv("SPRING_DATASOURCE_PASSWORD");
        String driver =System.getenv("SPRING_DATASOURCE_DRIVER");

        int max = 10;
        int min = 2;
        int timeout = 120000;

        String maxEnv = System.getenv("SPRING_DATASOURCE_MAX");
        if (maxEnv != null && !maxEnv.isEmpty())
            max = Integer.parseInt(maxEnv);

        String minEnv = System.getenv("SPRING_DATASOURCE_MIN");
        if (minEnv != null && !minEnv.isEmpty())
            min = Integer.parseInt(minEnv);

        String timeoutEnv = System.getenv("SPRING_DATASOURCE_TIMEOUT");
        if (timeoutEnv != null && !timeoutEnv.isEmpty())
            timeout = Integer.parseInt(timeoutEnv);

        if (driver != null && !driver.isEmpty()) {
            LOG.debug("Loading driver");
            try {
                Class.forName(driver);
            } catch (Exception e) {
                LOG.warn(e.getMessage());
            }
        }

        LOG.debug("Configuring data source");
        this.dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(pass);
        dataSource.setMaximumPoolSize(max);
        dataSource.setMinimumIdle(min);
        dataSource.setIdleTimeout(timeout);
    }
}
