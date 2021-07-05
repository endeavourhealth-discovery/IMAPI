package org.endeavourhealth.imapi.workflow.config;

import org.endeavourhealth.imapi.workflow.domain.Events;
import org.endeavourhealth.imapi.workflow.domain.States;
import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {StateMachineConfig.class})
class StateMachineConfigTest {

    @Autowired
    StateMachineFactory<States, Events> machineFactory;

    @Test
    void test(){
        StateMachine<States, Events> sm = machineFactory.getStateMachine(UUID.randomUUID());

        sm.start();
        assertEquals("UPLOADED", sm.getState().getId().toString());

        sm.sendEvent(Events.PROCESS);
        assertEquals("PROCESSING", sm.getState().getId().toString());

        sm.sendEvent(Events.COMPLETE);
        assertEquals("COMPLETED", sm.getState().getId().toString());

        sm.sendEvent(Events.DOWNLOAD);
        assertEquals("DOWNLOADED", sm.getState().getId().toString());

    }

}
