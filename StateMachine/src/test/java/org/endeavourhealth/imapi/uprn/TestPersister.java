package org.endeavourhealth.imapi.uprn;

import org.endeavourhealth.imapi.stateMachine.StateMachinePersister;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TestPersister implements StateMachinePersister<UUID,String> {

    private Map<UUID,String> db = new HashMap<>();
    @Override
    public String load(UUID id) {
        System.out.println("Id: "+id);
        return db.get(id);
    }

    @Override
    public void save(UUID id, String state) {
        System.out.println("Id: "+id);
        System.out.println("Current State: "+state);
        db.put(id,state);

    }
}
