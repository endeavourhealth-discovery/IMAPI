package org.endeavourhealth.imapi.stateMachine;

public interface StateMachinePersister<I,S>{
    S load(I id);
    void save(I id, S state);
}
