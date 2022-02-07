package org.endeavourhealth.imapi.statemachine;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;

public interface StateMachineConfigDAL {
    void saveConfig(String name, StateMachineConfig config) throws SQLException, JsonProcessingException;
    StateMachineConfig loadConfig(String name) throws SQLException, JsonProcessingException;
}
