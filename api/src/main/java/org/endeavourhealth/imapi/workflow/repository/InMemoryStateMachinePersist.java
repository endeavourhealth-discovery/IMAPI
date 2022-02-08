package org.endeavourhealth.imapi.workflow.repository;

import org.endeavourhealth.imapi.workflow.domain.Events;
import org.endeavourhealth.imapi.workflow.domain.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;

import java.util.HashMap;

public class InMemoryStateMachinePersist implements StateMachinePersist<States, Events, String> {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryStateMachinePersist.class);

    private final HashMap<String, StateMachineContext<States, Events>> contexts = new HashMap<>();

    @Override
    public void write(StateMachineContext<States, Events> context, String contextObj) {
        LOG.debug("********** STATE SAVED");
        contexts.put(contextObj, context);
    }

    @Override
    public StateMachineContext<States, Events> read(String contextObj) {
        LOG.debug("********** STATE LOADED");
        return contexts.get(contextObj);
    }
}