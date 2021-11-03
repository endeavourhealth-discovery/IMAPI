package org.endeavourhealth.imapi.statemachine;

public interface StateMachineConfigDAL {
    void saveConfig(String name, StateMachineConfig config) throws Exception;
    StateMachineConfig loadConfig(String name) throws Exception;
}
