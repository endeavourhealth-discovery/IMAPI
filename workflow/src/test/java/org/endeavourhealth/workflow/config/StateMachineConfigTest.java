package org.endeavourhealth.workflow.config;

import org.endeavourhealth.workflow.domain.Events;
import org.endeavourhealth.workflow.domain.States;
import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

@SpringBootTest(classes = {org.endeavourhealth.workflow.config.StateMachineConfig.class})
class StateMachineConfigTest {

    @Autowired
    StateMachineFactory<States, Events> machineFactory;

    @Test
    public void test(){
        StateMachine<States, Events> sm = machineFactory.getStateMachine(UUID.randomUUID());

        sm.start();
        System.out.println(sm.getState().toString());

        sm.sendEvent(Events.PROCESS);
        System.out.println(sm.getState().toString());

        sm.sendEvent(Events.COMPLETE);
        System.out.println(sm.getState().toString());

        sm.sendEvent(Events.DOWNLOAD);
        System.out.println(sm.getState().toString());


    }

}