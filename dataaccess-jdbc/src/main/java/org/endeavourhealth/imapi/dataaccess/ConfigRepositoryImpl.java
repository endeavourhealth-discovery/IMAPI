package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionPool;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.config.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("java:S1192") // Disable "Literals as const" rule for SQL
public class ConfigRepositoryImpl implements ConfigRepository {

    @Override
    public Config findByName(String name) throws DALException {
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
        } catch (SQLException e) {
            throw new DALException("Failed to get config", e);
        }
        return config;
    }

}
