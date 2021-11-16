package org.endeavourhealth.imapi.statemachine;

public interface StateMachineTaskDAL {
    String load(String name, String id) throws Exception;
    void save(String name, String id, String state) throws Exception;
}
