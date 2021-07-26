package org.endeavourhealth.imapi.statemachine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.dataaccess.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StateMachineConfigDALJDBC implements StateMachineConfigDAL {
    public void saveConfig(String name, StateMachineConfig config) throws SQLException, JsonProcessingException {
        String json = new ObjectMapper().writeValueAsString(config);

        String sql = "INSERT INTO workflow (name, config) VALUES (?, ?) ON DUPLICATE KEY UPDATE config = ?";
        try(Connection conn = ConnectionPool.get()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, json);
                stmt.setString(3, json);
                stmt.executeUpdate();
            }
        }
    }

    public StateMachineConfig loadConfig(String name) throws SQLException, JsonProcessingException {
        String sql = "SELECT config FROM workflow WHERE name = ?";
        try(Connection conn = ConnectionPool.get()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String json = rs.getString("config");
                        return new ObjectMapper().readValue(json, StateMachineConfig.class);
                    } else {
                        return null;
                    }
                }
            }
        }
    }
}
