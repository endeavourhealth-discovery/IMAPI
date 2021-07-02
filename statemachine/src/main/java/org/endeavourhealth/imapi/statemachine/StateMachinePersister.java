package org.endeavourhealth.imapi.statemachine;

public interface StateMachinePersister<I,S>{
    S load(I id);
    void save(I id, S state);
}
