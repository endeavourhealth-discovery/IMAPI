package org.endeavourhealth.imapi.statemachine;

import java.util.HashMap;
import java.util.Map;

public class StateMachineTaskDALMock implements StateMachineTaskDAL {
    private Map<String, String> db = new HashMap<>();

    @Override
    public String load(String type, String id) {
        String state = db.get(id);
        System.out.println("LOAD - Type: " + type + "\tId: " + id + "\tState: " + state);
        return state;
    }

    @Override
    public void save(String type, String id, String state) {
        System.out.println("SAVE - Type: " + type + "\tId: " + id + "\tState: " + state);
        db.put(id,state);
    }
}
