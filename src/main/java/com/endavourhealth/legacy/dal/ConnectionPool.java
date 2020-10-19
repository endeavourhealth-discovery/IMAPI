package com.endavourhealth.legacy.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionPool {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionPool.class);
    private static final int VALID_TIMEOUT = 5;
    private static ConnectionPool instance = null;

    public static ConnectionPool getInstance() {
        if (instance == null)
            instance = new ConnectionPool();

        return instance;
    }

    protected Connection create() {
        try {
            String url = "jdbc:mysql://localhost:3306/im_next?useSSL=false";
            String user = "root";
            String pass = "password";
            String driver = "com.mysql.jdbc.Driver";

            if (driver != null && !driver.isEmpty())
                Class.forName(driver);

            Properties props = new Properties();

            props.setProperty("user", user);
            props.setProperty("password", pass);

            Connection connection = DriverManager.getConnection(url, props);    // NOSONAR

            LOG.debug("New DB Connection created");
            return connection;
        } catch (Exception e) {
            LOG.error("Error getting connection", e);
        }
        return null;
    }
}
