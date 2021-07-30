package org.endeavourhealth.imapi.dataaccess.repository;

import org.endeavourhealth.imapi.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.model.config.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("java:S1192") // Disable "Literals as const" rule for SQL
public class ConfigRepository extends BaseRepository {

    public Config findByName(String name) throws SQLException {
        Config config = new Config();
        try (Connection conn = ConnectionPool.get()) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT config FROM config WHERE name = ?")) {
                stmt.setString(1, name);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next())
                        return null;
                    config.setData(rs.getString("config"));
                }
            }
        }
        return config;
    }

}
